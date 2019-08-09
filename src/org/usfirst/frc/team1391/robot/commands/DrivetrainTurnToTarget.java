package org.usfirst.frc.team1391.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1391.robot.Robot;
import org.usfirst.frc.team1391.robot.RobotMap;
import java.lang.Math;
/**
 * Turns the robot in autonomous (by an angle).
 */
public class DrivetrainTurnToTarget extends Command {
    // Optional speed (if set to anything but zero)
    private double speed = 0;

    // Variables to average the vision reading over a period of time
    private double angle;


    /**
     * Turn the robot to the nearest cube.
     */
    public DrivetrainTurnToTarget() {
        requires(Robot.myDrivetrain);
    }

    /**
     * Turn the robot to the nearest cube at a certain speed.
     *
     * @param speed The speed at which to turn.
     */
    DrivetrainTurnToTarget(double speed) {
        requires(Robot.myDrivetrain);
        this.speed = speed;
    }

    protected void initialize() {
    	
    	 angle = Robot.tx.getDouble(0.0);


         // Update DifferentialDrive
         Robot.myDrivetrain.arcadeDrive(0, 0);

         // Reset and initialize the encoder after collecting the data

         Robot.myDrivetrain.myAHRS.reset();
         
         // Set point, enable gyro PID
         Robot.myDrivetrain.gyroPID.setSetpoint(angle);
         Robot.myDrivetrain.gyroPID.reset();
         Robot.myDrivetrain.gyroPID.enable();
    	
    }

    /**
     * Collects vision data, then sets PID and starts to turn depending on PID output.
     */
    protected void execute() {

            
        double xSpeed = Robot.myDrivetrain.gyroPID.get();

        if (speed != 0) xSpeed = (xSpeed / RobotMap.autonomousDefaultTurningSpeed) * speed;

        Robot.myDrivetrain.arcadeDrive(0, xSpeed);
    }

    /**
     * Finished when it hits the gyroPID target after it was initialized.
     */
    protected boolean isFinished() {
    	
    	return (Robot.myDrivetrain.gyroPID.onTarget());
    }

    protected void end() {
    	System.out.println("turning ended");
    }

    protected void interrupted() {
    }
}
