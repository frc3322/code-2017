package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.Joystick;


public class OI {
    Joystick driver, tech;

    // Assign vague integers to variables
    public static final int
            // Buttons
            ABUTTON = 1,
            BBUTTON = 2,
            XBUTTON = 3,
            YBUTTON = 4,
            LBUMPER = 5,
            RBUMPER = 6,
            BACK = 7,
            START = 8,
            LSTICK = 9,
            RSTICK = 10,
            DPADVERT = 7, // not reliable
            DPADHORIZ = 6,

            // Stick axes
            L_YAXIS = 1,
            L_XAXIS = 2,
            R_YAXIS = 3,
            R_XAXIS = 4;

    public OI() {
        driver = new Joystick(RobotMap.driverPort);
        tech = new Joystick(RobotMap.techPort);
    }

    public boolean getButton(int button) {
        return driver.getRawButton(button);
	}

	public double getAxis(int axis) {
        return driver.getRawAxis(axis);
    }
}
