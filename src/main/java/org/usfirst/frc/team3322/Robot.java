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
    // Define our global variables
    Drivetrain drivetrain;
    Compressor compressor;
    Climber climber;
    Joystick joystick;
	private AHRS navx;

	@Override
    public void robotInit() {
        // Init our compressor as PCM number 1
        compressor = new Compressor(1);
        // NavX gyroscope init
        navx = new AHRS(SerialPort.Port.kUSB);
        drivetrain = new Drivetrain();
        drivetrain.init(true,false);
        climber = new Climber();
        joystick = new Joystick(1);
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
