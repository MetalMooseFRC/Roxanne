package org.usfirst.frc.team1391.robot.commands;

import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToTarget extends Command {
	double direction;
	double startDistance;
	double startingAngleOffTarget;
	
	int tic;
	
    public DriveToTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.myDrivetrain);
    	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	//reset tics
    	tic = 0;

    	//get angle relative to wall (are we on the left or right of it?)
        direction = Math.signum(Robot.camtran.getDoubleArray(new double[] {})[4]);
        //switch to high fps
        Robot.pipeline.setNumber(0);
        
        //reset PIDs
    	 Robot.myDrivetrain.limeAnglePID.reset();
         Robot.myDrivetrain.limeAnglePID.enable();

         
         Robot.myDrivetrain.limeDistPID.setSetpoint(27);
         Robot.myDrivetrain.limeDistPID.reset();
         Robot.myDrivetrain.limeDistPID.enable();
         
       
         //startingAngleOffTarget = startDistance/10;
         
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	//wait until pipeline successfully switches
    	if (tic > 200) {
    	double distance = Robot.myDrivetrain.myLimeDist.pidGet();
    	
    	//calculate changing angle setpoint
    	double setpoint = direction*Math.floor((distance/startDistance)*18 - 2);
    	Robot.myDrivetrain.limeAnglePID.setSetpoint(setpoint);
    	
    	//get PID outputs
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
    	//return when on target or if the 3D was not detected
        return  (Robot.myDrivetrain.limeAnglePID.onTarget() &&  Robot.myDrivetrain.limeDistPID.onTarget()) || direction == 0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Finished");
    	//reset pipeline and return to manual drive
    	Robot.pipeline.setNumber(1);
    	Command drive = new DrivetrainManualControl();
    	drive.start();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
