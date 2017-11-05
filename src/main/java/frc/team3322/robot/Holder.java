package frc.team3322.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Holder {
	private DoubleSolenoid holder;
	DigitalInput gearSensor;

	boolean extended;

	public Holder() {
		holder = new DoubleSolenoid(RobotMap.gearHolder_1, RobotMap.gearHolder_2);
		gearSensor = new DigitalInput(RobotMap.gearSensor);
	}

	public void extend() {
        extended = true;
        holder.set(DoubleSolenoid.Value.kForward);
        Robot.xbox.setToggled(OI.RBUMPER, true);

        SmartDashboard.putBoolean("holder", true);
    }

    public void retract() {
        extended = false;
        holder.set(DoubleSolenoid.Value.kReverse);
        Robot.xbox.setToggled(OI.RBUMPER, false);

        SmartDashboard.putBoolean("holder", false);
    }
}
