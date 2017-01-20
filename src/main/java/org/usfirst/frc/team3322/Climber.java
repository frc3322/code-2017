package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Created by Shivam Patel on 1/19/2017.
 */
public class Climber {
    CANTalon Climb_talon_1, Climb_talon_2;
    Joystick joystick;
    double climbRate = 1.0;

    void init() {
        Climb_talon_1 = new CANTalon(77);
        Climb_talon_2 = new CANTalon(78);
    }

    public void climb(boolean climbStatus) {
        if (climbStatus) {
            Climb_talon_1.set(climbRate);
            Climb_talon_2.set(climbRate);
        } else {
            Climb_talon_1.set(0);
            Climb_talon_2.set(0);
        }
    }
}