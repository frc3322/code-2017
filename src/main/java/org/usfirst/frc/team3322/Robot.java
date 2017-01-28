package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Main Robot class for team 3322's 2017 SteamWorks robot
 */
public class Robot extends IterativeRobot {
    // Define our global variables
    OI xbox;
    Drivetrain drivetrain;
    Compressor compressor;
    Climber climber;
    Gear gear;
    AHRS navx;
    Auton auton;
    ArrayList<Float> xValues = new ArrayList<>(500);
    ArrayList<Float> yValues = new ArrayList<>(500);

    @Override
    public void robotInit() {
        // Initialize required object classes
        drivetrain = new Drivetrain(3000.0, 4000.0,true, false); // TODO what RPM should these be?
        climber = new Climber();
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kUSB);
        xbox = new OI();
        gear = new Gear();
        auton = new Auton();
    }

    @Override
    public void disabledInit() {
        navx.resetDisplacement();
        xValues.add(0.0f);
        yValues.add(0.0f);
        while(!xbox.getButton(xbox.ABUTTON)){}
    }

    @Override
    public void autonomousInit() {
	    compressor.start();
    }

    @Override
    public void teleopInit() {
        compressor.start();
    }

    @Override
    public void disabledPeriodic() {
        xbox.controllerTest();
        int i = xValues.size();
        float xCurrent = navx.getDisplacementX();
        float yCurrent = navx.getDisplacementY();
        if (drivetrain.distance(xValues.get(i - 1), yValues.get(i - 1), xCurrent, yCurrent) > .025) {
            xValues.add(navx.getDisplacementX());
            yValues.add(navx.getDisplacementY());
        }
        if(xbox.getButton(xbox.ABUTTON)) {
            try{
                PrintStream printstream = new PrintStream("AutonPath");
                for(int j = 0; j<xValues.size(); ++j) {
                    printstream.println(xValues.get(j) + " " + yValues.get(j));
                }
            } catch (Exception e) {
                //fix me - file is a directory
            }
        }
    }

    @Override
    public void autonomousPeriodic() {
	    SmartDashboard.putNumber("Sonar distance", auton.sonar.getValue());
	    SmartDashboard.putBoolean("Ir state", auton.ir.get());

	    if (auton.sonar.getVoltage() > 0.1) {
	        drivetrain.drive(1, 0);
        }
    }

    @Override
    public void teleopPeriodic() {
        climber.climb(xbox.getButtonDown(OI.LBUMPER));

        if (xbox.getButton(OI.XBUTTON))
            drivetrain.shiftHigh();
        else if (xbox.getButton(OI.YBUTTON))
            drivetrain.shiftLow();

	    if (xbox.getButton(OI.ABUTTON)) {
	        gear.extendHolder();
	    } else {
	        gear.retractHolder();
	    }
    }
}
