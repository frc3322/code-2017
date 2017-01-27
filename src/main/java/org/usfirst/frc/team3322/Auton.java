package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;


public class Auton {
    AnalogInput sonar;
    DigitalInput ir;

    public Auton() {
        sonar = new AnalogInput(0);
        ir = new DigitalInput(0); // Digital not working for some reason
    }
}
