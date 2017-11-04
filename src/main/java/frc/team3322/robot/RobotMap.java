package frc.team3322.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static final int
        // Talons
        driveLeft_1 = 6,
        driveLeft_2 = 7,
        indenturedServantL = 8,
        driveRight_1 = 3,
        driveRight_2 = 4,
        indenturedServantR = 5,

        climbTalon_1 = 1,
        climbTalon_2 = 2,

        // Solenoids
        shifter_1 = 3,
        shifter_2 = 2,

        gearHolder_1 = 0,
        gearHolder_2 = 1,

        // Sensors
        encLeft_1 = 0,
        encLeft_2 = 1,
        encRight_1 = 2,
        encRight_2 = 3;

}
