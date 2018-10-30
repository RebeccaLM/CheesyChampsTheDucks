package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	final int LEFT_TALON_1_ID = 1; //CHANGE TO MATCH ACTUAL IDs
	final int RIGHT_TALON_1_ID = 2;
	final int LEFT_TALON_2_ID = 3;
	final int RIGHT_TALON_2_ID = 4;
	final int RIGHT_STICK_ID = 1;

	WPI_TalonSRX leftMotor1 = new WPI_TalonSRX(leftTalon1Id);
	WPI_TalonSRX rightMotor1 = new WPI_TalonSRX(rightTalon1Id);
	WPI_TalonSRX leftMotor2 = new WPI_TalonSRX(leftTalon2Id);
	WPI_TalonSRX rightMotor2 = new WPI_TalonsRX(rightTalon2Id);
	Joystick rightStick = new Joystick(rightStickId);
	Encoder leftEncoder = new Encoder(); //MUST FIND CORRECT CONSTRUCTOR
	Encoder rightEncoder = new Encoder();

	final double INTERMEDIATE_VAL = 0.5; //CHANGE TO DESIRED VALUE
	double leftSpeed = 0;
	double rightSpeed = 0;

	final double HALF_ROBOT_WIDTH = 1.088; //in feet
	final double HALF_ROBOT_LENGTH = 0.834; //in feet

	final double AUTO_SPEED = 0.75; //SET TO WANTED SPEED DURING AUTO

	public double interpolateVal(double want, double actual) { //for easing in and out of velocities
		return actual + INTERMEDIATE_VAL * (want - actual);
	}

	@Override
	public void robotInit() {
		System.out.println("Robot Initializing.");
		
		leftMotor2.set(ControlMode.Follower, LEFT_TALON_1_ID);
		rightMotor2.set(ControlMode.Follower, RIGHT_TALON_1_ID);

		leftEncoder.setDistancePerPulse(Math.pow(2, 2)*Math.PI); //SET TO NUMBER OF FEET PER ROTATION
		rightEncoder.setDistancePerPulse(Math.pow(2, 2)*Math.PI);
	}

	@Override
	public void teleopInit() {
		System.out.println("Teleop Initializing.");
	}

	@Override
	public void teleopPeriodic() {
		leftSpeed = interpolateVal(rightStick.getRawAxis(axisNum), leftSpeed);
		rightSpeed = interpolateVal(rightStick.getRawAxis(axisNum), rightSpeed);
		leftMotor1.set(ControlMode.PercentOutput, leftSpeed);
		rightMotor1.set(ControlMode.PercentOutput, rightSpeed);
	}

	public void moveStraight(double distance) { //move forward or backwards, in feet, UNTESTED
		leftEncoder.reset();
		rightEncoder.reset();
		
		while(leftEncoder.getDistance() < distance && rightEncoder.getDistance() < distance) {
			leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
			rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
		}
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
	}

	public void rotate(double radians, int direction) { // Angle in radians, <0 is left, >0 is right UNTESTED
		leftEncoder.reset();
		rightEncoder.reset();
		
		double circumference = Math.hypot(HALF_ROBOT_LENGTH, HALF_ROBOT_WIDTH) * radians/2;

		if(direction > 0) {
			while(leftEncoder.getDistance() < circumference && rightEncoder.getDistance() < circumference) {
				leftMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
				rightMotor.set(ControlMode.PercentOutput, AUTO_SPEED);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < circumference && rightEncoder.getDistance() < circumference) {
				leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				rightMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
			}
		} else {
			return;
		}
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
	}

	public void arc(double radius, double radians, int direction) { // Goes in an arc with a given radius, angle in radians, direction <0 left and >0 right UNTESTED
		leftEncoder.reset();
		rightEncoder.reset();
		
		double outCircum = Math.pow(radius + halfRobotWidth, 2) * radians/2;
		double inCircum = Math.pow(radius - halfRobotWidth, 2) * radians/2;

		if(direction > 0) {
			while(leftEncoder.getDistance() < outCircum && rightEncoder.getDistance() < inCircum) {
				leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED * inCircum / outCircum);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < inCircum && rightEncoder.getDistance() < outCircum) {
				leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED * inCircum / outCircum);
			}
		} else {
			return;
		}
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
	}
}
