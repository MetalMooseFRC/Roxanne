package org.usfirst.frc.team1391.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LimelightDistance implements PIDSource {
	
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
		// TODO Auto-generated method stub
        double vert = Robot.tvert.getDouble(0.0);
		double angleToTop = (vert *49.7)/(480) * Math.PI / 180;
		 
		//System.out.println("Dist: " + 2.75/Math.tan(angleToTop));
		return 2.75/Math.tan(angleToTop);
	}
	

}
