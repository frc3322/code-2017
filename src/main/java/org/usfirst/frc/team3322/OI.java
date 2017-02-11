package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.Joystick;


public class OI {
    static Joystick joystick;

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

    boolean buttonDown = false;
    public boolean invertInput = false;

    public OI() {
        joystick = new Joystick(0);
    }

    public boolean getButton(int button) {
        return joystick.getRawButton(button);
	}

	// Returns true only once, and will not return true again until the button is released and pressed again.
	public boolean getButtonDown(int button) {
        if (getButton(button)) {
            if (!buttonDown) {
                buttonDown = true;
                return true;
            } else {
                return false;
            }
        } else {
            buttonDown = false;
            return false;
        }
    }

	public double getAxis(int axis) {
            return joystick.getRawAxis(axis);
    }
}
