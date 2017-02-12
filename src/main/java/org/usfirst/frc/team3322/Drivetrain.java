package org.usfirst.frc.team3322;

import java.lang.Math;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Drivetrain {
    double lowRPM, highRPM;
    int numSamples = 3;
    int shiftThreshold = 50;

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1, drive_left_2, drive_right_1, drive_right_2;
    private Encoder enc_left, enc_right;

    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    int shiftCounter = 0;
    public double invert;

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

        leftSamples = new double[numSamples];
        rightSamples = new double[numSamples];
        sampleIndex = 0;

        for (int i = 0; i < numSamples; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }

        SmartDashboard.putNumber("Num samples", numSamples);
        SmartDashboard.putNumber("Shift threshold", shiftThreshold);
    }

    public void resetEncs() {
        enc_left.reset();
        enc_right.reset();
    }

    public double getLeftEncValue() { //returns in feet
        return enc_left.getDistance() / 714;
    }

    public double getRightEncValue() { //returns in feet
        return enc_right.getDistance() / 714;
    }

    public void direction(boolean inverted) {
        invert = inverted ? -1 : 1;
    }

    public void drive(double move, double rotate) {
        move *= invert;
        drive.arcadeDrive(move, rotate);
    }
    public void driveAngle(double targetAngle, double speed) { // in degrees
        double pTerm = SmartDashboard.getNumber("driveAnglePTerm", .05);
        double angle = Robot.navx.getYaw();
        double turn = (targetAngle - angle) * pTerm;
        drive.arcadeDrive(speed, turn);
    }

    public double encoderRPS(Encoder e) {
        return e.getRate() / 256.0;
    }
    public double motorRPS(Encoder e) {
        return encoderRPS(e) / (isHigh() ? 1.0588 : 0.4896);
    }
    public double motorRPM(Encoder e) {
        return motorRPS(e) * 60.0;
    }
    public double wheelRPS(Encoder e) {
        return encoderRPS(e) / 7.5;
    }
    public double wheelRPM(Encoder e) {
        return wheelRPS(e) * 60.0;
    }
    public double wheelFloorSpeed(Encoder e) {
        return wheelRPS(e) * 0.5 * 3.14;
    }

    public boolean isHigh() { return shifter.get() == DoubleSolenoid.Value.kReverse; }
    public void toggleGear() { shifter.set(isHigh() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse); }
    public void shiftHigh() {
        shifter.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putString("Shift state", "High");
    }
    public void shiftLow() {
        shifter.set(DoubleSolenoid.Value.kForward);
        SmartDashboard.putString("Shift state", "Low");
    }

    public void configFromDashboard(double highRPM, double lowRPM, int numSamples, int shiftThreshold) {
        this.highRPM = highRPM;
        this.lowRPM = lowRPM;
        this.numSamples = numSamples;
        this.shiftThreshold = shiftThreshold;
    }

    public void autoShift() {
        shiftCounter++;

        leftSamples[sampleIndex] = wheelFloorSpeed(enc_left);
        rightSamples[sampleIndex++] = wheelFloorSpeed(enc_right);
        if (sampleIndex >= numSamples)
            sampleIndex = 0;

        double leftAvg = 0.0, rightAvg = 0.0;
        for (int i = 0; i < leftSamples.length; ++i) {
            leftAvg += Math.abs(leftSamples[i]);
        }
        for (int i = 0; i < rightSamples.length; ++i) {
            rightAvg += Math.abs(rightSamples[i]);
        }

        leftAvg /= (double) numSamples;
        rightAvg /= (double) numSamples;

        // TODO Test which method is best
        //double currentSpeed = (leftAvg + rightAvg) / 2.0;
        double currentSpeed = Math.max(leftAvg, rightAvg);
        //double currentSpeed = Math.min(leftAvg, rightAvg);

        if (currentSpeed > highRPM) {
            if (shiftCounter > shiftThreshold) {
                if (!isHigh()) {
                    shiftHigh();
                    shiftCounter = 0;
                }
            }
        } else if (currentSpeed < lowRPM) {
            if (shiftCounter > shiftThreshold) {
                if (isHigh()) {
                    shiftLow();
                    shiftCounter = 0;
                }
            }
        }

        SmartDashboard.putNumber("Left motor (RPM)", motorRPM(enc_left));
        SmartDashboard.putNumber("Right motor (RPM)", motorRPM(enc_right));
        SmartDashboard.putNumber("Current speed (FPS)", currentSpeed);
        SmartDashboard.putNumber("Shift counter", shiftCounter);
    }

    public void configFromDashboard() {
        highRPM = SmartDashboard.getNumber("High gear", 0);
        lowRPM = SmartDashboard.getNumber("Low gear", 0);
        numSamples = (int)SmartDashboard.getNumber("Num samples", 0);
        shiftThreshold = (int)SmartDashboard.getNumber("Shift threshold", 0);
    }
}
