package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    public static OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    static AHRS navx;
    Compressor compressor;
    Holder holder;

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(
            1300,
            1600,
            false,
            false
        );
        SmartDashboard.putNumber("Low gear", 1300);
        SmartDashboard.putNumber("High gear", 1600);
        holder = new Holder();
        climber = new Climber();

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kUSB);
    }

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void teleopInit() {}

    @Override
    public void disabledPeriodic() {
        drivetrain.setShiftPoints(
            SmartDashboard.getNumber("High gear", 0),
            SmartDashboard.getNumber("Low gear", 0));
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {
        xbox.invertInput = xbox.getButtonDown(OI.BACK);
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        climber.climb(xbox.getButton(OI.LBUMPER));

	    if (xbox.getButtonDown(OI.RBUMPER)) {
	        holder.toggle();
	    }

	    if(xbox.getButton(OI.ABUTTON)){
	        climber.climbManual();
        }
        else{
	        climber.stop();
        }

        drivetrain.autoShift();

        SmartDashboard.putNumber("Left motor (RPM)", Math.abs(drivetrain.getRPM(drivetrain.enc_left)));
        SmartDashboard.putNumber("Right motor (RPM)", Math.abs(drivetrain.getRPM(drivetrain.enc_right)));
    }
}
