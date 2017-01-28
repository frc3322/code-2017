package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.Joystick;


public class OI {
    Joystick driver, tech;

    public OI() {
        driver = new Joystick(RobotMap.driverPort);
        tech = new Joystick(RobotMap.techPort);
    }

	// Assign button values to variables
	public static final int ABUTTON = 1,
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
		DPADHORIZ = 6;

    boolean buttonDown = false;

    // Returns true when button is initially tapped
    public boolean getButton(int button) {
        if (!buttonDown && driver.getRawButton(button)) {
            buttonDown = true;
            return true;
        } else {
			return false;
		}
	}

    // Returns true while button is held
    public boolean getButtonDown(int button) {
        return driver.getRawButton(button);
    }

    // Returns true when button is initially tapped
    public boolean getTechButton(int button) {
        if (!buttonDown && tech.getRawButton(button)) {
            buttonDown = true;
            return true;
        } else {
            return false;
        }
    }

    // Returns true while button is held
    public boolean getTechButtonDown(int button) {
        return tech.getRawButton(button);
    }

    public void controllerTest() {
        // not implemented
    }
}
