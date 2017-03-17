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
    int timer = 0;

    public Climber() {
        climb_talon_1 = new CANTalon(RobotMap.climbTalon_1);
        climb_talon_2 = new CANTalon(RobotMap.climbTalon_2);
        current = new double[] {0,0,0,0,0};
    }

    public void climb(boolean climbStatus) {
        // Pull holder back to prevent interference with ground
        if (climbStatus) {
            Robot.holder.retract();
        }

        avgCurrent();

        // Climb using only current
        if (climbStatus && avgCurrent < 50) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        }
        else if (!override) {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
        }

        // Vibrate controller based on motor current
        if (climbStatus == true) {
            Robot.xbox.setVibrate(avgCurrent * .6, avgCurrent * .6);
        } else {
            Robot.xbox.setVibrate(0, 0);
        }
    }

    public void forceClimb(boolean buttonPressed) {
        if (buttonPressed) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
            override = true;
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
            override = false;
        }
    }

    private void avgCurrent() {
        int i = 0;
        // Get the moving average of the current
        current[i] = (climb_talon_1.getOutputCurrent() + climb_talon_2.getOutputCurrent()) / 2;
        if (i < 4){
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