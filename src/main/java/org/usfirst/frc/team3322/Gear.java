package org.usfirst.frc.team3322;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
public class Gear {
    // Main solenoids to control linear actuators
	DoubleSolenoid gearSolenoidLeft;
	//DoubleSolenoid gearSolenoidRight;
    AnalogInput gearReedSwitch;
    SmartDashboard SmartDshbrd;
	public Gear() {
        gearSolenoidLeft = new DoubleSolenoid(RobotMap.gearLeft_1, RobotMap.gearLeft_2);
        //gearSolenoidRight = new DoubleSolenoid(RobotMap.gearRight_1, RobotMap.gearRight_2);
        gearReedSwitch = new AnalogInput(3);
	}
	public void extendHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kForward);
        gearReedSwitch.getValue();
        SmartDashboard.putNumber("Reed Switch Num", gearReedSwitch.getValue());
        //gearSolenoidRight.set(yDoubleSolenoid.Value.kForward);
    }
    public void retractHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putNumber("Reed Switch Num", gearReedSwitch.getValue());
       //gearSolenoidRight.set(DoubleSolenoid.Value.kReverse);
	}
}
