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

	double halfRobotWidth = 1.088; //in feet
	double halfRobotLength = 0.834; //in feet

	double autoSpeed = 0.75; //SET TO WANTED SPEED DURING AUTO

	public double interpolateVal(double want, double actual) { //for easing in and out of velocities
		return actual + intermediateVal * (want - actual);
	}

	@Override
	public void robotInit() {
		System.out.println("Robot Initializing.");

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
		leftMotor.set(ControlMode.PercentOutput, leftSpeed);
		rightMotor.set(ControlMode.PercentOutput, rightSpeed);
	}

	public void moveStraight(double distance) { //move forward or backwards, in feet, UNTESTED
		leftEncoder.reset();
		rightEncoder.reset();
		while(leftEncoder.getDistance() < distance && rightEncoder.getDistance() < distance) {
			leftMotor.set(ControlMode.PercentOutput, autoSpeed);
			rightMotor.set(ControlMode.PercentOutput, autoSpeed);
		}
	}

	public void rotate(double radians, int direction) { // Angle in radians, -1 is left, 1 is right
		double circumference = Math.hypot(halfRobotLength, halfRobotWidth) * radians/2;

		if(direction > 0) {
			while(leftEncoder.getDistance() < circumference && rightEncoder.getDistance() < circumference) {
				leftMotor.set(ControlMode.PercentOutput, -autoSpeed);
				rightMotor.set(ControlMode.PercentOutput, autoSpeed);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < circumference && rightEncoder.getDistance() < circumference) {
				leftMotor.set(ControlMode.PercentOutput, autoSpeed);
				rightMotor.set(ControlMode.PercentOutput, -autoSpeed);
			}
		} else {
			return;
		}
	}

	public void arc(double radius, double radians, int direction) { // Goes in an arc with a given radius, angle in radians, and direction -1 left and 1 right
		double outCircum = Math.pow(radius + halfRobotWidth, 2) * radians/2;
		double inCircum = Math.pow(radius - halfRobotWidth, 2) * radians/2;

		if(direction > 0) {
			while(leftEncoder.getDistance() < outCircum && rightEncoder.getDistance() < inCircum) {
				leftMotor.set(ControlMode.PercentOutput, autoSpeed);
				rightMotor.set(ControlMode.PercentOutput, autoSpeed * inCircum / outCircum);
			}
		} else if(direction < 0) {
			while(leftEncoder.getDistance() < inCircum && rightEncoder.getDistance() < outCircum) {
				leftMotor.set(ControlMode.PercentOutput, autoSpeed);
				rightMotor.set(ControlMode.PercentOutput, autoSpeed * inCircum / outCircum);
			}
		} else {
			return;
		}
	}
}