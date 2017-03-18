package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Climber {
    CANTalon climb_talon_1, climb_talon_2;
    Encoder climbEncoder;
    boolean climbStatus = false,
            climbStartStatus = true,
            currentSpike = false,
            override = false;
    // value from 0.00 to 1.00
    double climbRate = 1.0,
            climbDistance = 0,
            totalCurrent = 0,
            avgCurrent = 0,
            vibration = 0;
    double[] current;
    int timer = 0,
            i = 0;

    public Climber() {
        climb_talon_1 = new CANTalon(RobotMap.climbTalon_1);
        climb_talon_2 = new CANTalon(RobotMap.climbTalon_2);
        current = new double[] {0,0,0,0,0};
    }

    // Climbs if the climb button is toggled, and can be force-enabled by pressing and holding down the force climb button.
    public void climb(int climbButton, int forceClimbButton) {
        boolean climb = Robot.xbox.isToggled(climbButton),
                forceClimb = Robot.xbox.heldDown(forceClimbButton);

        avgCurrent();

        if (climb) {
            // Pull holder back to prevent interference with ground
            Robot.holder.retract();

            // Climbing with current threshold
            if (climbStatus && avgCurrent < 50) {
                climb_talon_1.set(climbRate);
                climb_talon_2.set(climbRate);
            }
            else if (!override) {
                climb_talon_1.set(0);
                climb_talon_2.set(0);
            }
        }

        if (forceClimb) {
            Robot.xbox.setToggled(climbButton, false);
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
            climbStatus = true;
            override = true;
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
            climbStatus = false;
            override = false;
        }


        // Vibrate controller based on motor current
        if (climbStatus && avgCurrent > 5) {
            Robot.xbox.setVibrate(avgCurrent * .01, avgCurrent * .01);
        } else {
            Robot.xbox.setVibrate(0, 0);
        }
    }

    public void forceClimb(boolean enabled) {

    }

    private void avgCurrent() {
        i = 0;
        current[i] = (climb_talon_1.getOutputCurrent() + climb_talon_2.getOutputCurrent()) / 2;
        if (i < 4) {
            i++;
        } else {
            i = 0;
        }
        totalCurrent = 0;
        for (double amps : current) {
            totalCurrent += amps;
        }
        avgCurrent = totalCurrent / 5.0;
    }
}