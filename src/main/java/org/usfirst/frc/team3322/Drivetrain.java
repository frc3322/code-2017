package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Created by sneki on 1/15/2017.
 * This class contains everything needed to initialize and use the drivetrain on 3322's 2017 robot
 */
public class Drivetrain {
    private RobotDrive drive;
    private DoubleSolenoid shifter;
    private CANTalon drive_left_1,drive_left_2,drive_left_3,drive_right_1,drive_right_2,drive_right_3;
    Drivetrain(){
    }
    void init(boolean invert_left, boolean invert_right) {
        drive_left_1 = new CANTalon(1);
        drive_left_2 = new CANTalon(2);
        drive_left_3 = new CANTalon(3);
        drive_right_1 = new CANTalon(4);
        drive_right_2 = new CANTalon(5);
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
        shifter = new DoubleSolenoid(0,1);

    }
    void drive(double x, double y){
        drive.arcadeDrive(x,y);
    }
}
