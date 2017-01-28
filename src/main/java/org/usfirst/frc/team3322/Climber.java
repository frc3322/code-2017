package org.usfirst.frc.team3322;

import com.ctre.CANTalon;


public class Climber {
    CANTalon climb_talon_1, climb_talon_2;

    double climbRate = 1.0; // value from 0.00 to 1.00

    public Climber() {
        climb_talon_1 = new CANTalon(RobotMap.climbTalon_1);
        climb_talon_2 = new CANTalon(RobotMap.climbTalon_2);
    }

    public void climb(boolean climbStatus) {
        if (climbStatus) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
        }
    }
}