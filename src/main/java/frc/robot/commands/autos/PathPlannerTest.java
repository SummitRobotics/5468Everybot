package frc.robot.commands.autos;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.summitrobotics.common.commands.FollowPathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class PathPlannerTest extends SequentialCommandGroup {
    public PathPlannerTest(Drivetrain drivetrain) {
        PathPlannerTrajectory traj = PathPlanner.loadPath("Test", new PathConstraints(0.75, 0.5));
        addCommands(
            new FollowPathPlannerTrajectory(drivetrain, traj, false, false, new double[] {5, 0, 0}, new double[] {5, 0, 0})
        );
    }
    
}
