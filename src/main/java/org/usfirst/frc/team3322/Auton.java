package org.usfirst.frc.team3322;

public class Auton {
    private double startDistance;
    private double ly;
    private double lx;
    private double stringLength;
    private double angleLift;
    private double autonD1;
    private double autonD2;
    private int autonState;

    public Auton() {
        stringLength = 132; //inches
        angleLift = 30; //degrees
        startDistance = 0;
    }

    public void initVars(double angleStart) {
        startDistance = 0;
        double ly = stringLength * Math.sin(Math.toRadians(angleStart)); //trig functions take radians
        double lx = stringLength * Math.cos(Math.toRadians(angleStart));
        autonD1 = ly - lx * Math.tan(Math.toRadians(angleLift));
        autonD2 = lx/Math.cos(Math.toDegrees(angleLift));
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
            if (Robot.navx.getAngle() < 60) {
                Robot.drivetrain.drive(.5, 1);
            } else {
                autonState++;
                startDistance = Robot.drivetrain.getLeftEncValue();
            }
        } else if (autonState == 2) {
            if (Robot.drivetrain.getLeftEncValue() < (startDistance + autonD2)) {
                Robot.drivetrain.driveAngle(60, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 3) {
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
            if (Robot.navx.getAngle() > -60) {
                Robot.drivetrain.drive(.5, -1);
            } else {
                autonState++;
                startDistance = Robot.drivetrain.getRightEncValue();
            }
        } else if (autonState == 2) {
            if (Robot.drivetrain.getRightEncValue() < (startDistance + autonD2)) {
                Robot.drivetrain.driveAngle(60, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 3) {
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