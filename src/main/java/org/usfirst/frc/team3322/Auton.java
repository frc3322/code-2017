package org.usfirst.frc.team3322;

public class Auton {
    private double startDistance;
    private double ly;
    private double lx;
    private double stringLength;
    private double smallY; //distance of measurement
    private double angleLift;
    private double autonD1;
    private double autonD2;
    private int autonState;

    public Auton() {
        stringLength = 133; //inches
        smallY = 12;
        angleLift = 30; //degrees
        startDistance = 0;
    }

    public void initVars(double smallX) {
        startDistance = 0;
        double hypSmall = Math.sqrt(Math.pow(smallX, 2) + 144);
        double ly = (stringLength * smallY / hypSmall) - 8; //string measured behind front
        double lx = (stringLength * smallX / hypSmall) + 12; //string measured off center
        autonD1 = ly - lx * Math.tan(Math.toRadians(angleLift));
        autonD2 = lx/Math.cos(Math.toRadians(angleLift));
        autonState = 0;
    }

    public void leftPos() {
        if (autonState == 0) {
            if (Robot.drivetrain.getLeftEncValue() < startDistance + autonD1) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
                startDistance = Robot.drivetrain.getLeftEncValue();
            }
        } else if (autonState == 1) {
            if (Robot.drivetrain.getLeftEncValue() < (startDistance + autonD2)) {
                Robot.drivetrain.driveAngle(58, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 2) {
            //wait until end of auton
        }
    }

    public void rightPos() {
        if (autonState == 0) {
            if (Robot.drivetrain.getRightEncValue() < startDistance + autonD1) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
                startDistance = Robot.drivetrain.getRightEncValue();
            }
        } else if (autonState == 1) {
            if (Robot.drivetrain.getRightEncValue() < (startDistance + autonD2)) {
                Robot.drivetrain.driveAngle(-58, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 2) {
            //wait until end of auton
        }
    }

    public void middlePos() {
        if(autonState == 0) {
            if(Robot.drivetrain.getLeftEncValue() < ly) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        }
    }
}