package org.usfirst.frc.team3322;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static final int
            // Controllers
            driverPort = 0,
            techPort = 1,

            // Talons
            driveLeft_1 = 0,
            driveLeft_2 = 1,
            driveLeft_3 = 2,

            driveRight_1 = 3,
            driveRight_2 = 4,
            driveRight_3 = 5,

            climbTalon_1 = 6,
            climbTalon_2 = 7,

            // Solenoids
            shifter_1 = 1,
            shifter_2 = 3,
            gearLeft_1 = 0,
            gearLeft_2 = 2,
            gearRight_1 = 4,
            gearRight_2 = 6,

            // Digital
            encoderLeft_A = 0,
            encoderLeft_B = 1,
            encoderRight_A = 2,
            encoderRight_B = 3,

            // Analog
            sonarFrontLeft = 0,
            sonarFrontRight = 1;

}
