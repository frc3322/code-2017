package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
    private double startDorT; //distance or time
    private double ly;
    private double lx;
    private double angleLift;
    private double autonD1;
    private double autonD2;
    public int autonState,
            startPos = 0;
    private double correctionAngle = 0;

    public Auton() {
        angleLift = 30; // degrees
        startDorT = 0;
    }

    public void init(double xIn, double yIn) { //default
        startDorT = 0;
        ly = yIn - 12;
        lx = xIn - 15;
        autonD1 = ly - lx * Math.tan(Math.toRadians(angleLift));
        autonD2 = lx/Math.cos(Math.toRadians(angleLift));
        autonState = 0;
    }

    public void run() {
        startPos = (int) SmartDashboard.getNumber("start_pos", 0);
        switch (startPos) {
            case 0:
                leftPos();
                break;
            case 1:
                middlePos();
                break;
            case 2:
                rightPos();
                break;
            default:
                leftPos();
                break;
        }
    }

    public void leftPos() { //uses vision
        switch (autonState) {
            case 0:
                if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1 +2 ) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() < 20) {
                    Robot.drivetrain.drive(.4, 1);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startDorT + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle, -.5);
                    } else {
                        Robot.drivetrain.driveAngle(55, -.5);
                    }
                    if(System.currentTimeMillis() > startDorT + 3000){
                        Robot.holder.extend();
                    }
                } else {
                    Robot.holder.extend();
                }
                break;
        }
    }
    /*public void leftPos() {
        switch (autonState) {
            case 0:
                if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() < 20) {
                    Robot.drivetrain.drive(.3, 1);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startDorT + 3000) {
                    Robot.drivetrain.driveAngle(55, -.8);
                } else {
                    autonState++;
                    startDorT = Robot.drivetrain.getLeftEncValue();
                }
                break;
            case 3:
                if (Robot.drivetrain.getLeftEncValue() > startDorT - 40) {
                    if(!SmartDashboard.getBoolean("detected_target")){
                        correctionAngle = SmartDashboard.getNumber("angle_to_target");
                    }
                    Robot.drivetrain.driveAngle(45, .5);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 4:
                if (System.currentTimeMillis() < startDorT + 2000) {
                    Robot.drivetrain.driveAngle(60, -.8);
                } else {
                    autonState++;
                    startDorT = Robot.drivetrain.getLeftEncValue();
                }
                break;
            case 5:
                if (Robot.drivetrain.getLeftEncValue() > startDorT - 50) {
                    Robot.drivetrain.driveAngle(65, .5);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 6:
                if (System.currentTimeMillis() < startDorT + 2000) {
                    Robot.drivetrain.driveAngle(55, -.8);
                } else {
                    autonState++;
                }
                break;
            case 7:
                Robot.drivetrain.drive(0, 0);
                break;
        }
    }*/

    public void rightPos() {
        switch (autonState) {
            case 0:
                if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1 + 20) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() > -15) {
                    Robot.drivetrain.drive(.4, -1);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startDorT + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle, -.5);
                    } else {
                        Robot.drivetrain.driveAngle(-56, -.5);
                    }
                } else {
                    Robot.holder.extend();
                }
//            case 3:
//                if (Robot.drivetrain.getRightEncValue() > startDorT - 50) {
//                    Robot.drivetrain.driveAngle(-45, .5);
//                } else {
//                    autonState++;
//                    startDorT = System.currentTimeMillis();
//                }
//                break;
//            case 4:
//                if (System.currentTimeMillis() < startDorT + 2000) {
//                    Robot.drivetrain.driveAngle(-56, -.8);
//                } else {
//                    autonState++;
//                    startDorT = Robot.drivetrain.getRightEncValue();
//                }
//                break;
//            case 5:
//                if (Robot.drivetrain.getRightEncValue() > startDorT - 50) {
//                    Robot.drivetrain.driveAngle(-65, .5);
//                } else {
//                    autonState++;
//                    startDorT = System.currentTimeMillis();
//                }
//                break;
//            case 6:
//                if (System.currentTimeMillis() < startDorT + 2000) {
//                    Robot.drivetrain.driveAngle(-56, -.8);
//                } else {
//                    autonState++;
//                }
//                break;
//            case 7:
//                Robot.drivetrain.drive(0, 0);
//                break;
//        }
        }
    }

    public void middlePos() {
        switch (autonState) {
            case 0:
                if (Robot.drivetrain.getLeftEncValue() < 100) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                Robot.drivetrain.drive(0, 0);
                break;
        }
    }
}