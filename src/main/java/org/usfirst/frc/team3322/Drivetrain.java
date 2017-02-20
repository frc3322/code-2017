package org.usfirst.frc.team3322;

import java.lang.Math;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Drivetrain {

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1, drive_left_2, drive_right_1, drive_right_2,indenturedServantL,indenturedServantR;
    private Encoder enc_left, enc_right;

    double robotSpeed, lowThreshold, highThreshold;
    int numSamples, cooldown, invert;
    int shiftCounter = 0;

    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    public static final double DIAMETER_WHEEL = 0.5;

    Drivetrain(double lowThreshold, double highThreshold, int numSamples, int cooldown) {
        this(lowThreshold, highThreshold, numSamples, cooldown, false, false);
    }
    Drivetrain(double lowThreshold, double highThreshold, int numSamples, int cooldown, boolean left_inv, boolean right_inv) {
        drive_left_1 = new CANTalon(RobotMap.driveLeft_1);
        drive_left_2 = new CANTalon(RobotMap.driveLeft_2);
        drive_right_1 = new CANTalon(RobotMap.driveRight_1);
        drive_right_2 = new CANTalon(RobotMap.driveRight_2);
        indenturedServantL = new CANTalon(RobotMap.indenturedServantL);
        indenturedServantR = new CANTalon(RobotMap.indenturedServantR);

        drive_left_1.setInverted(left_inv);
        drive_left_2.setInverted(left_inv);
        indenturedServantL.setInverted(left_inv);
        drive_right_1.setInverted(right_inv);
        drive_right_2.setInverted(right_inv);
        indenturedServantR.setInverted(right_inv);

        // This could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);
        indenturedServantL.changeControlMode(CANTalon.TalonControlMode.Follower);//setting indentured servants to follow the master talon drive_right || left_1
        indenturedServantR.changeControlMode(CANTalon.TalonControlMode.Follower);
        indenturedServantL.set(drive_left_1.getDeviceID());//getting the device ID for the thingies
        indenturedServantR.set(drive_right_1.getDeviceID());
        shifter = new DoubleSolenoid(RobotMap.shifter_1, RobotMap.shifter_2);

        enc_left = new Encoder(RobotMap.encLeft_1, RobotMap.encLeft_2);
        enc_right = new Encoder(RobotMap.encRight_1, RobotMap.encRight_2);

        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        this.numSamples = numSamples;
        this.cooldown = cooldown;

        leftSamples = new double[numSamples];
        rightSamples = new double[numSamples];
        sampleIndex = 0;

        for (int i = 0; i < numSamples; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }

        SmartDashboard.putNumber("low_gear", lowThreshold);
        SmartDashboard.putNumber("high_gear", highThreshold);
        SmartDashboard.putNumber("num_samples", numSamples);
        SmartDashboard.putNumber("cooldown", cooldown);
    }

    public void resetEncs() {
        enc_left.reset();
        enc_right.reset();
    }

    public double getLeftEncValue() { //returns in inches
        return enc_left.getDistance() / 67;
    }
    public double getRightEncValue() { //returns in inches
        return enc_right.getDistance() / 67;
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
    public double wheelRPS(Encoder e) {
        return encoderRPS(e) / 3.0;
    }
    public double wheelFloorSpeed(Encoder e) {
        return wheelRPS(e) * Math.PI * DIAMETER_WHEEL;
    }

    public boolean isHigh() { return shifter.get() == DoubleSolenoid.Value.kReverse; }
    public void shiftHigh() {
        shifter.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putString("shift_state", "high");
    }
    public void shiftLow() {
        shifter.set(DoubleSolenoid.Value.kForward);
        SmartDashboard.putString("shift_state", "low");
    }

    public void configFromDashboard(double highThreshold, double lowThreshold, int numSamples, int cooldown) {
        this.highThreshold = highThreshold;
        this.lowThreshold = lowThreshold;
        this.numSamples = numSamples;
        this.cooldown = cooldown;
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

        leftAvg /= (double)numSamples;
        rightAvg /= (double)numSamples;

        robotSpeed = Math.max(leftAvg, rightAvg);

        if (shiftCounter > cooldown) {
            if (robotSpeed > highThreshold) {
                if (!isHigh()) {
                    shiftHigh();
                    shiftCounter = 0;
                }
            } else if (robotSpeed < lowThreshold) {
                if (isHigh()) {
                    shiftLow();
                    shiftCounter = 0;
                }
            }
        }

        SmartDashboard.putNumber("robot_speed", robotSpeed);
    }

    public void configFromDashboard() {
        highThreshold = SmartDashboard.getNumber("high_gear", 0);
        lowThreshold = SmartDashboard.getNumber("low_gear", 0);
        numSamples = (int)SmartDashboard.getNumber("num_samples", 0);
        cooldown = (int)SmartDashboard.getNumber("shift_threshold", 0);
    }
}
