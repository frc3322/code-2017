package org.usfirst.frc.team3322;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
public class Gear {
	DoubleSolenoid gearSolenoidLeft;
	//DoubleSolenoid gearSolenoidRight;
    AnalogInput gearReedSwitch;
    SmartDashboard SmartDash;
    Compressor compressor;
	public Gear() {
        gearSolenoidLeft = new DoubleSolenoid(RobotMap.gearLeft_1, RobotMap.gearLeft_2);
        //gearSolenoidRight = new DoubleSolenoid(RobotMap.gearRight_1, RobotMap.gearRight_2);
        gearReedSwitch = new AnalogInput(3);
        compressor = new Compressor(0);
        compressor.start();
	}
	public void extendHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kForward);
        SmartDashboard.putNumber("Reed Switch Num", gearReedSwitch.getValue());
        //gearSolenoidRight.set(yDoubleSolenoid.Value.kForward);
    }
    public void retractHolder() {
        gearSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
        SmartDashboard.putNumber("Reed Switch Num", gearReedSwitch.getValue());
       //gearSolenoidRight.set(DoubleSolenoid.Value.kReverse);
	}
}
