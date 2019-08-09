package org.usfirst.frc.team1391.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;;
public class LimelightAngle implements PIDSource {
	//input is displacement
	PIDSourceType pidSource = PIDSourceType.kDisplacement;

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		this.pidSource = pidSource;

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return pidSource;
	}

	@Override
	public double pidGet() {
		// get the network table limelight angle
		
		//System.out.println("Theta: " + Robot.tx.getDouble(0.0));
		return Robot.tx.getDouble(0.0);
	}

}
