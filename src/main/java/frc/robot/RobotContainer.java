/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.auto.BallStealAuto;
import frc.robot.commands.auto.GeneratorAuto;
import frc.robot.commands.auto.SplineTesting;
import frc.robot.commands.auto.TrenchRunAuto;
import frc.robot.commands.ballmanager.AutoIntake;
import frc.robot.commands.shooter.AutoShooter;
import frc.robot.subsystems.*;
import frc.robot.utils.CustomRamseteCommand;
import frc.robot.commands.drivetrain.AlignToTarget;
import frc.robot.commands.drivetrain.DriverControl;
import frc.robot.utils.Logger;
import frc.robot.Constants.DriveConstants;


import java.util.List;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...

    private final Limelight limelight = new Limelight();
    private final Shooter shooter = new Shooter(limelight);
    private final Conveyor conveyor = new Conveyor(shooter::atSetpoint);
    private final Intake intake = new Intake();
    private final Climber climber = new Climber();

    private final Drivetrain drivetrain = new Drivetrain();

    private static Joystick driverStick = new Joystick(0);
    private static Joystick operatorStick = new Joystick(1);

    private JoystickButton driverAButton = new JoystickButton(driverStick, 1);
    private JoystickButton driverBButton = new JoystickButton(driverStick, 2);
    private POVButton driverLeftPOVButton = new POVButton(driverStick, 90);

    private JoystickButton operatorAButton = new JoystickButton(operatorStick, 1);
    private JoystickButton operatorBButton = new JoystickButton(operatorStick, 2);
    private JoystickButton operatorLeftShoulder = new JoystickButton(operatorStick, 5);
    private JoystickButton operatorYButton = new JoystickButton(operatorStick, 4);
    private JoystickButton operatorXButton = new JoystickButton(operatorStick, 3);



    private DriverControl driverControlCommand = new DriverControl(
            drivetrain,
            () -> driverStick.getRawAxis(1),
            () -> driverStick.getRawAxis(4)
    );

    private Logger<Subsystem> subsystemLogger = new Logger<>();

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();
    }

    public void initDefaultCommands() {
        drivetrain.initDefaultCommand(driverStick);
    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        driverAButton.whileHeld(new AlignToTarget(limelight, drivetrain, () -> driverStick.getRawAxis(1)));
        driverBButton.whenPressed(new InstantCommand(() -> drivetrain.setControlsFlipped(!drivetrain.isControlsFlipped())));

        operatorAButton.whileHeld(new AutoIntake(shooter,intake, conveyor, AutoIntake.IntakeType.NORMAL_INTAKE));
        operatorXButton.whileHeld(new AutoIntake(shooter,intake, conveyor, AutoIntake.IntakeType.FORCED_SHOOT));
        operatorBButton.whileHeld(new AutoIntake(shooter,intake, conveyor, AutoIntake.IntakeType.NORMAL_EXTAKE));
        operatorYButton.whileHeld(new AutoIntake(shooter,intake,conveyor, AutoIntake.IntakeType.FORCED_INTAKE));

        operatorLeftShoulder.whileHeld(new AutoShooter(shooter,conveyor,2300));
        operatorYButton.whileHeld(new InstantCommand(
                () -> climber.setPower(1.0)
        ));
//        operatorXButton.whileHeld(new InstantCommand(
//                () -> climber.setPower(-1.0)
//        ));
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getGeneratorAuto() {
        return new GeneratorAuto(drivetrain, limelight);
    }

    public Command getBallStealAutonomous() {
        // return new RightStart(drivetrain, limelight);
        return new BallStealAuto(drivetrain, intake, conveyor, shooter, limelight);
    }

    public Command getTrenchRunAuto() {
        return new TrenchRunAuto(drivetrain, intake, conveyor, shooter, limelight);
    }

    public Command getSplineTestAuto(){return new SplineTesting(drivetrain,limelight);}


    public Command getRamseteCommand() {
        drivetrain.resetAll();
        DifferentialDriveVoltageConstraint voltageConstraint =
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(
                                Constants.DriveConstants.kStatic,
                                Constants.DriveConstants.kV,
                                Constants.DriveConstants.kA
                        ),
                        drivetrain.getKinematics(),
                        10
                );

        CentripetalAccelerationConstraint centripetalAccelerationConstraint = new CentripetalAccelerationConstraint(Units.feetToMeters(4.5));
        TrajectoryConfig config =
                new TrajectoryConfig(Units.feetToMeters(10), Units.feetToMeters(6.5))
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(drivetrain.getKinematics())
                        // Apply the voltage constraint
                        .addConstraint(voltageConstraint)
                        .addConstraint(centripetalAccelerationConstraint);

        TrajectoryConfig reverseConfig =
                new TrajectoryConfig(Units.feetToMeters(10), Units.feetToMeters(6.5))
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(drivetrain.getKinematics())
                        // Apply the voltage constraint
                        .addConstraint(voltageConstraint)
                        .addConstraint(centripetalAccelerationConstraint)
                        .setReversed(true);

        Trajectory simpleSCurve = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d().fromDegrees(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(
                        new Translation2d(Units.feetToMeters(7), Units.feetToMeters(-2.5))
                ),
                new Pose2d(Units.feetToMeters(13), Units.feetToMeters(-4.5), new Rotation2d().fromDegrees(30)),
                // Pass config
                config
        );

        Trajectory reverseSimpleSCurve = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(Units.feetToMeters(13), Units.feetToMeters(-4.5), new Rotation2d().fromDegrees(30)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(
                        new Translation2d(Units.feetToMeters(7), Units.feetToMeters(-2.5))
                ),
                new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0), new Rotation2d().fromDegrees(0)),
                // Pass config
                reverseConfig
        );

        CustomRamseteCommand ramseteCommand = new CustomRamseteCommand(
                simpleSCurve,
                drivetrain::getPose,
                new RamseteController(DriveConstants.kRamseteB, DriveConstants.kRamseteZeta),
                new SimpleMotorFeedforward(
                        DriveConstants.kStatic,
                        DriveConstants.kV,
                        DriveConstants.kA
                ),
                drivetrain.getKinematics(),
                drivetrain::getWheelSpeeds,
                new PIDController(DriveConstants.kDriveP, 0, DriveConstants.kDriveD),
                new PIDController(DriveConstants.kDriveP, 0, DriveConstants.kDriveD),
                drivetrain::tankDriveVolts,
                drivetrain
        );

        CustomRamseteCommand ramseteCommand2 = new CustomRamseteCommand(
                reverseSimpleSCurve,
                drivetrain::getPose,
                new RamseteController(DriveConstants.kRamseteB, DriveConstants.kRamseteZeta),
                new SimpleMotorFeedforward(
                        DriveConstants.kStatic,
                        DriveConstants.kV,
                        DriveConstants.kA
                ),
                drivetrain.getKinematics(),
                drivetrain::getWheelSpeeds,
                new PIDController(DriveConstants.kDriveP, 0, DriveConstants.kDriveD),
                new PIDController(DriveConstants.kDriveP, 0, DriveConstants.kDriveD),
                drivetrain::tankDriveVolts,
                drivetrain
        );

        return ramseteCommand.andThen(ramseteCommand2.andThen(() -> drivetrain.tankDriveVolts(0, 0)));
    }


}
