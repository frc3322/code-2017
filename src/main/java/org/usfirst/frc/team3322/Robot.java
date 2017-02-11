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
    public void disabledInit() {}

    @Override
    public void teleopInit() {}

    @Override
    public void disabledPeriodic() {
        drivetrain.setShiftPoints(
            SmartDashboard.getNumber("High gear", 0),
            SmartDashboard.getNumber("Low gear", 0));
    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
    }

    @Override
    public void autonomousPeriodic() { //starts 5.5 feet from left side, goes to left lift
        SmartDashboard.putNumber("LeftEncValue", drivetrain.getLeftEncValue());
        holder.extend();
        if(autonState == 0) {
            if(drivetrain.getLeftEncValue() < 5) {
                drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 1) {
            if(drivetrain.getLeftEncValue() < 15) {
                drivetrain.driveAngle(56, -.8);
            } else {
                autonState++;
            }
        } else if(autonState == 2) {
            //wait until end of auton
        }
    }

    @Override
    public void teleopPeriodic() {
        // getButtonDown cannot be called twice :(
        //xbox.invertInput = xbox.getButtonDown(OI.BACK);
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        climber.climb(xbox.getButton(OI.LBUMPER));

	    if (xbox.getButtonDown(OI.RBUMPER)) {
	        holder.toggle();
	    }

	    if(xbox.getButton(OI.ABUTTON)){
	        climber.climbManual();
        } else {
	        climber.stop();
        }

        drivetrain.autoShift();
        drivetrain.showRPM();
    }

}
