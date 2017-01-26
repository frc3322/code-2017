package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Gear {
    // Main solenoids to control linear actuator
	DoubleSolenoid gearSolenoid1;
	DoubleSolenoid gearSolenoid2;

	public Gear() {
        gearSolenoid1 = new DoubleSolenoid(0, 2);
        gearSolenoid2 = new DoubleSolenoid(4, 6);
	}

	public void extendHolder() {
        gearSolenoid1.set(DoubleSolenoid.Value.kForward);
        gearSolenoid2.set(DoubleSolenoid.Value.kForward);
    }
    public void retractHolder(){
        gearSolenoid1.set(DoubleSolenoid.Value.kReverse);
        gearSolenoid2.set(DoubleSolenoid.Value.kReverse);
	}
}
