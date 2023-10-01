package frc.robot.commands;

import java.util.function.Consumer;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

/**
 * Command to have the swerve drivetrain follow a PathPlannerLib trajectory.
 * This is based off of example code from https://github.com/mjansen4857/pathplanner/wiki/PathPlannerLib:-Java-Usage.
 */
public class FollowPathPlannerTrajectory extends SequentialCommandGroup {
    public FollowPathPlannerTrajectory(Drivetrain drivetrain, PathPlannerTrajectory traj, boolean resetPose, boolean allianceMirror) {
        addCommands(
            // Pose should be reset to the start point if this is the first trajectory to run
            new InstantCommand(() -> {
              if (resetPose) {
                if (allianceMirror) {
                    drivetrain.setPose(PathPlannerTrajectory.transformTrajectoryForAlliance(traj, DriverStation.getAlliance()).getInitialPose());
                } else {
                    drivetrain.setPose(traj.getInitialPose());
                }
              }
            }),
            new PPSwerveControllerCommand(
                traj,
                drivetrain::getPose,
                drivetrain.getConstellation().kinematics,
                // TODO - tune these
                new PIDController(0, 0, 0), // X controller. Tune these values for your robot. Leaving them 0 will only use feedforwards.
                new PIDController(0, 0, 0), // Y controller (usually the same values as X controller)
                new PIDController(0, 0, 0), // Rotation controller. Tune these values for your robot. Leaving them 0 will only use feedforwards.
                new Consumer<SwerveModuleState[]>() {
                    @Override
                    public void accept(SwerveModuleState[] states) {
                        drivetrain.getConstellation().setModuleStates(states);
                    }
                },
                allianceMirror, // Should the path be automatically mirrored depending on alliance color? Optional, defaults to true
                drivetrain // Requires the drive subsystem
            )
        );
    }
}
