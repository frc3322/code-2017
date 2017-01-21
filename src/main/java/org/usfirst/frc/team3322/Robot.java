package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * Created by snekiam on 1/10/2017.
 * Main Robot class for team 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    // Define our global variables
    Drivetrain drivetrain;
    Compressor compressor;
    Climber climber;
    Joystick joystick;
    Gear gear;
	private AHRS navx;

	@Override
    public void robotInit() {
	    // Initialize required object classes
        drivetrain = new Drivetrain(3000.0, 4000.0,true, false);
        climber = new Climber();

        // Init our compressor as PCM number 1
        compressor = new Compressor(0);
        // Init NavX gyroscope
        navx = new AHRS(SerialPort.Port.kUSB);
        joystick = new Joystick(1);
        gear = new Gear();
        //TODO what RPM should these be?
        //drivetrain = new Drivetrain(1000,2500,true, false);
    }
    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {
	    if(joystick.getRawButton(Xbox.LBUMPER)) {
            climber.climb(true);
        }
        else climber.climb(false);
	    if(joystick.getRawButton(Xbox.ABUTTON)){
	        gear.extendHolder();
        }
        else{
	        gear.retractHolder();
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
