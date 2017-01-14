package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

/**
 * Created by snekiam on 1/10/2017.
 * Main Robot class for 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    //define our global variables
    private Joystick driver, tech;
    private AHRS navx;
    private DoubleSolenoid shifter;
    private RobotDrive drive;
    @Override
    public void robotInit() {
        super.robotInit();
        //define local CANTalon variables for our drivetrain
        CANTalon drive_left_1,drive_left_2,drive_left_3,drive_right_1,drive_right_2,drive_right_3;
        //are our left or right drive motors inverted?
        boolean invert_left_drive_motors = true;
        boolean invert_right_drive_motors = false;
        //init our compressor as PCM number 1
        Compressor compressor = new Compressor(1);
        //init the driver joystick as number 0, and tech as number 1
        driver = new Joystick(0);
        tech = new Joystick(1);
        /*
        Drivetrarin Stuff - pneumatic shifting, and drive minicims [citation needed], and NavX
         */
        navx = new AHRS(SerialPort.Port.kUSB);
        shifter = new DoubleSolenoid(0,1);
        drive_left_1 = new CANTalon(1);
        drive_left_2 = new CANTalon(2);
        drive_left_3 = new CANTalon(3);
        drive_right_1 = new CANTalon(4);
        drive_right_2 = new CANTalon(5);
        drive_right_3 = new CANTalon(6);
        //invert our motors according to our inversion variables
        drive_left_1.setInverted(invert_left_drive_motors);
        drive_left_2.setInverted(invert_left_drive_motors);
        drive_left_3.setInverted(invert_left_drive_motors);
        drive_right_1.setInverted(invert_right_drive_motors);
        drive_right_2.setInverted(invert_right_drive_motors);
        drive_right_3.setInverted(invert_right_drive_motors);
        //initialize our RobotDrive object - this could (should) be replaced with something like our 2016
        drive = new RobotDrive(drive_left_1,drive_left_2,drive_right_1,drive_right_2);
    }
    @Override
    public void disabledPeriodic() {
        super.disabledPeriodic();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
    }

    @Override
    public void disabledInit() {
        super.disabledInit();
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
    }
}
