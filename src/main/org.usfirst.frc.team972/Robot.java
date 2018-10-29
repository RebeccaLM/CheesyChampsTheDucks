package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	WPI_TalonSRX leftMotor = new WPI_TalonSRX(id);
	WPI_TalonSRX rightMotor = new WPI_TalonSRX(id);
	Joystick rightStick = new Joystick(id);
	
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
		double leftPower = rightStick.getRawAxis(axisNum);
		double rightPower = rightStick.getRawAxis(axisNum);
		leftMotor.set(ControlMode.PercentOutput, leftPower);
		rightMotor.set(ControlMode.PercentOutput, rightPower);
	}
}
