package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Autonomous {
	Robot robot;
	
	final double HALF_ROBOT_WIDTH = 1.088; //in feet
	final double HALF_ROBOT_LENGTH = 0.834; //in feet

	final double AUTO_SPEED = 0.75; //SET TO WANTED SPEED DURING AUTO
	
	public Autonomous(Robot robot) {
		this.robot = robot;
	}
	
	public void autonomousInit() {
		moveStraight(4);
		rotate(Math.PI/2, 1);
		arc(2, Math.PI, 1);
		
		//autonomousPath();
	}
	
	public void autonomousPath() {
		moveStraight(5);
		rotate(Math.PI/2, -1);
		moveStraight(10);
		arc(2, Math.PI, 1);
		moveStraight(9);
		arc(5, Math.PI/2, 1);
	}
	
	public void moveStraight(double distance) { //move forward or backwards, in feet, UNTESTED
		robot.leftEncoder.reset();
		robot.rightEncoder.reset();
		
		while(robot.leftEncoder.getDistance() < distance || robot.rightEncoder.getDistance() < distance) {
			if (robot.leftEncoder.getDistance() < distance) {
				robot.leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
			}
			if (robot.rightEncoder.getDistance() < distance) {
				robot.rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
			}
			robot.intakeController.autonomousPeriodic();
		}
		
		robot.leftMotor1.set(ControlMode.PercentOutput, 0);
		robot.rightMotor1.set(ControlMode.PercentOutput, 0);
	}

	public void rotate(double radians, int direction) { // Angle in radians, <0 is left, >0 is right UNTESTED
		robot.leftEncoder.reset();
		robot.rightEncoder.reset();
		
		double circumference = Math.hypot(HALF_ROBOT_LENGTH, HALF_ROBOT_WIDTH) * radians/2;

		if(direction > 0) {
			while(robot.leftEncoder.getDistance() < circumference && robot.rightEncoder.getDistance() < circumference) {
				if (robot.leftEncoder.getDistance() < circumference) {
					robot.leftMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
				}
				if (robot.rightEncoder.getDistance() < circumference) {
					robot.rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				robot.intakeController.autonomousPeriodic();
			}
		} else if(direction < 0) {
			while(robot.leftEncoder.getDistance() < circumference && robot.rightEncoder.getDistance() < circumference) {
				if (robot.leftEncoder.getDistance() < circumference) {
					robot.leftMotor1.set(ControlMode.PercentOutput, -AUTO_SPEED);
				}
				if (robot.rightEncoder.getDistance() < circumference) {
					robot.rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				robot.intakeController.autonomousPeriodic();
			}
		} else {
			return;
		}
		
		robot.leftMotor1.set(ControlMode.PercentOutput, 0);
		robot.rightMotor1.set(ControlMode.PercentOutput, 0);
	}

	public void arc(double radius, double radians, int direction) { // Goes in an arc with a given radius, angle in radians, direction <0 left and >0 right UNTESTED
		robot.leftEncoder.reset();
		robot.rightEncoder.reset();
		
		double outCircum = Math.pow(radius + HALF_ROBOT_WIDTH, 2) * radians/2;
		double inCircum = Math.pow(radius - HALF_ROBOT_WIDTH, 2) * radians/2;
		
		double INNER_AUTO_SPEED = AUTO_SPEED * inCircum / outCircum;

		if(direction > 0) {
			while(robot.leftEncoder.getDistance() < outCircum && robot.rightEncoder.getDistance() < inCircum) {
				if (robot.leftEncoder.getDistance() < outCircum) {
					robot.leftMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				if (robot.rightEncoder.getDistance() < inCircum) {
					robot.rightMotor1.set(ControlMode.PercentOutput, INNER_AUTO_SPEED);
				}
				robot.intakeController.autonomousPeriodic();
			}
		} else if(direction < 0) {
			while(robot.leftEncoder.getDistance() < inCircum && robot.rightEncoder.getDistance() < outCircum) {
				if (robot.leftEncoder.getDistance() < inCircum) {
					robot.leftMotor1.set(ControlMode.PercentOutput, INNER_AUTO_SPEED);
				}
				if (robot.rightEncoder.getDistance() < outCircum) {
					robot.rightMotor1.set(ControlMode.PercentOutput, AUTO_SPEED);
				}
				robot.intakeController.autonomousPeriodic();
			}
		} else {
			return;
		}
		
		robot.leftMotor1.set(ControlMode.PercentOutput, 0);
		robot.rightMotor1.set(ControlMode.PercentOutput, 0);
	}
}
