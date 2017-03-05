package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.nio.charset.StandardCharsets;


public class Robot extends IterativeRobot {
    static OI xbox;
    static Drivetrain drivetrain;
    static Climber climber;
    static AHRS navx;
    static Holder holder;
    static Auton auton;
    static Compressor compressor;
    static I2C LEDs = new I2C(I2C.Port.kOnboard, 4);
    byte[] WriteData;
    int startPos;
    boolean holderForward;
    boolean climbing;
    boolean drivingStraight = false;
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
        drivetrain = new Drivetrain(8.75, 12.5, 3, 50);
        holder = new Holder();
        climber = new Climber();
        auton = new Auton();
        startPos = 0;

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kMXP);
        SmartDashboard.putNumber("auton",0);
        LEDWrite("RobotInit");
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        SmartDashboard.putNumber("x_length", 100);
        SmartDashboard.putNumber("y_length", 132);
        SmartDashboard.putNumber("start_pos", 0);
        SmartDashboard.putString("position_key", "L to R, B in 1-3, R in 4-6");
        SmartDashboard.putNumber("auton",0);
        LEDWrite("DisabledInit");
        SmartDashboard.putBoolean("enabled",false);
    }

    @Override
    public void teleopInit() {
        SmartDashboard.putNumber("auton",0);
        LEDWrite("TeleopInit");
        SmartDashboard.putBoolean("enabled",true);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("robot_speed", drivetrain.robotSpeed);
    }

    @Override
    public void disabledPeriodic() {
        Robot.xbox.setVibrate(0, 0);
        startPos = (int) SmartDashboard.getNumber("start_pos", 0);
        xLength = SmartDashboard.getNumber("x_length", 100); //100x, 100y if starting on boiler
        yLength = SmartDashboard.getNumber("y_length", 132); //84x, 100y if starting next to return loading station
        SmartDashboard.putBoolean("auton_ready", startPos != 0);
        LEDWrite("DisabledPeriodic");
        SmartDashboard.putBoolean("enabled",false);
    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
        auton.initVars(xLength, yLength);
        SmartDashboard.putNumber("auton", 1);
        LEDWrite("AutonInit");
        SmartDashboard.putBoolean("enabled",true);
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("auton",1);
        if(startPos == 1 || startPos == 4) {
            auton.leftPos();
        } else if (startPos == 2 || startPos == 5) {
            auton.middlePos();
        } else if (startPos == 3 || startPos == 6) {
            auton.rightPos();
        }
        LEDWrite("AutonPeriodic");
        SmartDashboard.putBoolean("enabled",true);
    }

    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("auton", 0);
        SmartDashboard.putNumber("yaw",navx.getYaw());
        System.out.println(navx.getYaw());
        SmartDashboard.putBoolean("enabled",true);
        // Drivetrain
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
//        drivetrain.drive(throttleValue, turnValue);
        drivetrain.autoShift();
        clamp();
//        if (Math.abs(xbox.getAxis(OI.R_XAXIS)) < .05) { //compare directly to stick, not clamped value
//            if(!drivingStraight) {
//                drivingStraight = true;
//                driveStraightAngle = navx.getYaw();
//            }
//            SmartDashboard.putNumber("Turn Angle",driveStraightAngle);
////            drivetrain.driveAngle(driveStraightAngle, throttleValue);
//             drivetrain.closedLoopDrive(throttleValue,turnValue);
//        }
//        else {
//            drivetrain.drive(throttleValue,turnValue);

            drivetrain.closedLoopDrive(throttleValue,turnValue);
            drivingStraight = false;
//        }
       // drivetrain.closedLoopDrive(throttleValue,turnValue);
        SmartDashboard.putNumber("Yaw Rate",navx.getRate());
//        }

        // Controls
        climber.climb(xbox.isToggled(OI.LBUMPER));
        climber.climbManually(xbox.heldDown(OI.ABUTTON));

        if (xbox.isToggled(OI.RBUMPER)) {
            holder.extend();
            holderForward = true;
        } else {
            holder.retract();
            holderForward = false;
        }
        if(xbox.isToggled(OI.LBUMPER) || xbox.heldDown(OI.ABUTTON)){
            LEDWrite("Climbing");
        }
        else if(holderForward) {
            LEDWrite("HolderForward");
        }
        else{
            LEDWrite("HolderBack");
        }

    }
    private void clamp(){
        currentThrottle = xbox.getFineAxis(OI.L_YAXIS, 2);
        currentTurn = xbox.getFineAxis(OI.R_XAXIS, 2);

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
        SmartDashboard.putNumber("turn_value",turnValue);
        SmartDashboard.putNumber("joystick", currentTurn);
    }
    private void LEDWrite(String data){
        WriteData = data.getBytes(StandardCharsets.UTF_8);
        LEDs.transaction(WriteData, WriteData.length, null, 0);
    }
}