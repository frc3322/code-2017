package org.usfirst.frc.team3322;

import java.lang.Math;
/*import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;*/

public class Auton {

    public Auton() {
        // TODO Parse the coordinate stream for reassembly
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    /*public static String convertStreamToString() { //file reading
        try {
            return(new String(readAllBytes(get("AutonPath"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/
    public double getAngle(float xTarget, float yTarget) { //returns target angle in degrees, based on current position and target position
        float x = Robot.navx.getDisplacementX();
        float y = Robot.navx.getDisplacementY();
        return Math.toDegrees(Math.atan((yTarget-y)/(xTarget-x)));
    }
}