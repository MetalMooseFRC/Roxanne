package org.usfirst.frc.team1391.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team1391.robot.BlankPIDOutput;
import org.usfirst.frc.team1391.robot.LimelightAngle;
import org.usfirst.frc.team1391.robot.LimelightDistance;
import org.usfirst.frc.team1391.robot.Robot;
import org.usfirst.frc.team1391.robot.RobotMap;
import org.usfirst.frc.team1391.robot.commands.DrivetrainManualControl;

/**
 * Controls the drivebase motors.
 */
public class Drivetrain extends Subsystem {

    // Objects that control the driving of the drivebase
    private VictorSP leftMotor = new VictorSP(RobotMap.drivebaseLeftMotorPort);
    private VictorSP rightMotor = new VictorSP(RobotMap.drivebaseRightMotorPort);
    private DifferentialDrive myDifferentialDrive = new DifferentialDrive(leftMotor, rightMotor);

    // Sensors (encoder, gyro)
    public Encoder myEncoder = new Encoder(RobotMap.drivetrainEncoderAPort, RobotMap.drivetrainEncoderBPort, false, Encoder.EncodingType.k4X);
    public AHRS myAHRS = new AHRS(SPI.Port.kMXP);
    
    public LimelightAngle myLimeAngle = new LimelightAngle();
    public LimelightDistance myLimeDist = new LimelightDistance();

    // PIDController objects
    public PIDController encoderPID = new PIDController(RobotMap.drivetrainEncoderP, RobotMap.drivetrainEncoderI, RobotMap.drivetrainEncoderD, 0, myEncoder, new BlankPIDOutput());
    public PIDController gyroPID = new PIDController(RobotMap.drivetrainGyroP, RobotMap.drivetrainGyroI, RobotMap.drivetrainGyroD, 0, myAHRS, new BlankPIDOutput());
    
    //long distance values
    public PIDController limeAnglePID = new PIDController(0.1, 0.001, 0, 0, myLimeAngle, new BlankPIDOutput());
    public PIDController limeDistPID = new PIDController(0.04, 0.0001, 0, 0, myLimeDist, new BlankPIDOutput());
    
    //short distance values
    //public PIDController limeAnglePID = new PIDController(0.1, 0.0001, 0, 0, myLimeAngle, new BlankPIDOutput());
    //public PIDController limeDistPID = new PIDController(0.04, 0, 0, 0, myLimeDist, new BlankPIDOutput());

    public Drivetrain() {
        // Encoder PIDObject values
        encoderPID.setOutputRange(-RobotMap.autonomousDefaultDrivingSpeed, RobotMap.autonomousDefaultDrivingSpeed);
        encoderPID.setAbsoluteTolerance(RobotMap.drivetrainEncoderPIDError); //in inches

        // Gyro PIDObject values
        gyroPID.setInputRange(-180.0, +180.0);
        gyroPID.setOutputRange(-RobotMap.autonomousDefaultTurningSpeed, RobotMap.autonomousDefaultTurningSpeed);
        gyroPID.setAbsoluteTolerance(RobotMap.drivetrainGyroPIDError); //in degrees
        gyroPID.setContinuous(true); //loops around

        // Sets myEncoder to output distance traveled in inches
        myEncoder.setDistancePerPulse(RobotMap.drivetrainEncoderCoefficient);
        
        limeAnglePID.setAbsoluteTolerance(3.5);
        limeAnglePID.setOutputRange(-1, 1);
        limeAnglePID.setInputRange(-24.85, 24.85);
        
        limeDistPID.setAbsoluteTolerance(3);
        limeDistPID.setOutputRange(-0.75, 0.75);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new DrivetrainManualControl());
    }

    /**
     * Updates differentialDrive using the arcadeDrive function.
     *
     * @param y Forward speed of the robot.
     * @param x Turning speed of the robot.
     */
    public void arcadeDrive(double y, double x) {
        myDifferentialDrive.arcadeDrive(y, x);
    }

    /**
     * Updates differentialDrive using the tankDrive function.
     *
     * @param left Speed of the left side of the robot.
     * @param right Speed of the right side of the robot.
     */
    public void tankDrive(double left, double right) {
        myDifferentialDrive.tankDrive(left, right);
    }

    /**
     * Updates differentialDrive using the arcadeDrive function throttled by a polynomial function dependent on the position of the elevator.
     *
     * The further the elevator is, the slower the drivebase moves.
     *
     * @param y Forward speed of the robot.
     * @param x Turning speed of the robot.
     */
    public void throttledArcadeDrive(double y, double x) {
        double elevatorPosition = Robot.myElevator.elevatorEncoder.getDistance();

        // If the encoder somehow messes up, we will simply ignore it (the error accumulation throughout the match is not that huge
        if (elevatorPosition > 100) elevatorPosition = 100;
        else if (elevatorPosition < 0) elevatorPosition = 0;

        // The coefficients of the polynomial
        double[] coefficients = new double[]{0.00002452791461, -0.004905582922, 1};

        // Calculate the y value at point x of the polynomial
        // Example for 3rd degree polynomial: ax^3 + bx^2 + cx + d = x(x(x(a) + b) + c) + d... this simplifies the calculation
        double functionValue = 0;
        for (double coefficient : coefficients) functionValue = functionValue * elevatorPosition + coefficient;

        double alteredX = x * functionValue;
        double alteredY = y * functionValue;
        
        myDifferentialDrive.arcadeDrive(alteredY, alteredX);
    }
}
