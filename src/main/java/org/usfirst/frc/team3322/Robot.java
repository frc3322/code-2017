package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintStream;
import java.util.ArrayList;


public class Robot extends IterativeRobot {
    OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    Gear gear;
    AHRS navx;
    Compressor compressor;
    Auton auton;
    ArrayList<Float> xValues = new ArrayList<>(500);
    ArrayList<Float> yValues = new ArrayList<>(500);

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(3000.0, 4000.0,false, false); // TODO what RPM should these be?
        gear = new Gear();
        climber = new Climber();
        auton = new Auton();

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kUSB);
    }

    @Override
    public void disabledInit() {
        navx.resetDisplacement();
        xValues.add(0.0f);
        yValues.add(0.0f);
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
        int i = xValues.size();
        float xCurrent = navx.getDisplacementX();
        float yCurrent = navx.getDisplacementY();

        if (drivetrain.distance(xValues.get(i - 1), yValues.get(i - 1), xCurrent, yCurrent) > .025) {
            xValues.add(navx.getDisplacementX());
            yValues.add(navx.getDisplacementY());
        }

        if (xbox.getButton(OI.ABUTTON)) {
            try{
                PrintStream printstream = new PrintStream("AutonPath");
                for (int j = 0; j < xValues.size(); ++j) {
                    printstream.println(xValues.get(j) + " " + yValues.get(j));
                }
            } catch (Exception e) {
                //fix me - file is a directory
            }
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(xbox.getAxis(1), xbox.getAxis(4));
        climber.climb(xbox.getButton(OI.LBUMPER));

        if (xbox.getButton(OI.XBUTTON)) {
            drivetrain.shiftHigh();
        } else if (xbox.getButton(OI.YBUTTON)) {
            drivetrain.shiftLow();
        }

	    if (xbox.getButton(OI.ABUTTON)) {
	        gear.extendHolder();
	    } else {
	        gear.retractHolder();
	    }
    }
}
