package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	final int LEFT_TALON_1_ID = 1; //CHANGE TO MATCH ACTUAL IDs
	final int RIGHT_TALON_1_ID = 2;
	final int LEFT_TALON_2_ID = 3;
	final int RIGHT_TALON_2_ID = 4;
	final int INTAKE_TALON_ID = 5;
	final int RIGHT_STICK_ID = 1;
	
	final int INTAKE_BUTTON_ID = 1;

	WPI_TalonSRX leftMotor1 = new WPI_TalonSRX(LEFT_TALON_1_ID);
	WPI_TalonSRX rightMotor1 = new WPI_TalonSRX(RIGHT_TALON_1_ID);
	WPI_TalonSRX leftMotor2 = new WPI_TalonSRX(LEFT_TALON_2_ID);
	WPI_TalonSRX rightMotor2 = new WPI_TalonSRX(RIGHT_TALON_2_ID);
	WPI_TalonSRX intakeMotor = new WPI_TalonSRX(INTAKE_TALON_ID);
	Joystick rightStick = new Joystick(RIGHT_STICK_ID);
	Encoder leftEncoder = new Encoder(1, 1); //MUST FIND CORRECT CONSTRUCTOR
	Encoder rightEncoder = new Encoder(1, 1);

	final double INTERMEDIATE_VAL = 0.5; //CHANGE TO DESIRED VALUE
	double leftSpeed = 0;
	double rightSpeed = 0;

	final double HALF_ROBOT_WIDTH = 1.088; //in feet
	final double HALF_ROBOT_LENGTH = 0.834; //in feet

	final double AUTO_SPEED = 0.75; //SET TO WANTED SPEED DURING AUTO
	final double INTAKE_TO_WHEEL_SPEED_RATIO = 1;

	public double interpolateVal(double want, double actual) { //for easing in and out of velocities
		return actual + INTERMEDIATE_VAL * (want - actual);
	}
	
	public void handleIntake(int mode) { // 1: turns when moving, 2: turns when button pressed
		switch(mode) {
			case 1:
				intakeMotor.set(ControlMode.PercentOutput, Math.max(leftMotor1.get(), rightMotor1.get()) * INTAKE_TO_WHEEL_SPEED_RATIO);
				break;
			case 2:
				
				break;
			default:
				System.err.println("Typo");
		}
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
	public void autonomousInit() {
		moveStraight(4);
		try {Thread.sleep(1000);} catch(Exception e) {System.err.println("Bad");}
		rotate(Math.PI/2, 1);
		try {Thread.sleep(2000);} catch(Exception e) {System.err.println("Bad");}
		arc(2, Math.PI, 1);
	}
	
	@Override
	public void autonomousPeriodic() {
		//Do nothing
	}

	@Override
	public void teleopInit() {
		System.out.println("Teleop Initializing.");
	}

	@Override
	public void teleopPeriodic() {
		leftSpeed = interpolateVal(rightStick.getRawAxis(RIGHT_STICK_ID), leftSpeed);
		rightSpeed = interpolateVal(rightStick.getRawAxis(RIGHT_STICK_ID), rightSpeed);
		leftMotor1.set(ControlMode.PercentOutput, leftSpeed);
		rightMotor1.set(ControlMode.PercentOutput, rightSpeed);
		handleIntake(1);
	}

	public void moveStraight(double distance) { //move forward or backwards, in feet, UNTESTED
		leftEncoder.reset();
		rightEncoder.reset();
		
		while(leftEncoder.getDistance() < distance || rightEncoder.getDistance() < distance) {
			if (leftEncoder.getDistance() < distance) {
				leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
			}
			if (rightEncoder.getDistance() < distance) {
				rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
			}
			handleIntake(1);
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
				if (leftEncoder.getDistance() < circumference) {
					leftMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
				}
				if (rightEncoder.getDistance() < circumference) {
					rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				handleIntake(1);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < circumference && rightEncoder.getDistance() < circumference) {
				if (leftEncoder.getDistance() < circumference) {
					leftMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
				}
				if (rightEncoder.getDistance() < circumference) {
					rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				handleIntake(1);
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
		
		double outCircum = Math.pow(radius + HALF_ROBOT_WIDTH, 2) * radians/2;
		double inCircum = Math.pow(radius - HALF_ROBOT_WIDTH, 2) * radians/2;
		
		double INNER_AUTO_SPEED = AUTO_SPEED * inCircum / outCircum;

		if(direction > 0) {
			while(leftEncoder.getDistance() < outCircum && rightEncoder.getDistance() < inCircum) {
				if (leftEncoder.getDistance() < outCircum) {
					leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				if (rightEncoder.getDistance() < inCircum) {
					rightMotor1.set(ControlMode.PercentOutput, INNER_AUTO_SPEED);
				}
				handleIntake(1);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < inCircum && rightEncoder.getDistance() < outCircum) {
				if (leftEncoder.getDistance() < inCircum) {
					leftMotor1.set(ControlMode.PercentOutput, INNER_AUTO_SPEED);
				}
				if (rightEncoder.getDistance() < outCircum) {
					rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				handleIntake(1);
			}
		} else {
			return;
		}
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
	}
}
