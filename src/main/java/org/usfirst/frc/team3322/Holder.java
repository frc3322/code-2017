package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Holder {
    // Main solenoids to control linear actuators
	DoubleSolenoid holder;

	public Holder() {
        holder = new DoubleSolenoid(RobotMap.gearHolder_1, RobotMap.gearHolder_2);
	}

	public void extend() {
        holder.set(DoubleSolenoid.Value.kForward);
        SmartDashboard.putBoolean("holder", true);
    }
    public void retract() {
        holder.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putBoolean("holder", false);
	}
}
