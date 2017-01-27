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
    AnalogInput sonar;
    DoubleSolenoid doubleSolenoid1;
    DoubleSolenoid doubleSolenoid2;
    DigitalInput ir;
	@Override
    public void robotInit() {
	    // Initialize required object classes
        drivetrain = new Drivetrain(3000.0, 4000.0,true, false);
        climber = new Climber();
        // Init our compressor as PCM number 1
        compressor = new Compressor(0);
        //Init navx gyroscope
        navx = new AHRS(SerialPort.Port.kUSB);
        joystick = new Joystick(1); //port 1 of computer
        gear = new Gear();
        sonar = new AnalogInput(0); //put it in analog
        //TODO what RPM should these be?
        //drivetrain = new Drivetrain(1000,2500,true, false)
        doubleSolenoid1 = new DoubleSolenoid();
        doubleSolenoid2 = new DoubleSolenoid();
        //both solenoids are working together
        ir = new DigitalInput(1);
    }

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {
	    compressor.start();
    }

    @Override
    public void teleopInit() {
        compressor.start();
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {
	    SmartDashboard.putNumber("Sonar distance", sonar.getValue());
	    SmartDashboard.putBoolean("Ir state", ir.get());

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
