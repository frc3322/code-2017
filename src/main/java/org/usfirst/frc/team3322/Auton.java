package org.usfirst.frc.team3322;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
    private double startDorT = 0,
            ly,
            lx,
            turnAngle = 30,
            autonD1,
            autonD2,
            correctionAngle = 0;
    public int autonState;
    private int startPos;

    public Auton() {
        SmartDashboard.putNumber("x_length", 100);
        SmartDashboard.putNumber("y_length", 132);
        SmartDashboard.putNumber("start_pos", 0);
        SmartDashboard.putString("position_key", "1 = left | 2 = mid | 3 = right");
    }

    public void init() {
        startPos = (int)SmartDashboard.getNumber("start_pos", 0);

        double xIn = SmartDashboard.getNumber("x_length", 100); //100x, 100y if starting on boiler
        double yIn = SmartDashboard.getNumber("y_length", 132); //84x, 100y if starting next to return loading st

        startDorT = 0;
        ly = yIn - 12;
        lx = xIn - 15;
        autonD1 = ly - lx * Math.tan(Math.toRadians(turnAngle));
        autonD2 = lx/Math.cos(Math.toRadians(turnAngle));
        autonState = 0;

        SmartDashboard.putBoolean("auton_ready", startPos != 0);
    }

    public void run() {
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
            default:
                System.out.println("Auton mode not specified, running in middle position...");
                middlePos();
                break;
        }
    }

    public void leftPos() {
        switch (autonState) {
            case 0:
                Robot.holder.retract();

                if (Robot.drivetrain.getLeftEncDist() < startDorT + autonD1 +2 ) {
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

    public void rightPos() {
        switch (autonState) {
            case 0:
                Robot.holder.extend();

                if (Robot.drivetrain.getLeftEncDist() < startDorT + autonD1 + 10) {
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
                   Robot.holder.extend();
                }
        }
    }

    public void middlePos() {
        switch (autonState) {
            case 0:
                if (Robot.drivetrain.getLeftEncDist() < 100) {
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