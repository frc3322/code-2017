package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;


public class Drivetrain {

    public static final boolean SHIFT_HIGH = true;
    public static final boolean SHIFT_LOW  = false;
    private static final int NUM_SAMPLES = 20;
    private static final int SHIFT_THRESHOLD = 3;

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private Talon drive_left_1, drive_left_2, drive_right_1, drive_right_2;
    private Encoder enc_left, enc_right;

    private double lowGear, highGear;
    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    int highCounter = 0, lowCounter = 0;

    Drivetrain(double low, double high, boolean left_inv, boolean right_inv){
        drive_left_1 = new Talon(RobotMap.driveLeft_1);
        drive_left_2 = new Talon(RobotMap.driveLeft_2);
        drive_right_1 = new Talon(RobotMap.driveRight_1);
        drive_right_2 = new Talon(RobotMap.driveRight_2);

        // Invert our motors according to our inversion variables
        drive_left_1.setInverted(left_inv);
        drive_left_2.setInverted(left_inv);
        drive_right_1.setInverted(right_inv);
        drive_right_2.setInverted(right_inv);

        // Initialize our RobotDrive object - this could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);

        // Shifter for our gearboxes
        shifter = new DoubleSolenoid(RobotMap.shifter_1, RobotMap.shifter_2);

        enc_left = new Encoder(RobotMap.encoderLeft_A, RobotMap.encoderLeft_B);
        enc_right = new Encoder(RobotMap.encoderRight_A, RobotMap.encoderRight_B);

        lowGear = low;
        highGear = high;
        leftSamples = new double[NUM_SAMPLES]; rightSamples = new double[NUM_SAMPLES];
        sampleIndex = 0;

        for (int i = 0; i < NUM_SAMPLES; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }
    }

    public void getSample() {
        leftSamples[sampleIndex] = enc_left.getRate();
        rightSamples[sampleIndex++] = enc_right.getRate();
        if (sampleIndex >= NUM_SAMPLES)
            sampleIndex = 0;
    }

    public void drive(double move, double rotate) {
        drive.arcadeDrive(move * -1, rotate * -1);
    }

    public boolean isHigh() { return shifter.get() == DoubleSolenoid.Value.kReverse; }
    public void toggleGear() { shifter.set(isHigh() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse); }
    public void shiftHigh() { shifter.set(DoubleSolenoid.Value.kReverse); }
    public void shiftLow() { shifter.set(DoubleSolenoid.Value.kForward); }

    public void autoShift() {
        double leftAvg = 0.0, rightAvg = 0.0;
        for(double i : leftSamples)
            leftAvg += i;
        for (double i : rightSamples)
            rightAvg += i;

        leftAvg /= ((double) NUM_SAMPLES);
        rightAvg /= ((double)NUM_SAMPLES);

        if(Math.abs((leftAvg + rightAvg) / 2.0) > highGear){
            highCounter++;
            if(highCounter > SHIFT_THRESHOLD)
                shiftHigh();
           lowCounter = 0;
        }else if(Math.abs((leftAvg + rightAvg)/2.0) < lowGear) {
            lowCounter++;
            if(lowCounter > SHIFT_THRESHOLD)
                shiftLow();
            highCounter = 0;
        }else {
            highCounter = lowCounter = 0;
        }
    }
}
