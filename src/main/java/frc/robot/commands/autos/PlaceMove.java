package frc.robot.commands.autos;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;
import frc.robot.subsystems.Intake.Preset;
import frc.robot.subsystems.Intake.State;

public class PlaceMove extends SequentialCommandGroup {
    public PlaceMove(Drivetrain drivetrain, Intake intake, GamePiece piece) {
        addCommands(
            new InstantCommand(() -> {
                intake.setPiece(piece);
                intake.setState(State.HOLD);
                intake.setArm(Preset.HIGH);
            }),
            new WaitCommand(1),
            new InstantCommand(() -> {
                intake.setState(State.EJECT);
                intake.setArm(Preset.HOME);
                drivetrain.drive(new ChassisSpeeds(0, -1, 0));
            }),
            new WaitCommand(1),
            new InstantCommand(drivetrain::stop, drivetrain)
        );
    }
}
