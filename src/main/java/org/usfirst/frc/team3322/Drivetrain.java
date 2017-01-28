package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;


public class Drivetrain {

    public static final boolean SHIFT_HIGH = true;
    public static final boolean SHIFT_LOW  = false;
    private static final int NUM_SAMPLES = 20;
    private static final int SHIFT_THRESHOLD = 3;

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1, drive_left_2, drive_left_3, drive_right_1, drive_right_2, drive_right_3;
    private Encoder leftEnc, rightEnc;

    private double lowGear, highGear;
    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    int highCounter = 0, lowCounter = 0;

    Drivetrain(double low, double high, boolean left_inv, boolean right_inv){
        drive_left_1 = new CANTalon(RobotMap.driveLeft_1);
        drive_left_2 = new CANTalon(RobotMap.driveLeft_2);
        drive_left_3 = new CANTalon(RobotMap.driveLeft_3);
        drive_right_1 = new CANTalon(RobotMap.driveRight_1);
        drive_right_2 = new CANTalon(RobotMap.driveRight_2);
        drive_right_3 = new CANTalon(RobotMap.driveRight_3);

        // Invert our motors according to our inversion variables
        drive_left_1.setInverted(left_inv);
        drive_left_2.setInverted(left_inv);
        drive_left_3.setInverted(left_inv);
        drive_right_1.setInverted(right_inv);
        drive_right_2.setInverted(right_inv);
        drive_right_3.setInverted(right_inv);

        // Initialize our RobotDrive object - this could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);

        // Shifter for our gearboxes
        shifter = new DoubleSolenoid(RobotMap.shifter_1, RobotMap.shifter_2);

        leftEnc = new Encoder(RobotMap.encoderLeft_A, RobotMap.encoderLeft_B);
        rightEnc = new Encoder(RobotMap.encoderRight_A, RobotMap.encoderRight_B);

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
        leftSamples[sampleIndex] = leftEnc.getRate();
        rightSamples[sampleIndex++] = rightEnc.getRate();
        if (sampleIndex >= NUM_SAMPLES)
            sampleIndex = 0;
    }

    void drive(double power, double turnPower){
        drive.arcadeDrive(power, turnPower);
    }

    public boolean isHigh() {
        return shifter.get() == DoubleSolenoid.Value.kReverse;
    }
    public void toggleGear() {
        shifter.set(isHigh() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }
    public void shiftHigh() {
        shifter.set(DoubleSolenoid.Value.kReverse);
    }

    public void shiftLow() {
        shifter.set(DoubleSolenoid.Value.kForward);
    }

    public void autoShift()
    {
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
    public double distance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }
}
