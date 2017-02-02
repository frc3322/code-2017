package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


public class Robot extends IterativeRobot {
    OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    Dashboard dashboard;
    Joystick joystick;
    static AHRS navx;
    Compressor compressor;
    Auton auton;
    Gear gear;
    //ArrayList<> coords = new ArrayList<>(500);

    @Override
    public void robotInit() {
	      // Initialize required object classes
        xbox = new OI();
        drivetrain = new Drivetrain(3000.0, 4000.0,false, false); // TODO what RPM should these be?
        // Init our compressor as PCM number 1
        compressor = new Compressor(0);
        climber = new Climber();
        dashboard = new Dashboard();
        joystick = new Joystick(1);
        auton = new Auton();
        gear = new Gear();
        // Init NavX gyroscope
        navx = new AHRS(SerialPort.Port.kUSB);
    }

    @Override
    public void disabledInit() {
        navx.resetDisplacement();
        //coords.add(new Point2D.Float(0.0f, 0.0f));
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
        //int i = coords.size();
        float x = navx.getDisplacementX();
        float y = navx.getDisplacementY();

        /*
        // Record a new point for every inch moved
        if (Point2D.distance(coords.get(i - 1).x, coords.get(i - 1).y, x, y) > .025) {
            coords.add(new Point2D.Float(navx.getDisplacementX(), navx.getDisplacementY()));
        }*/

        // Start recording points to file
        if (xbox.getButtonDown(OI.ABUTTON)) {
            try {
                PrintStream out = new PrintStream("AutonPath");

                /*for (Point2D.Float coord : coords) {
                    out.println(coord.x + " " + coord.y);
                }*/
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
