package org.usfirst.frc.team3322;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static final int
            // Controller
            driverPort = 0,
            techPort = 1,

            // Talons
            driveLeft_1 = 20,
            driveLeft_2 = 21,
            driveRight_1 = 23,
            driveRight_2 = 22,

            climbTalon_1 = 5,
            climbTalon_2 = 6,

            // Solenoids
            shifter_1 = 0,
            shifter_2 = 1,

            gearLeft_1 = 3,
            gearLeft_2 = 4,
            gearRight_1 = 5,
            gearRight_2 = 6;

}
