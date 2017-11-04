package frc.team3322.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
    private double startDorT; //distance or time
    private double ly;
    private double lx;
    private double angleLift;
    private double autonD1;
    private double autonD2;
    private double correctionAngle = 0;
    public int autonState;

    public Auton() {
        // TODO verify the difference of the auton turning angles with the trig calculation angle used (20deg vs 30deg respectively)
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
        int startPos = (int)SmartDashboard.getNumber("start_pos", 0);
        switch (startPos) {
            case 1:
                leftPos();
                break;
            case 2:
                middlePos();
                break;
            case 3:
                rightPos();
                break;
            case 4:
                leftPos();
                break;
            case 5:
                middlePos();
                break;
            case 6:
                rightPos();
                break;
            default:
                leftPos();
                break;
        }
    }

    // Uses vision
    public void leftPos() {
        switch (autonState) {
            case 0:
                Robot.holder.extend();
                if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1 +2 ) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getAngle() < 20) {
                    Robot.drivetrain.drive(.4, .8);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startDorT + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle, -.6);
                        SmartDashboard.putBoolean("lost_target",false);
                    } else {
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw(), -.5);
                        SmartDashboard.putBoolean("lost_target",true);
                    }
                    if(System.currentTimeMillis() > startDorT + 1500){
                       Robot.holder.retract();
                    }
                } else {
                    Robot.drivetrain.drive(0, 0);
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
                if (Robot.drivetrain.getLeftEncValue() < startDorT + autonD1 + 10) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() < 30.5) {
                    Robot.drivetrain.drive(.4, -.6);
                } else {
                    autonState++;
                    startDorT = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startDorT + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle + 10, -.5);
                    } else {
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw(), -.5);
                    }
                } else {
                   // Robot.holder.extend();
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