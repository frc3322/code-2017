package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
    static OI xbox;
    static Drivetrain drivetrain;
    static Climber climber;
    static Holder holder;
    static AHRS navx;
    static Auton auton;
    static Compressor compressor;
    static I2C LEDs = new I2C(I2C.Port.kOnboard, 4);

    boolean drivingStraight = false;
    int startPos;
    DigitalInput gearSensor;
    double xLength,
        yLength,
        driveStraightAngle;

    @Override
    public void robotInit() {
        // Subsystem init
        xbox = new OI();
        drivetrain = new Drivetrain(6.75, 8.5, 3, 50);
        holder = new Holder();
        climber = new Climber();
        auton = new Auton();

        // Component init
        compressor = new Compressor(0);
        gearSensor = new DigitalInput(4);
        navx = new AHRS(SerialPort.Port.kMXP);
        SmartDashboard.putNumber("auton", 1);
        SmartDashboard.putNumber("start_pos", 0);
        OI.LEDWrite("RobotInit");
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        Robot.xbox.setVibrate(0, 0);

        SmartDashboard.putNumber("x_length", 100);
        SmartDashboard.putNumber("y_length", 132);
        SmartDashboard.putString("position_key", "1 = left | 2= mid | 3 = right");
        SmartDashboard.putNumber("auton", 1);
        SmartDashboard.putBoolean("enabled", false);
        OI.LEDWrite("DisabledInit");
    }

    @Override
    public void teleopInit() {
        SmartDashboard.putNumber("teleop", 0);
        SmartDashboard.putNumber("auton", 0);
        SmartDashboard.putBoolean("enabled", true);
        OI.LEDWrite("TeleopInit");
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("robot_speed", drivetrain.getRobotSpeed());
    }

    @Override
    public void disabledPeriodic() {
        startPos = (int)SmartDashboard.getNumber("start_pos", 0);
        xLength = SmartDashboard.getNumber("x_length", 100); //100x, 100y if starting on boiler
        yLength = SmartDashboard.getNumber("y_length", 132); //84x, 100y if starting next to return loading station
        System.out.println("Nav X" + navx.getYaw());
        SmartDashboard.putBoolean("auton_ready", startPos != 0);
        OI.LEDWrite("DisabledPeriodic");
    }
    @Override
    public void autonomousInit() {
        holder.retract();
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();

        auton.init(xLength, yLength);

        SmartDashboard.putNumber("auton", 2);
        SmartDashboard.putBoolean("enabled", true);
        OI.LEDWrite("AutonInit");
    }

    @Override
    public void autonomousPeriodic() {
        if (startPos == 1 || startPos == 4) {
            auton.leftPos();
        } else if (startPos == 2 || startPos == 5) {
            auton.middlePos();
        } else if (startPos == 3 || startPos == 6) {
            auton.rightPos();
        } else {
            auton.leftPos();
        }

        SmartDashboard.putNumber("auton", 1);
        OI.LEDWrite("AutonPeriodic");
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.direction(true);
        drivetrain.driveClamped(OI.L_XAXIS, OI.R_YAXIS);

        climber.climb(OI.ABUTTON, OI.LBUMPER);

        if (xbox.isToggled(OI.RBUMPER)) {
            holder.extend();
        } else {
            holder.retract();
        }

        if (climber.climbStatus == Climber.ClimbState.CLIMB) {
            OI.LEDWrite("Climbing");
        } else if (holder.extended) {
            OI.LEDWrite("HolderForward");
        } else {
            OI.LEDWrite("HolderBack");
        }


        // Vibrate controller based on motor current and sensor state
        if (climber.climbStatus != Climber.ClimbState.STOP) {
            Robot.xbox.setVibrate(climber.avgCurrent * .02, climber.avgCurrent * .02);
        } else if (!gearSensor.get()) {
            SmartDashboard.putBoolean("gear_sensor", true);
            xbox.setVibrate(0.5,0.5);
        } else {
            SmartDashboard.putBoolean("gear_sensor", false);
            xbox.setVibrate(0, 0);
        }

        SmartDashboard.putNumber("teleop", 0);
        SmartDashboard.putNumber("auton", 0);
        SmartDashboard.putNumber("yaw", navx.getYaw());
        SmartDashboard.putNumber("left_enc", drivetrain.getLeftEncValue());
        SmartDashboard.putNumber("right_enc", drivetrain.getRightEncValue());
        SmartDashboard.putNumber("vel_x",navx.getVelocityX());
        SmartDashboard.putNumber("vel_y",navx.getVelocityY());
        SmartDashboard.putNumber("vel_z",navx.getVelocityZ());
    }
}