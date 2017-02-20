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
    double autonD1;
    double autonD2;
    int stringLength;
    double angleStart;
    double angleLift;

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(5, 3.5, 3, 50);
        holder = new Holder();
        climber = new Climber();
        autonState = 0;
        stringLength = 132; //inches
        angleLift = 30; //degrees

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kMXP);
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        SmartDashboard.putNumber("String Angle", 60);
    }

    @Override
    public void teleopInit() {}

    @Override
    public void robotPeriodic() {}

    @Override
    public void disabledPeriodic() {
        Robot.xbox.setLeftRightControllerVibrate(0,0);
        drivetrain.configFromDashboard();
        angleStart = SmartDashboard.getNumber("String Angle", 60);

    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
        autonState = 0;
        double ly = Math.sin(Math.toDegrees(angleStart));
        double lx = Math.cos(Math.toDegrees(angleStart));
        autonD1 = ly - lx * Math.tan(angleLift);
        autonD2 = lx/Math.cos(angleLift);
    }

    @Override
    public void autonomousPeriodic() {
        holder.extend();

        /*if(autonState == 0) { //starts 5.5 feet from left side, goes to left lift
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
        }*/
    }

    @Override
    public void teleopPeriodic() {
        // Drivetrain
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
        climber.climb(xbox.isToggled(OI.ABUTTON));

        drivetrain.autoShift();

        // Controls
        if (xbox.isToggled(OI.RBUMPER)) {
	        holder.extend();
	    } else {
            holder.retract();
        }
    }
}
