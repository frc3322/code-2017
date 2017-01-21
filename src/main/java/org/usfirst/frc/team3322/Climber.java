package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Created by Shivam Patel on 1/19/2017.
 */
public class Climber {
    CANTalon climb_talon_1, climb_talon_2;
    Joystick joystick;
    double climbRate = 1.0; //value from 0.00 to 1.00

    public Climber() {
        climb_talon_1 = new CANTalon(77);
        climb_talon_2 = new CANTalon(78);
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