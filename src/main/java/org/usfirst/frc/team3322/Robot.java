package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	AHRS navx;
    Auton auton;
    Ultrasonic sonar;

	@Override
    public void robotInit() {
	    // Initialize required object classes
        drivetrain = new Drivetrain(3000.0, 4000.0,true, false);
        climber = new Climber();
        sonar = new Ultrasonic(6, 6);
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
    public void disabledInit() {}

    @Override
    public void autonomousInit() {
	    SmartDashboard.putString("test", "testvalue");
    }

    @Override
    public void teleopInit() {
        compressor.start();
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {
	    SmartDashboard.putNumber("Sonar distance", sonar.getRangeInches());
    }

    @Override
    public void teleopPeriodic() {
	    if(joystick.getRawButton(Xbox.LBUMPER)) {
            climber.climb(true);
        }
        else climber.climb(false);
	    if(joystick.getRawButton(Xbox.ABUTTON)) {
	        gear.extendHolder();
        }
        else{
	        gear.retractHolder();
        }
    }
}
