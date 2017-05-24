package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.concurrent.*;

public class Robot extends IterativeRobot {
    static OI xbox;
    static Drivetrain drivetrain;
    static Climber climber;
    static Holder holder;
    static AHRS navx;
    static Auton auton;
    static Compressor compressor;
    static I2C LEDs = new I2C(I2C.Port.kOnboard, 4);
    public double turn;
    public double throttle;
    boolean drivingStraight = false;
    int startPos;
    ScheduledExecutorService teleopDriveExecuter;
    DigitalInput gearSensor;
    double xLength,
        yLength,
        driveStraightAngle,
        previousThrottle = 0,
        previousTurn = 0,
        maxTurnDelta = .05,
        maxThrottleDelta = .05,
        turnValue,
        throttleValue,
        currentTurn,
        currentThrottle;

    @Override
    public void robotInit() {
        // Object init
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
        SmartDashboard.putString("position_key", "1,4 = left | 2,5 = mid | 3,6 = right");
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
        teleopDriveExecuter = Executors.newScheduledThreadPool(1);
        teleopDriveExecuter.scheduleAtFixedRate(()->{
            teleopPeriodicFast();
        },0,5, TimeUnit.MILLISECONDS);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("robot_speed", drivetrain.robotSpeed);
    }

    @Override
    public void disabledPeriodic() {
        startPos = (int)SmartDashboard.getNumber("start_pos", 0);
        xLength = SmartDashboard.getNumber("x_length", 100); //100x, 100y if starting on boiler
        yLength = SmartDashboard.getNumber("y_length", 132); //84x, 100y if starting next to return loading station

        SmartDashboard.putBoolean("auton_ready", startPos != 0);
        SmartDashboard.putNumber("start_pos_in_code", startPos);
        OI.LEDWrite("DisabledPeriodic");
    }
    @Override
    public void autonomousInit() {
        holder.retract();
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();

        auton.init(xLength, yLength);

        SmartDashboard.putNumber("start_pos_in_code", startPos);
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
        SmartDashboard.putNumber("start_pos_in_code", startPos);
        OI.LEDWrite("AutonPeriodic");
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.direction(true);
        //drivetrain.drive(throttleValue, turnValue);
        drivetrain.autoShift();

        //clamp();
        /*
        if (Math.abs(xbox.getAxis(OI.R_XAXIS)) < .05) { //compare directly to stick, not clamped value
        if (!drivingStraight) {
            drivingStraight = true;
            driveStraightAngle = navx.getYaw();
        }
            SmartDashboard.putNumber("Turn Angle",driveStraightAngle);
            drivetrain.driveAngle(driveStraightAngle, throttleValue);
            drivetrain.closedLoopDrive(throttleValue,turnValue);
        }
          else {
            drivetrain.drive(throttleValue,turnValue);
        */
        //    drivetrain.closedLoopDrive(xbox.getAxis(OI.L_YAXIS),xbox.getAxis(OI.R_XAXIS));
            drivingStraight = false;
//      }
        //drivetrain.closedLoopDrive(throttleValue,turnValue);
//      }

        climber.climb(OI.ABUTTON, OI.LBUMPER);

        if (xbox.isToggled(OI.RBUMPER)) {
            holder.extend();
        } else {
            holder.retract();
        }

        if (gearSensor.get()) {
            SmartDashboard.putBoolean("gear_sensor", true);
            //xbox.setVibrate(.5, .5);
        } else {
            SmartDashboard.putBoolean("gear_sensor", false);
            //xbox.setVibrate(0, 0);
        }

        if (climber.climbStatus == Climber.ClimbState.CLIMB) {
            OI.LEDWrite("Climbing");
        } else if (holder.extended) {
            OI.LEDWrite("HolderForward");
        } else {
            OI.LEDWrite("HolderBack");
        }

        SmartDashboard.putNumber("teleop", 0);
        SmartDashboard.putNumber("auton", 0);
        SmartDashboard.putNumber("yaw rate", navx.getRate());
        SmartDashboard.putNumber("turn",xbox.getAxis(OI.R_XAXIS));
        SmartDashboard.putNumber("left_enc", drivetrain.getLeftEncValue());
        SmartDashboard.putNumber("right_enc", drivetrain.getRightEncValue());
        System.out.println(navx.getYaw());
    }
    
    private void teleopPeriodicFast(){
        drivetrain.closedLoopDrive(Robot.xbox.getAxis(OI.L_YAXIS),Robot.xbox.getAxis(OI.R_XAXIS));
    }
    
    private void clamp(){
        currentThrottle = xbox.getFineAxis(OI.L_YAXIS, 3);
        currentTurn = xbox.getFineAxis(OI.R_XAXIS, 3);

        double deltaTurn = currentTurn - previousTurn;
        double deltaThrottle = currentThrottle - previousThrottle;

        if(Math.abs(deltaTurn) > maxTurnDelta && (previousTurn / deltaTurn) > 0){
            turnValue = previousTurn + ((deltaTurn < 0)? -maxTurnDelta : maxTurnDelta);
        } else {
            turnValue = currentTurn;
        }

        if (Math.abs(deltaThrottle) > maxThrottleDelta && (previousThrottle / deltaThrottle) > 0) {
            throttleValue = previousThrottle + ((deltaThrottle < 0)? -maxThrottleDelta : maxThrottleDelta);
        } else {
            throttleValue = currentThrottle;
        }

        previousThrottle = throttleValue;
        previousTurn = turnValue;

        SmartDashboard.putNumber("turn_value", turnValue);
        SmartDashboard.putNumber("joystick", currentTurn);
    }
}
