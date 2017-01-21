package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Gear {
	DoubleSolenoid gearSolenoid; // Main solenoid to control linear actuator
	DoubleSolenoid pitchSolenoid; // Controls pitch of collection assembly

	public Gear() {
		gearSolenoid = new DoubleSolenoid(0, 1);
		pitchSolenoid = new DoubleSolenoid(2, 3);
	}

	void toggleSolenoid(DoubleSolenoid solenoid) {
		if (solenoid.get() == DoubleSolenoid.Value.kReverse) {
			solenoid.set(DoubleSolenoid.Value.kForward);
		} else {
			solenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public void extendHolder() {
	    toggleSolenoid(gearSolenoid);
    }
}
