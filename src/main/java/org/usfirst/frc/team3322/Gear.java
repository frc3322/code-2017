package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Gear {
    // Main solenoids to control linear actuators
	DoubleSolenoid gearSolenoidLeft;
	DoubleSolenoid gearSolenoidRight;

	public Gear() {
        gearSolenoidLeft = new DoubleSolenoid(0, 2);
        gearSolenoidRight = new DoubleSolenoid(4, 6);
	}

	public void extendHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kForward);
        gearSolenoidRight.set(DoubleSolenoid.Value.kForward);
    }
    public void retractHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
        gearSolenoidRight.set(DoubleSolenoid.Value.kReverse);
	}
}
