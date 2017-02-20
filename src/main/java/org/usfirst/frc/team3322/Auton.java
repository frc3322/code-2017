package org.usfirst.frc.team3322;

public class Auton {
    private double startDorT; //distance or time
    private double ly;
    private double lx;
    private double angleLift;
    private double autonD1;
    private double autonD2;
    private int autonState;

    public Auton() {
        angleLift = 30; //degrees
        startDorT = 0;
    }

    public void initVars(double xIn, double yIn) { //default
        startDorT = 0;
        double ly = yIn - 12;
        double lx = xIn - 15;
        autonD1 = ly - lx * Math.tan(Math.toRadians(angleLift));
        autonD2 = lx/Math.cos(Math.toRadians(angleLift));
        autonState = 0;
    }

    public void leftPos() {
        if (autonState == 0) {
            if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getLeftEncValue();
            }
        } else if (autonState == 1) {
            if(Robot.navx.getYaw() < 20) {
                Robot.drivetrain.drive(.3, 1);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 2) {
            if (System.currentTimeMillis() < startDorT + 5000) {
                Robot.drivetrain.driveAngle(55, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getLeftEncValue();
            }
        } else if (autonState == 3) {
            if (Robot.drivetrain.getLeftEncValue() > startDorT - 50) {
                Robot.drivetrain.driveAngle(50, .5);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 4) {
            if (System.currentTimeMillis() < startDorT + 2000) {
                Robot.drivetrain.driveAngle(55, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getLeftEncValue();
            }
        } else if (autonState == 5) {
            if (Robot.drivetrain.getLeftEncValue() > startDorT - 50) {
                Robot.drivetrain.driveAngle(65, .5);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 6) {
            if (System.currentTimeMillis() < startDorT + 2000) {
                Robot.drivetrain.driveAngle(55, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 7) {
            //wait until end of auton;
        }
    }

    public void rightPos() {
        if (autonState == 0) {
            if (Robot.drivetrain.getRightEncValue() < startDorT + autonD1 - 2) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getRightEncValue();
            }
        } else if (autonState == 1) {
            if(Robot.navx.getYaw() > -20) {
                Robot.drivetrain.drive(.4, -1);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 2) {
            if (System.currentTimeMillis() < startDorT + 5000) {
                Robot.drivetrain.driveAngle(-56, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getRightEncValue();
            }
        } else if (autonState == 3) {
            if (Robot.drivetrain.getRightEncValue() > startDorT - 50) {
                Robot.drivetrain.driveAngle(-45, .5);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 4) {
            if (System.currentTimeMillis() < startDorT + 1500) {
                Robot.drivetrain.driveAngle(-56, -.8);
            } else {
                autonState++;
                startDorT = Robot.drivetrain.getRightEncValue();
            }
        } else if (autonState == 5) {
            if (Robot.drivetrain.getRightEncValue() > startDorT - 50) {
                Robot.drivetrain.driveAngle(-65, .5);
            } else {
                autonState++;
                startDorT = System.currentTimeMillis();
            }
        } else if (autonState == 6) {
            if (System.currentTimeMillis() < startDorT + 2000) {
                Robot.drivetrain.driveAngle(-56, -.8);
            } else {
                autonState++;
            }
        } else if (autonState == 7) {
            //wait until end of auton;
        }
    }

    public void middlePos() {
        if(autonState == 0) {
            if(Robot.drivetrain.getLeftEncValue() < 100) {
                Robot.drivetrain.driveAngle(0, -.8);
            } else {
                autonState++;
            }
        }
    }
}