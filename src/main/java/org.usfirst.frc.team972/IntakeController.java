package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class IntakeController {
	Robot robot;

	public final int INTAKE_TALON_ID = 5;
	public final int INTAKE_BUTTON_ID = 1;
	public WPI_TalonSRX intakeMotor = new WPI_TalonSRX(INTAKE_TALON_ID);

	boolean intakeOn = false;
	final double INTAKE_SPEED = 0.75; //SET TO DESIRED VALUE
	final double INTAKE_TO_SPEED_RATIO = 0.75;
	
	public IntakeController(Robot robot) {
		this.robot = robot;
	}
	
	public void teleopPeriodic() {
		intakeOn = robot.joystick.getRawButtonPressed(INTAKE_BUTTON_ID) == true ? !intakeOn : intakeOn;
		intakeMotor.set(ControlMode.PercentOutput, intakeOn ? INTAKE_SPEED : 0);
	}
	
	public void autonomousPeriodic() {
		intakeMotor.set(ControlMode.PercentOutput, Math.max(robot.rightSpeed, robot.leftSpeed) * INTAKE_TO_SPEED_RATIO);
	}
}
