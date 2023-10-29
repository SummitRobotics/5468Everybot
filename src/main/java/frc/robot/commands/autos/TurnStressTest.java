package frc.robot.commands.autos;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class TurnStressTest extends SequentialCommandGroup {
    public TurnStressTest(Drivetrain drivetrain) {
        addCommands(
            new InstantCommand(() -> {
                double angle = 0;
                double speed = 0.25;
                if ((int) Timer.getFPGATimestamp() % 2 == 0) {
                   angle = 1.5 * Math.PI;
                } else {
                    angle = 0;
                }
                drivetrain.drive(new ChassisSpeeds(speed * Math.cos(angle), speed * Math.sin(angle), 0));
            }).repeatedly()
        );
        addRequirements(drivetrain);
    }
}
