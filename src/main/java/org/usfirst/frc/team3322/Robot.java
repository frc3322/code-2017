package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;


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
        drivetrain = new Drivetrain(5, 3.5, 3, 50);
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
        Robot.xbox.setVibrate(0, 0);
        drivetrain.configFromDashboard();
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
        // Drivetrain
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        drivetrain.autoShift();

        // Above chassis
        climber.climb(xbox.heldDown(OI.ABUTTON));

        // Controls
        if (xbox.isToggled(OI.RBUMPER)) {
	        holder.extend();
	    } else {
	        holder.retract();
        }
    }
}
