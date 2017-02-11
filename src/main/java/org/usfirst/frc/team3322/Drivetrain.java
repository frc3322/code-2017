package org.usfirst.frc.team3322;

import java.lang.Math;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Drivetrain {

    public static final boolean SHIFT_HIGH = true;
    public static final boolean SHIFT_LOW  = false;
    private static final int NUM_SAMPLES = 20;
    private static final int SHIFT_THRESHOLD = 3;

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1, drive_left_2, drive_right_1, drive_right_2;
    private Encoder enc_left, enc_right;

    private double lowRPM, highRPM;
    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    int highCounter = 0, lowCounter = 0;
    public boolean invertInput = false;

    Drivetrain(double lowRPM, double highRPM) {
        this(lowRPM, highRPM, false, false);
    }

    Drivetrain(double lowRPM, double highRPM, boolean left_inv, boolean right_inv) {
        drive_left_1 = new CANTalon(RobotMap.driveLeft_1);
        drive_left_2 = new CANTalon(RobotMap.driveLeft_2);
        drive_right_1 = new CANTalon(RobotMap.driveRight_1);
        drive_right_2 = new CANTalon(RobotMap.driveRight_2);

        drive_left_1.setInverted(left_inv);
        drive_left_2.setInverted(left_inv);
        drive_right_1.setInverted(right_inv);
        drive_right_2.setInverted(right_inv);

        // This could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);

        shifter = new DoubleSolenoid(RobotMap.shifter_1, RobotMap.shifter_2);

        enc_left = new Encoder(RobotMap.encLeft_1, RobotMap.encLeft_2);
        enc_right = new Encoder(RobotMap.encRight_1, RobotMap.encRight_2);

        this.lowRPM = lowRPM;
        this.highRPM = highRPM;
        leftSamples = new double[NUM_SAMPLES]; rightSamples = new double[NUM_SAMPLES];
        sampleIndex = 0;

        for (int i = 0; i < NUM_SAMPLES; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }
    }

    public void drive(double move, double rotate) {
        int invert = 1;
        if(invertInput) {
            invert = -1;
        }
        drive.arcadeDrive(invert * move, invert * rotate);
    }
    public void driveAngle(double targetAngle, double speed) { // in degrees
        double pTerm = SmartDashboard.getNumber("driveAnglePTerm", .2); // a constant that controls the sensitivity of the angle follower - has not been tuned
        double angle = Robot.navx.getAngle() % 360;
        double turn = (targetAngle - angle) * pTerm;
        // if robot corrects in wrong direction, either switch targetAngle with angle or make k negative
        drive.arcadeDrive(speed, turn);
    }
    void driveAngleDistance(double targetAngle, double speed, double distance) { //distance in meters, angle in degrees
        double distToEncoder = 7.5 * .3 * Math.PI; //7.5 for encoder gearing, .3 needs to be adjusted - should equal diameter of wheel in meters, pi to compute circumference
        double initEncoderValue = enc_left.getDistance();
        while(distance * distToEncoder + initEncoderValue >= drive_left_1.getEncPosition()) {
            driveAngle(targetAngle, speed);
        }
    }

    public double getRPM(Encoder e) {
        return e.getRate() / 256.0 * 60.0 / (isHigh() ? 1.0588 : 0.4896);
    }
    public double getWheelRPM(Encoder e) {
        return e.getRate() / 256.0 * 60.0 / 7.5;
    }

    public boolean isHigh() { return shifter.get() == DoubleSolenoid.Value.kReverse; }
    public void toggleGear() { shifter.set(isHigh() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse); }
    public void shiftHigh() { shifter.set(DoubleSolenoid.Value.kReverse); }
    public void shiftLow() { shifter.set(DoubleSolenoid.Value.kForward); }

    void getSample() {
        leftSamples[sampleIndex] = getRPM(enc_left);
        rightSamples[sampleIndex++] = getRPM(enc_right);
        if (sampleIndex >= NUM_SAMPLES)
            sampleIndex = 0;
    }
    public void setShiftPoints(double highRPM, double lowRPM) {
        this.highRPM = highRPM;
        this.lowRPM = lowRPM;
    }

    public void autoShift() {
        getSample();

        double leftAvg = 0.0, rightAvg = 0.0;
        for(double i : leftSamples)
            leftAvg += Math.abs(i);
        for (double i : rightSamples)
            rightAvg += Math.abs(i);

        leftAvg /= ((double) NUM_SAMPLES);
        rightAvg /= ((double)NUM_SAMPLES);

        if (Math.abs((leftAvg + rightAvg) / 2.0) > highRPM) {
            highCounter++;
            if(highCounter > SHIFT_THRESHOLD)
                shiftHigh();
           lowCounter = 0;
        } else if (Math.abs((leftAvg + rightAvg)/2.0) < lowRPM) {
            lowCounter++;
            if(lowCounter > SHIFT_THRESHOLD)
                shiftLow();
            highCounter = 0;
        } else {
            // Reset counters when not within thresholds
            highCounter = lowCounter = 0;
        }
    }

    public void showRPM() {
        SmartDashboard.putNumber("Left motor (RPM)", Math.abs(getRPM(enc_left)));
        SmartDashboard.putNumber("Right motor (RPM)", Math.abs(getRPM(enc_right)));
    }
}
