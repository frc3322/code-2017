package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Holder {
    // Main solenoids to control linear actuators
	DoubleSolenoid holder;

	public Holder() {
        holder = new DoubleSolenoid(RobotMap.gearHolder_1, RobotMap.gearHolder_2);
	}

	public void extend() {
        holder.set(DoubleSolenoid.Value.kForward);
    }
    public void retract() {
        holder.set(DoubleSolenoid.Value.kReverse);
	}
	public void toggle() {
	    if (holder.get() == DoubleSolenoid.Value.kReverse) {
	        extend();
        } else {
	        retract();
        }
    }
}
