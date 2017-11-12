package frc.team3322.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
    private double startTime = 0,
            ly,
            lx,
            turnAngle = 30,
            autonD1,
            autonD2,
            correctionAngle = 0;
    public int autonState;
    private int startPos;
    private int startSide;

    public Auton() {
        SmartDashboard.putNumber("start_pos", 0);
        SmartDashboard.putNumber("start_side", 0);

        SmartDashboard.putString("position_key", "1 = left | 2 = mid | 3 = right");
        SmartDashboard.putString("side_key", "1 = blue | 2 = red");
    }

    public void init() {
        Robot.holder.retract();
        Robot.drivetrain.resetEncDist();
        Robot.navx.reset();
        Robot.compressor.start();

        startPos = (int)SmartDashboard.getNumber("start_pos", 0);
        startSide = (int)SmartDashboard.getNumber("start_side", 0);

        double xIn;
        double yIn;
        switch (startSide) {
            case 1:
                xIn = 100;
                yIn = 132;
                break;
            case 2:
                xIn = 84;
                yIn = 100;
                break;
            default:
                xIn = 100;
                yIn = 132;
                break;
        }

        autonState = 0;
        startTime = 0;

        ly = yIn - 12;
        lx = xIn - 15;
        autonD1 = ly - lx * Math.tan(Math.toRadians(turnAngle));
        autonD2 = lx/Math.cos(Math.toRadians(turnAngle));

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
                System.out.println("Auton mode not specified");
                break;
        }
    }

    public void leftPos() {
        switch (autonState) {
            case 0:
                Robot.holder.retract();

                if (Robot.drivetrain.getRobotDisp() < autonD1 + 2) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() < turnAngle - 10) {
                    Robot.drivetrain.drive(.4, .6);
                } else {
                    autonState++;
                    startTime = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startTime + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle, -.6);

                        SmartDashboard.putBoolean("lost_target", false);
                    } else {
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw(), -.5);

                        SmartDashboard.putBoolean("lost_target", true);
                    }

                    if (System.currentTimeMillis() > startTime + 1500){
                       Robot.holder.extend();
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
                Robot.holder.retract();

                if (Robot.drivetrain.getRobotDisp() < autonD1 + 10) {
                    Robot.drivetrain.driveAngle(0, -.8);
                } else {
                    autonState++;
                }
                break;
            case 1:
                if (Robot.navx.getYaw() > -turnAngle) {
                    Robot.drivetrain.drive(.4, -.6);
                } else {
                    autonState++;
                    startTime = System.currentTimeMillis();
                }
                break;
            case 2:
                if (System.currentTimeMillis() < startTime + 5000) {
                    if (SmartDashboard.getBoolean("detected_target", false)) {
                        correctionAngle = SmartDashboard.getNumber("angle_to_target", 0);
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw() + correctionAngle + 10, -.5);

                        SmartDashboard.putBoolean("lost_target", false);

                    } else {
                        Robot.drivetrain.driveAngle(Robot.navx.getYaw(), -.5);

                        SmartDashboard.putBoolean("lost_target", true);
                    }

                    if (System.currentTimeMillis() > startTime + 1500){
                        Robot.holder.extend();
                    }
                } else {
                    Robot.drivetrain.drive(0, 0);
                }
                break;
        }
    }

    public void middlePos() {
        switch (autonState) {
            case 0:
                Robot.holder.extend();

                if (Robot.drivetrain.getRobotDisp() < 100) {
                    Robot.drivetrain.driveAngle(0, -.6);
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