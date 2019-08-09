package org.usfirst.frc.team1391.robot.commands;

import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LarryAndWinston extends Command {
	double direction;
	double startDistance;
	double startingAngleOffTarget;
	
	int tic;
	
    public LarryAndWinston(int direction) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.myDrivetrain);
    	this.direction = Math.signum(direction);
    	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	tic = 0;
    	
    	//Robot.pipeline.setNumber(1);
    	
        direction = Math.signum(Robot.camtran.getDoubleArray(new double[] {})[4]);
        Robot.pipeline.setNumber(0);

        //Robot.myDrivetrain.limeAnglePID.setSetpoint(0);
    	 Robot.myDrivetrain.limeAnglePID.reset();
         Robot.myDrivetrain.limeAnglePID.enable();

         // Set point, enable gyro PID
         // The Setpoint is 0, because we want the robot to keep driving straight
         Robot.myDrivetrain.limeDistPID.setSetpoint(27);
         Robot.myDrivetrain.limeDistPID.reset();
         Robot.myDrivetrain.limeDistPID.enable();
         
       
         //startingAngleOffTarget = startDistance/10;
         
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if (tic > 200) {
    	double distance = Robot.myDrivetrain.myLimeDist.pidGet();
    	double setpoint = direction*Math.floor((distance/startDistance)*18 - 2);
    	Robot.myDrivetrain.limeAnglePID.setSetpoint(setpoint);
    	
    	double ySpeed = Robot.myDrivetrain.limeDistPID.get();
        double xSpeed = Robot.myDrivetrain.limeAnglePID.get();
    	
        Robot.myDrivetrain.arcadeDrive(-ySpeed, -xSpeed);
        
        System.out.println("set: " + setpoint);
        System.out.println("X: " + xSpeed + " Y: " + ySpeed);

    	} else {
    	tic++;
        System.out.println(direction);
        startDistance = Robot.myDrivetrain.myLimeDist.pidGet();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return  (Robot.myDrivetrain.limeAnglePID.onTarget() &&  Robot.myDrivetrain.limeDistPID.onTarget()) || direction == 0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Finished");
    	Robot.pipeline.setNumber(1);
    	Command drive = new DrivetrainManualControl();
    	drive.start();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
