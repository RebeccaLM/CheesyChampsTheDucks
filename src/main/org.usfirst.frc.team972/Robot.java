package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	WPI_TalonSRX leftMotor = new WPI_TalonSRX(id);
	WPI_TalonSRX rightMotor = new WPI_TalonSRX(id);
	Joystick rightStick = new Joystick(id);
	
	double intermediateVal = 0.5;
	double leftPower = 0;
	double rightPower = 0;
	
	public double interpolateVal(want, actual) {
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
		leftPower = interpolateVal(rightStick.getRawAxis(axisNum), leftPower);
		rightPower = interpolateVal(rightStick.getRawAxis(axisNum), rightPower);
		leftMotor.set(ControlMode.PercentOutput, leftPower);
		rightMotor.set(ControlMode.PercentOutput, rightPower);
	}
}
