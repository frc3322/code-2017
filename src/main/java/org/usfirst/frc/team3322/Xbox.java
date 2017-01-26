package org.usfirst.frc.team3322;

/**
 * This class can be used to allocate controller buttons to methods. Hopefully we can use this to unify us all!
 *
 *
 */

import edu.wpi.first.wpilibj.Joystick;

public class Xbox extends Robot {
	// Define joystick
	Joystick xbox = new Joystick(1);

	// Assign button variables to reasonable names
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

    // Returns true if button is held down during call
    public boolean getButton(int button) {
		 return xbox.getRawButton(button);
	}

    // Returns true when button is initially pressed
    public boolean getButtonDown(int button) {
        if (!buttonDown && xbox.getRawButton(button)) {
            buttonDown = true;
            return true;
        } else
	    return false;
	}
}
