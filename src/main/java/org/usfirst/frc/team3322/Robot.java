package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    OI xbox;
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
                3000,
                4000,
                false,
                false
        );
        SmartDashboard.putNumber("Low gear", 3000);
        SmartDashboard.putNumber("High gear", 4000);
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
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        climber.climb(xbox.getButton(OI.LBUMPER));

        if (xbox.getButton(OI.XBUTTON)) {
            drivetrain.shiftHigh();
        } else if (xbox.getButton(OI.YBUTTON)) {
            drivetrain.shiftLow();
        }

	    if (xbox.getButton(OI.ABUTTON)) {
	        holder.extend();
	    } else {
	        holder.retract();
	    }

        drivetrain.autoShift();

        SmartDashboard.putNumber("Left wheel (RPM)", drivetrain.getWheelRPM(drivetrain.enc_left));
        SmartDashboard.putNumber("Right wheel (RPM)", drivetrain.getWheelRPM(drivetrain.enc_right));
    }
}
