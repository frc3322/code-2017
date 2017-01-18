package org.usfirst.frc.team3322;

/**
 * This class can be used to allocate controller buttons to methods. Hopefully we can use this to unify us all!
 *
 *
 */

import edu.wpi.first.wpilibj.Joystick;

public class Xbox {
	// Define joystick
	Joystick driver = new Joystick(1),
			tech = new Joystick(2);

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

	// TODO: Implement button state methods
	// Returns true when button is initially pressed
	public boolean getButton() {
		return false;
	}

	// Returns true if button is held down during call
	public boolean getButtonDown() {
		return false;
	}
}
