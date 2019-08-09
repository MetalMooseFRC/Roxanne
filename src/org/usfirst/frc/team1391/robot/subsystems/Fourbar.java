package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1391.robot.RobotMap;
import org.usfirst.frc.team1391.robot.commands.FourbarManualControl;

/**
 * Controls the fourbar motor.
 */
public class Fourbar extends Subsystem {

    // Speed controller controlling the fourbar
    private Spark fourbarMotor = new Spark(RobotMap.fourbarMotorPort);

    public Fourbar() {
    }

    public void initDefaultCommand() {
        setDefaultCommand(new FourbarManualControl());
    }

    /**
     * Sets the speed of the fourbar motor.
     *
     * @param speed The speed to set the fourbar motor to.
     */
    public void setSpeed(double speed) {
        fourbarMotor.set(speed);
    }

    /**
     * Sets the motor to the holdUp speed.
     */
    public void holdUp() {
        fourbarMotor.set(RobotMap.fourbarHoldUpSpeed);
    }

    /**
     * Sets the motor to the holdDown speed.
     */
    public void holdDown() {
        fourbarMotor.set(RobotMap.fourbarHoldDownSpeed);
    }
}
