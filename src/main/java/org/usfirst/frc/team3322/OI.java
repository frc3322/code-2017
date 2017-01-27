package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.Joystick;

/*
  This class can be used to allocate controller buttons to methods.
 */
public class OI extends Robot {

    Joystick xbox;

    public OI(int port) {
        xbox = new Joystick(port);
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
		 return xbox.getRawButton(button);
	}

    // Returns true while button is held
    public boolean getButtonDown(int button) {
        if (!buttonDown && xbox.getRawButton(button)) {
            buttonDown = true;
            return true;
        } else
	    return false;
	}
}
