package org.usfirst.frc.team3322;

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
    Compressor compressor;

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
        drivetrain.init(true,false);
    }
    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void teleopInit() {
        compressor.start();
    }
}
