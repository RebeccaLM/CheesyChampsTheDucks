package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	WPI_TalonSRX leftMotor = new WPI_TalonSRX(id);
	WPI_TalonSRX rightMotor = new WPI_TalonSRX(id);
	Joystick rightStick = new Joystick(id);
	Encoder leftEncoder = new Encoder(); //MUST FIND CORRECT CONSTRUCTOR
	Encoder rightEncoder = new Encoder();
	
	double intermediateVal = 0.5;
	double leftSpeed = 0;
	double rightSpeed = 0;
	
	double autoSpeed = 0.5; //SET TO WANTED SPEED DURING AUTO
	leftEncoder.setDistancePerPulse(); //SET TO NUMBER OF ENCODER TICKS PER CYCLE OR SOMETHING, FIGURE OUT HOW IT WORKS
	rightEncoder.setDistancePerPulse();
	
	public double interpolateVal(double want, double actual) { //for easing in and out of velocities
		return actual + intermediateVal * (want - actual);
	}
	
	@Override
	public void robotInit() {
		System.out.println("Robot Initializing.");
	}

	@Override
	public void teleopInit() {
		System.out.println("Teleop Initializing.");
	}

	@Override
	public void teleopPeriodic() {
		leftSpeed = interpolateVal(rightStick.getRawAxis(axisNum), leftPower);
		rightSpeed = interpolateVal(rightStick.getRawAxis(axisNum), rightPower);
		leftMotor.set(ControlMode.PercentOutput, leftSpeed);
		rightMotor.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	public void moveStraight(double distance) { //move forward or backwards, in feet, UNTESTED
		encoder.setDistance(0); //CHANGE
		while(encoder.getDistance() < distance) {
			leftMotor.set(ControlMode.PercentOutput, autoSpeed);
			rightMotor.set(ControlMode.PercentOutput, autoSpeed);
		}
	}
}
