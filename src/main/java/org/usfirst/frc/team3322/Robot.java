package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    static OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    static AHRS navx;
    Compressor compressor;
    Holder holder;
    int autonState;

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(
            1300,
            1600
        );
        SmartDashboard.putNumber("Low gear", 1300);
        SmartDashboard.putNumber("High gear", 1600);
        holder = new Holder();
        climber = new Climber();
        autonState = 0;

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kMXP);
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
    }

    @Override
    public void teleopInit() {}

    @Override
    public void robotPeriodic() {}

    @Override
    public void disabledPeriodic() {
        drivetrain.lowRPM = SmartDashboard.getNumber("Low gear", 0);
        drivetrain.highRPM = SmartDashboard.getNumber("High gear", 0);
        drivetrain.numSamples = (int)SmartDashboard.getNumber("Num samples", 0);
        drivetrain.shiftThreshold = (int)SmartDashboard.getNumber("Shift threshold", 0);
    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
        autonState = 0;
    }

    @Override
    public void autonomousPeriodic() {
        holder.extend(); //starts 5.5 feet from left side, goes to left lift
        if(autonState == 0) {
            if(drivetrain.getLeftEncValue() < 5) {
                drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 1) {
            if(drivetrain.getLeftEncValue() < 15) {
                drivetrain.driveAngle(59, -.8);
            } else {
                autonState++;
            }
        } else if(autonState == 2) {
            //wait until end of auton
        }
        /*holder.extend(); //starts 5.5 feet from right side, goes to right lift
        if(autonState == 0) {
            if(drivetrain.getRightEncValue() < 5) {
                drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 1) {
            if(drivetrain.getRightEncValue() < 15) {
                drivetrain.driveAngle(-59, -.8);
            } else {
                autonState++;
            }
        } else if(autonState == 2) {
            //wait until end of auton
        }*/
        /*holder.extend(); //starts directly in front of center lift, goes to center lift
        if(autonState == 0) {
            if(drivetrain.getRightEncValue() < 10) {
                drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 1) {
            //wait until end of auton
        }*/
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        climber.climb(xbox.heldDown(OI.ABUTTON));

        if(xbox.heldDown(OI.XBUTTON)){
            climber.climb_talon_1.set(-1);
            climber.climb_talon_2.set(-1);
        }
        else if (xbox.heldDown(OI.YBUTTON)){
            climber.climb_talon_1.set(0);
            climber.climb_talon_2.set(0);
        }

        if (xbox.isToggled(OI.RBUMPER)) {
	        holder.extend();
	    } else {
	        holder.retract();
        }
/*
	    if(xbox.heldDown(OI.ABUTTON)){
	        climber.climbManual();
        } else {
	        climber.stop();
        }
*/
        drivetrain.autoShift();
    }
}
