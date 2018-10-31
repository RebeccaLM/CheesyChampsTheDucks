package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class TankDriveTeleop {
	Robot robot;

	final int RIGHT_STICK_ID = 1;
	final double INTERMEDIATE_VAL = 0.5;
	
	public TankDriveTeleop(Robot robot) {
		this.robot = robot;
	}
	
	public double interpolateVal(double want, double actual) { //for easing in and out of velocities
		return actual + INTERMEDIATE_VAL * (want - actual);
	}
	
	public void teleopPeriodic() {
		robot.leftSpeed = interpolateVal(robot.joystick.getRawAxis(RIGHT_STICK_ID), robot.leftSpeed);
		robot.rightSpeed = interpolateVal(robot.joystick.getRawAxis(RIGHT_STICK_ID), robot.rightSpeed);
		robot.leftMotor1.set(ControlMode.PercentOutput, robot.leftSpeed);
		robot.rightMotor1.set(ControlMode.PercentOutput, robot.rightSpeed);
	}
}
