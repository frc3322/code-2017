package org.usfirst.frc.team3322;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static final int
        // Talons
        driveLeft_1 = 20,
        driveLeft_2 = 21,
        driveLeft_3 = 19,
        driveRight_1 = 23,
        driveRight_2 = 22,
        driveRight_3 = 24,

        climbTalon_1 = 40,
        climbTalon_2 = 41,

        // Solenoids
        shifter_1 = 0,
        shifter_2 = 1,

        gearHolder_1 = 3,
        gearHolder_2 = 4,

        // Sensors
        encLeft_1 = 0,
        encLeft_2 = 1,
        encRight_1 = 2,
        encRight_2 = 3,

        encClimb_1 = 4,
        encClimb_2 = 5;
}
