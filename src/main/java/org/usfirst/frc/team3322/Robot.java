package org.usfirst.frc.team3322;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
    OI xbox;
    Drivetrain drivetrain;
    Climber climber;
    Gear gear;
    static AHRS navx;
    Compressor compressor;
    Auton auton;
    public static float[] xValues;
    public static float[] yValues;
    int valueNum;

    @Override
    public void robotInit() {
        // Object init
        xbox = new OI();
        drivetrain = new Drivetrain(3000.0, 4000.0,false, false); // TODO what RPM should these be?
        gear = new Gear();
        climber = new Climber();
        auton = new Auton();
        xValues = new float[2];
        yValues = new float[2];
        valueNum = 0;

        // Component init
        compressor = new Compressor(0);
        navx = new AHRS(SerialPort.Port.kUSB);
    }

    @Override
    public void disabledInit() {
        navx.resetDisplacement();
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
        if(xbox.getButton(OI.ABUTTON)) {
            xValues[valueNum] = navx.getDisplacementX();
            xValues[valueNum] = navx.getDisplacementY();
            valueNum++;
        }
        if(valueNum == 2) {

        // Start recording points to file

            /*if (xbox.getButtonDown(OI.ABUTTON)) {
                try {
                    PrintStream out = new PrintStream("AutonPath");
                    out.print();
                } catch (Exception e) {
                    // TODO fix me - file is a directory
                    e.printStackTrace();
               }*/
        }
    }

    @Override
    public void autonomousPeriodic() {
        int targetPoint = 0;
        double targetAngle;
        float x = navx.getDisplacementX();
        float y = navx.getDisplacementY();
        if(auton.distance(x, y, xValues[targetPoint], yValues[targetPoint]) < 2) {
            targetPoint++;
        }
        if(targetPoint != 2) {
            targetAngle = auton.getAngle(xValues[targetPoint], yValues[targetPoint]);
            drivetrain.driveAngle(targetAngle, 1);
        } else {
            //Add vision move here
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
