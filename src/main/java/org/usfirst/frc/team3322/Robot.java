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
    double xLength;
    double yLength;
    double previousThrottle = 0;
    double previousTurn = 0;
    double maxTurnDelta = .05;
    double maxThrottleDelta = .05;
    double turnValue;
    double throttleValue;
    double currentTurn;
    double currentThrottle;

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
    }

    @Override
    public void disabledInit() {
        drivetrain.shiftLow();
        SmartDashboard.putNumber("x_length", 100);
        SmartDashboard.putNumber("y_length", 132);
        SmartDashboard.putNumber("start_pos", 0);
        SmartDashboard.putString("position_key", "L to R, B in 1-3, R in 4-6");
        SmartDashboard.putNumber("auton",0);
    }

    @Override
    public void teleopInit() {
        SmartDashboard.putNumber("auton",0);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("robot_speed", drivetrain.robotSpeed);
    }

    @Override
    public void disabledPeriodic() {
        Robot.xbox.setVibrate(0,0);
        drivetrain.configFromDashboard();
        startPos = (int) SmartDashboard.getNumber("start_pos", 0);
        xLength = SmartDashboard.getNumber("x_length", 100); //100x, 100y if starting on boiler
        yLength = SmartDashboard.getNumber("y_length", 132); //84x, 100y if starting next to return loading station
        SmartDashboard.putBoolean("auton_ready", startPos != 0);
    }
    @Override
    public void autonomousInit() {
        navx.reset();
        drivetrain.resetEncs();
        compressor.start();
        auton.initVars(xLength, yLength);
        SmartDashboard.putNumber("auton",1);
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("auton",1);
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
        SmartDashboard.putNumber("auton",0);
        // Drivetrain
        drivetrain.direction(xbox.isToggled(OI.LBUMPER));
        clamp();
        drivetrain.drive(throttleValue, turnValue);
        //TODO Quadratic rotating
        drivetrain.autoShift();

        // Controls
        climber.climb(xbox.isToggled(OI.LBUMPER));
        climber.climbManually(xbox.heldDown(OI.ABUTTON));

        if (xbox.isToggled(OI.RBUMPER)) {
            holder.extend();
        } else {
            holder.retract();
        }
    }
    private void clamp(){
        currentThrottle = xbox.getAxis(OI.L_YAXIS);
        currentTurn = xbox.getAxis(OI.R_XAXIS);

        double deltaTurn = currentTurn - previousTurn;
        double deltaThrottle = currentThrottle - previousThrottle;
        //TODO put Andriy's function here
        if(Math.abs(deltaTurn) > maxTurnDelta && (previousTurn / deltaTurn) > 0){
            turnValue = previousTurn + ((deltaTurn < 0)? -maxTurnDelta : maxTurnDelta);
        }
        else{
            turnValue = currentTurn;
        }
        if(Math.abs(deltaThrottle) > maxThrottleDelta && (previousThrottle / deltaThrottle) > 0){
            throttleValue = previousThrottle + ((deltaThrottle < 0)? -maxThrottleDelta : maxThrottleDelta);
        }
        else{
            throttleValue = currentThrottle;
        }
        previousThrottle = throttleValue;
        previousTurn = turnValue;
        SmartDashboard.putNumber("Turn Value",turnValue);
        SmartDashboard.putNumber("Joystick", currentTurn);
    }
}