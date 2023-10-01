package frc.robot.commands.autos;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.commands.autos.ChargeBalance.BalanceDirection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;
import frc.robot.subsystems.Intake.Preset;
import frc.robot.subsystems.Intake.State;

public class PlaceMoveBalance extends SequentialCommandGroup {
    public PlaceMoveBalance(Drivetrain drivetrain, Intake intake, AHRS gyro, GamePiece piece) {
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
            new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) > 8),
            new WaitCommand(0.5),
            new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) < 8),
            new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) > 10),
            new WaitCommand(0.25),
            new InstantCommand(drivetrain::stop, drivetrain),
            new ChargeBalance(drivetrain, gyro, BalanceDirection.FORWARD)
        );
    }
}
