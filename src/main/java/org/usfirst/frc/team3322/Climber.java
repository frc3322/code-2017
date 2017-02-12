package org.usfirst.frc.team3322;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Climber {
    CANTalon climb_talon_1, climb_talon_2;
    Encoder climbEncoder;
    static OI xbox;
    boolean climbStatus = false;
    boolean climbStartStatus = true;
    boolean currentSpike = false;
    double climbRate = 1.0; //value from 0.00 to 1.00
    double climbDistance =0;
    double totalCurrent = 0;
    double avgCurrent = 0;
    double ticksPerSecond = 44.0;
    double vibration = 0;
//  double altitude = 0;
    double[] current;
    int iterator = 0;
    int timer = 0;

    public Climber() {
        climb_talon_1 = new CANTalon(RobotMap.climbTalon_1);
        climb_talon_2 = new CANTalon(RobotMap.climbTalon_2);
        climbEncoder = new Encoder(4,5);
        xbox = new OI();
        current = new double[]{0,0,0,0,0};
    }
    public void climbManual(){
        climb_talon_1.set(1);
        climb_talon_2.set(1);
        climbVibrate();
    }
    public void stop(){
        climb_talon_1.set(0);
        climb_talon_2.set(0);
    }
    public void climb (boolean climbStatus) {
    // Climb using current spike and encoder
/*
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
*/
    // Climb using only current
/*
        climber.iterator();
        if (climbStatus && avgCurrent > 50 && avgCurrent < 100) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
        }
*/
    // Climb using altitude and encoder
/*
        altitude = Robot.navx.getAltitude();
        if (altitude > 5 & climbStartStatus) {
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
*/
    // Climb using only altitude
/*
        altitude = Robot.navx.getAltitude();
        if (climbStatus && altitude > 5 && altitude < 50) {
            climb_talon_1.set(climbRate);
            climb_talon_2.set(climbRate);
        } else {
            climb_talon_1.set(0);
            climb_talon_2.set(0);
        }
*/
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

    private void climbVibrate() {
        if (avgCurrent > 50) {
            timer ++;
            xbox.vibrate(0 + vibration,0 + vibration);
        }
        if (timer == 5) {
            vibration = vibration + 0.01;
            timer = 0;
        }
    }
}