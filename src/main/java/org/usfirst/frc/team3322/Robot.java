package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    static OI xbox;
    static Drivetrain drivetrain;
    Climber climber;
    static AHRS navx;
    Compressor compressor;
    Holder holder;
    Auton auton;
    int startPos;
    double smallX;

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(8.75, 12.5, 3, 50);
        holder = new Holder();
        climber = new Climber();
        auton = new Auton();
        startPos = 0;

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kMXP);
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        SmartDashboard.putNumber("smallX", 5.875);
        SmartDashboard.putNumber("StartPos", 0);
        SmartDashboard.putString("PositionKey", "L to R, B in 1-3, R in 4-6");
    }

    @Override
    public void teleopInit() {}

    @Override
    public void robotPeriodic() {}

    @Override
    public void disabledPeriodic() {
        Robot.xbox.setVibrate(0, 0);
        drivetrain.configFromDashboard();
        smallX = SmartDashboard.getNumber("smallX", 5.875);
        startPos = (int) SmartDashboard.getNumber("StartPos", 0);
        SmartDashboard.putBoolean("AutonReady", startPos != 0);
        SmartDashboard.putNumber("LeftEnc", drivetrain.getLeftEncValue());
    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
        auton.initVars(smallX);
    }

    @Override
    public void autonomousPeriodic() {
        holder.extend();
        if(startPos == 1 || startPos == 4) {
            auton.leftPos();
        } else if (startPos == 2 || startPos == 5) {
            auton.middlePos();
        } else if (startPos == 3 || startPos == 6) {
            auton.rightPos();
        }
    }

    @Override
    public void teleopPeriodic() {
        // Drivetrain
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        drivetrain.autoShift();

        // Controls
        climber.climb(xbox.isToggled(OI.ABUTTON));

        if (xbox.isToggled(OI.RBUMPER)) {
	        holder.extend();
	    } else {
	        holder.retract();
        }
    }
}