/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants {
        public static double kMaxAngularSpeed = 2 * Math.PI; // one rotation per second
        public static double kTrackWidth = 0.91; // meters
        public static double kWheelRadius = 0.0762; // meters
        public static double kShaftEncoderResolution = 2048; // counts per revolution bore encoder
        public static double positionChangePerRotation = 8.73; // Motor rotation per shaft rotation
        public static double kMaxVelocity = 13; // feet per second
        public static double kMaxAcceleration = 3; // Max Accel fet per second squared

        public static double kStatic = 0.177; // Constant feedforward term for the robot drive.
        public static double kV = 2.25; // Velocity-proportional feedforward term for the robot drive
        public static double kA = 0.44; //Acceleration-proportional feedforward term for the robot (.348)

        // Tuning parameter (b > 0) for which larger values make convergence more aggressive like a proportional term
        public static double kRamseteB = 2;

        // Tuning parameter (0 &lt; zeta &lt; 1) for which larger values provide more damping in response
        public static double kRamseteZeta = 0.7;

        public static double kHeadingP = 1;
        public static double kHeadingD = 0;
        public static double kHeadingI = 0;

        public static double kAlignP = 0.04;
        public static double kAlignD = 0.0003;

        public static double kDriveP = 2.45; // 3 stable
        public static double kDriveI = 0;
        public static double kDriveD = 0;

        public static int[] kLeftEncoderPorts = new int[]{0, 1};
        public static int[] kRightEncoderPorts = new int[]{2, 3};

        public static boolean kLeftEncoderReversed = false;
        public static boolean kRightEncoderReversed = false;

        public static boolean kGyroReversed = false;

        public static DifferentialDriveKinematics kDriveKinematics
                = new DifferentialDriveKinematics(DriveConstants.kTrackWidth);
    }

    public static final class IntakeConstants {
        public static int kIntakeMotorPort = 7;
        public static int kSerializerMotorPort = 8;

        public static double kIntakePower = 1.0;
        public static double kSerializerPower = 0.5;
    }

    public static final class ShooterConstants {
        // TODO: tune these gains
        public static double kV = 0.0;
        public static double kA = 0.0;
        public static double kStatic = 0.0;
        public static double kWPILibP = 0.0001;


        public static double kSparkMaxP = 0.0005;
        public static double kSparkMaxFeedforward = 0.000195; // .00022


        public static int kLeftMotorPort = 11;
        public static int kRightMotorPort = 12;

        // ONLY TO BE USED IN EMERGENCIES
        // RPM SHOULD PRIMARILY COME FROM LUT
        public static double kTrenchShotRPM = 4000;
        public static double kBaselineShotRPM = 3000;
        public static double kLongshotRPM = 7000;

        public static double kVelocityTolerance = 120;
    }

    public static final class ConveyorConstants {
        public static int kHorizontalMotorPort = 9;
        public static int kVerticalMotorPort = 10;
    }

    public static final class CameraConstants {
        public static double kCameraHeight = Units.inchesToMeters(27.25); // TODO: update this
        public static double kCameraMountingAngle = 22; // degrees
    }

    public static final class FieldConstants {
        public static double kGoalHeight = Units.inchesToMeters(98.25); // TODO: check this
    }

    public static final class ClimberConstants {
        public static double kGearRatio = 1.0;
        public static double kSprocketRadius = 1.0; // in

        public static double kP = 0.1;

        public static double kToleranceInches = 1;
    }

    public static final class AutoConstants {
        
    }
}
