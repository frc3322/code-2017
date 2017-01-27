package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main Robot class for team 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    // Define our global variables
    Drivetrain drivetrain;
    Compressor compressor;
    Climber climber;
    OI xbox;
    Gear gear;
    AHRS navx;
    Auton auton;

    @Override
    public void robotInit() {
        // Initialize required object classes
        drivetrain = new Drivetrain(3000.0, 4000.0,true, false); // TODO what RPM should these be?
        climber = new Climber();
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kUSB);
        xbox = new OI(1);
        gear = new Gear();
        auton = new Auton();
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
	    SmartDashboard.putNumber("Sonar distance", auton.sonar.getValue());
	    SmartDashboard.putBoolean("Ir state", auton.ir.get());

	    if (auton.sonar.getVoltage() > 0.1) {
	        drivetrain.drive(1, 0);
        }
    }

    @Override
    public void teleopPeriodic() {
        climber.climb(xbox.getButtonDown(OI.LBUMPER));

	    if (xbox.getButton(OI.ABUTTON)) {
	        gear.extendHolder();
	    } else {
	        gear.retractHolder();
	    }
    }
}
