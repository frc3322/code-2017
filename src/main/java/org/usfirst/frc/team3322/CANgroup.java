package org.usfirst.frc.team3322;
import com.ctre.CANTalon;

public class CANgroup {
    public CANTalon foo1;
    public CANTalon foo2;
    public CANTalon foo3;
    public CANgroup(int motor1, int motor2, int motor3){
        foo1 = new CANTalon(motor1);
        foo2 = new CANTalon(motor2);
        foo3 = new CANTalon(motor3);
    }
    public void setMotorSpeed(double speed){
            foo1.set(speed);
            foo2.set(speed);
            foo3.set(speed);
    }
}