package org.usfirst.frc.team3322;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class Auton {

    public Auton() {
        // TODO Parse the coordinate stream for reassembly
        /*try {
            InputStream in = new FileInputStream("AutonPath");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    public static String convertStreamToString(java.io.InputStream stream) {
        try {
            return(new String(readAllBytes(get("AutonPath"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void parseCoordinates() {

    }
}
