package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * Created by snekiam on 1/10/2017.
 * Main Robot class for 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    // Define our global variables
    Drivetrain drivetrain;
    Compressor compressor;
	private AHRS navx;

	@Override
    public void robotInit() {
        // Init our compressor as PCM number 1
        compressor = new Compressor(1);
        // NavX gyroscope init
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
    public void autonomousInit() {
	    // foo
    }

    @Override
    public void teleopInit() {
        compressor.start();
    }
}
