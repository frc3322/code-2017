package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class GearCollection extends Robot {
	private DoubleSolenoid gearSolenoid; // Main solenoid to control linear actuator
	private DoubleSolenoid pitchSolenoid; // Controls pitch of collection assembly

	public void robotInit() {
		gearSolenoid = new DoubleSolenoid(0, 1);
		pitchSolenoid = new DoubleSolenoid(2, 3);
	}

	public void toggleSolenoid(DoubleSolenoid solenoid) {
		if (solenoid.get() == DoubleSolenoid.Value.kReverse) {
			solenoid.set(DoubleSolenoid.Value.kForward);
		}
	}

	public void teleopPeriodic() {

	}
}
