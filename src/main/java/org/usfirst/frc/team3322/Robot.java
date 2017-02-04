package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.List;
import java.util.ArrayList;


public class Robot extends IterativeRobot {
    OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    Gear gear, gear2;
    AHRS navx;
    Compressor compressor;
    Auton auton;
    Point2D.Float point = new Point2D.Float(0f, 0f);
    List<Point2D.Float> coords = new ArrayList<Point2D.Float>(500);

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
        coords.add(point);
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
        int i = coords.size();
        float x = navx.getDisplacementX();
        float y = navx.getDisplacementY();


        // Record a new point for every inch moved
        if (Point2D.distance(coords.get(i - 1).x, coords.get(i - 1).y, x, y) > .025) {
            coords.add(new Point2D.Float(navx.getDisplacementX(), navx.getDisplacementY()));
        }

        // Start recording points to file
        if (xbox.getButtonDown(OI.ABUTTON)) {
            try {
                PrintStream out = new PrintStream("AutonPath");

                for (Point2D.Float coord : coords) {
                    out.println(coord.x + " " + coord.y);
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
