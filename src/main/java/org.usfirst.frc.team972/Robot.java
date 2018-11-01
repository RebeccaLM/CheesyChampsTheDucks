package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	
	final int LEFT_TALON_1_ID = 1; //CHANGE TO MATCH ACTUAL IDs
	final int RIGHT_TALON_1_ID = 2;
	final int LEFT_TALON_2_ID = 3;
	final int RIGHT_TALON_2_ID = 4;
	final int JOYSTICK_ID = 1;
	
	final double WHEEL_RADIUS = 1/6;

	WPI_TalonSRX leftMotor1 = new WPI_TalonSRX(LEFT_TALON_1_ID);
	WPI_TalonSRX rightMotor1 = new WPI_TalonSRX(RIGHT_TALON_1_ID);
	WPI_TalonSRX leftMotor2 = new WPI_TalonSRX(LEFT_TALON_2_ID);
	WPI_TalonSRX rightMotor2 = new WPI_TalonSRX(RIGHT_TALON_2_ID);
	Joystick joystick = new Joystick(JOYSTICK_ID);
	
	Encoder leftEncoder = new Encoder(1, 1); //MUST FIND CORRECT CONSTRUCTOR
	Encoder rightEncoder = new Encoder(1, 1);
	
	public IntakeController intakeController = new IntakeController(this);
	public TankDriveTeleop tankDriveTeleop = new TankDriveTeleop(this);
	public Autonomous autonomous = new Autonomous(this);

	double leftSpeed = 0;
	double rightSpeed = 0;

	@Override
	public void robotInit() {
		System.out.println("Robot Initializing.");
		
		leftMotor2.set(ControlMode.Follower, LEFT_TALON_1_ID);
		rightMotor2.set(ControlMode.Follower, RIGHT_TALON_1_ID);

		leftEncoder.setDistancePerPulse(Math.pow(WHEEL_RADIUS, 2)*Math.PI); //SET TO NUMBER OF FEET PER ROTATION
		rightEncoder.setDistancePerPulse(Math.pow(WHEEL_RADIUS, 2)*Math.PI);
	}
	
	@Override
	public void autonomousInit() {
		autonomous.autonomousInit();
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
		tankDriveTeleop.teleopPeriodic();
		intakeController.teleopPeriodic();
	}

	
}
