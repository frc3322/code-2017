package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import java.io.*;
import java.util.ArrayList;

public class Robot extends IterativeRobot {
    OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    Gear gear;
    AHRS navx;
    Compressor compressor;
    Auton auton;
    ArrayList<Float> xValues = new ArrayList(500);
    ArrayList<Float> yValues = new ArrayList(500);

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
        xValues.add(navx.getDisplacementX());
        yValues.add(navx.getDisplacementY());
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
        float x = navx.getDisplacementX();
        float y = navx.getDisplacementY();


        // Record a new point for every inch moved
        if (Auton.distance(xValues.get(xValues.size()), yValues.get(yValues.size()), x, y) > .025) {
            xValues.add(x);
            yValues.add(y);
        }

        // Start recording points to file
        if (xbox.getButtonDown(OI.ABUTTON)) {
            try {
                PrintStream out = new PrintStream("AutonPath");

                for (int i = 0; i<xValues.size(); ++i) {
                    out.println(xValues.get(i) + " " + yValues.get(i));
                }
            } catch (Exception e) {
                // TODO fix me - file is a directory
                e.printStackTrace();
            }
        }
    }

    @Override
    public void autonomousPeriodic() {
        // TODO Parse the coordinate stream for reassembly
        try {
            InputStream in = new FileInputStream("AutonPath");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(xbox.getAxis(OI.L_YAXIS), xbox.getAxis(OI.R_XAXIS));
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
