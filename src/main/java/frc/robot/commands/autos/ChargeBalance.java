package frc.robot.commands.autos;

import java.util.Map;
import java.util.function.Supplier;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Drivetrain;

public class ChargeBalance extends SequentialCommandGroup {
    public enum BalanceDirection {
        FORWARD, BACKWARD
    }

    private final static double INIT_SPEED = 1, RAMP_SPEED = 0.4, TUNE_SPEED = 0.4;

    public ChargeBalance(Drivetrain drivetrain, AHRS gyro, BalanceDirection direction) {
        this(drivetrain, gyro,() -> direction);
    }

    public ChargeBalance(Drivetrain drivetrain, AHRS gyro, Supplier<BalanceDirection> direction) {
        addCommands(
            new ParallelRaceGroup(
                new SelectCommand(Map.ofEntries(
                    Map.entry(BalanceDirection.FORWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, INIT_SPEED, 0)), drivetrain).repeatedly()),
                    Map.entry(BalanceDirection.BACKWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, -INIT_SPEED, 0)), drivetrain).repeatedly())
                ), direction::get),
                new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) > 12)
            ),
            new WaitCommand(1.25),
            new ParallelRaceGroup(
                new SelectCommand(Map.ofEntries(
                    Map.entry(BalanceDirection.FORWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, RAMP_SPEED, 0)), drivetrain).repeatedly()),
                    Map.entry(BalanceDirection.BACKWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, -RAMP_SPEED, 0)), drivetrain).repeatedly())
                ), direction::get),
                new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) < 12)
            ),
            new ParallelRaceGroup(
                new SelectCommand(Map.ofEntries(
                    Map.entry(BalanceDirection.FORWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, -TUNE_SPEED, 0)), drivetrain).repeatedly()),
                    Map.entry(BalanceDirection.BACKWARD, new InstantCommand(() -> drivetrain.drive(new ChassisSpeeds(0, TUNE_SPEED, 0)), drivetrain).repeatedly())
                ), direction::get),
                new WaitUntilCommand(() -> Math.abs(gyro.getRoll()) < 2)
            ),
            new JustBalance(drivetrain, gyro),
            new InstantCommand(drivetrain::stop, drivetrain)
        );
    }
}
