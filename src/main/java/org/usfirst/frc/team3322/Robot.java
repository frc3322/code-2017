package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * Created by snekiam on 1/10/2017.
 * Main Robot class for 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    //define our global variables
    private Joystick driver, tech;
    private AHRS navx;
    Drivetrain drivetrain;
    //GearCollection gearCollection;
    Compressor compressor;
    CANTalon w1,w2; //CANTalons for winch
    @Override
    public void robotInit() {
        //init our compressor as PCM number 1
        compressor = new Compressor(1);
        //init the driver joystick as number 0, and tech as number 1
        driver = new Joystick(0);
        tech = new Joystick(1);
        //NavX gyroscope init
        navx = new AHRS(SerialPort.Port.kUSB);
        drivetrain = new Drivetrain();
        //gearCollection = new GearCollection();
        //gearCollection.init();
        drivetrain.init(true,false);
        w1 = new CANTalon(40);
        w2 = new CANTalon(41);
    }
    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(driver.getX(),driver.getY());
        if(driver.getRawButton(XBox.ABUTTON)){
            w1.set(1);
            w2.set(1);
        }
        else{
            w1.set(0);
            w2.set(0);
        }
    }

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void teleopInit() {
        compressor.start();
    }
}
