package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Created by Shivam Patel on 1/19/2017.
 */
public class Climber {
    CANTalon climb_talon_1, climb_talon_2;
    Encoder climbEncoder;
    Climber climber;
    boolean climbStatus = false;
    boolean climbStartStatus = true; //set current as false (not a power surge)
    boolean currentSpike = false;
    double climbRate = 1.0; //value from 0.00 to 1.00
    double climbDistance =0;
    double totalCurrent = 0;
    double avgCurrent = 0;
    double[] current;
    int iterator = 0;

    public Climber() {
        climb_talon_1 = new CANTalon(77);
        climb_talon_2 = new CANTalon(78);
        climbEncoder = new Encoder(4,5,false);
        current = new double[]{0,0,0,0,0};
    }

    public void climb (boolean climbStatus) {
        climber.iterator();
        if (avgCurrent > 50) {
            currentSpike = true;
        } else {
            currentSpike = false;
        }
        if (currentSpike & climbStartStatus) {
            climbEncoder.reset();
            climbStartStatus = false;
        }
        if (!climbStartStatus) {
            climbDistance = climbEncoder.getDistance();
        }
        if (climbStatus && climbDistance < 100) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
        }
    }
    private void iterator() {
        current[iterator] = (climb_talon_1.getOutputCurrent() + climb_talon_2.getOutputCurrent())/2;
        if (iterator < 5){
            iterator++;
        } else {
            iterator = 0;
        }
        totalCurrent = 0;
        for (double amps : current) {
            totalCurrent += amps;
        }
        avgCurrent = totalCurrent / 5.0;
    }
}