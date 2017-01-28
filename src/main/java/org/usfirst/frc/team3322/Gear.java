package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Gear {
    // Main solenoids to control linear actuators
	DoubleSolenoid gearSolenoidLeft;
	DoubleSolenoid gearSolenoidRight;

	public Gear() {
        gearSolenoidLeft = new DoubleSolenoid(RobotMap.gearLeft_1, RobotMap.gearLeft_2);
        gearSolenoidRight = new DoubleSolenoid(RobotMap.gearRight_1, RobotMap.gearRight_2);
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
