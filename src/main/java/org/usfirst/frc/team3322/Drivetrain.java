package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Created by sneki on 1/15/2017.
 * This class contains everything needed to initialize and use the drivetrain on 3322's 2017 robot
 */
public class Drivetrain {

    public static final boolean SHIFT_HIGH = true;
    public static final boolean SHIFT_LOW  = false;
    private static final int NUM_SAMPLES = 20;
    private static final int SHIFT_THRESHOLD = 3;

    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1,drive_left_2,drive_left_3,drive_right_1,drive_right_2,drive_right_3;
    private Encoder leftEnc, rightEnc;

    private double lowGear, highGear;
    private int sampleIndex;
    private double leftSamples[], rightSamples[];
    int highCounter = 0, lowCounter = 0;

    Drivetrain(double low, double high, boolean left_inv, boolean right_inv){
        init(left_inv, right_inv);
        lowGear = low;
        highGear = high;
        leftSamples = new double[NUM_SAMPLES]; rightSamples = new double[NUM_SAMPLES];
        sampleIndex = 0;

        for (int i = 0; i < NUM_SAMPLES; i++) {
            leftSamples[i] = 0;
            rightSamples[i] = 0;
        }
    }

    private void init(boolean invert_left, boolean invert_right) {
        drive_left_1 = new CANTalon(20);
        drive_left_2 = new CANTalon(21);
        drive_left_3 = new CANTalon(3);
        drive_right_1 = new CANTalon(22);
        drive_right_2 = new CANTalon(23);
        drive_right_3 = new CANTalon(6);

        //invert our motors according to our inversion variables
        drive_left_1.setInverted(invert_left);
        drive_left_2.setInverted(invert_left);
        drive_left_3.setInverted(invert_left);
        drive_right_1.setInverted(invert_right);
        drive_right_2.setInverted(invert_right);
        drive_right_3.setInverted(invert_right);
        //initialize our RobotDrive object - this could (should) be replaced with something like our 2016 gyro driving code
        drive = new RobotDrive(drive_left_1, drive_left_2, drive_right_1, drive_right_2);
        //shifter for our gearboxes
        shifter = new DoubleSolenoid(1,3);

        leftEnc = new Encoder(0, 1); rightEnc = new Encoder(2, 3);
    }

    public void getSample() {
        leftSamples[sampleIndex] = leftEnc.getRate();
        rightSamples[sampleIndex++] = rightEnc.getRate();
        if (sampleIndex >= NUM_SAMPLES)
            sampleIndex = 0;
    }

    void drive(double x, double y){
        drive.arcadeDrive(x,y);
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
}
