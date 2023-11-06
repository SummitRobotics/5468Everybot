package frc.robot.commands;

import com.summitrobotics.common.oi.inputs.OITrigger;
import com.summitrobotics.common.oi.inputs.OITrigger.PrioritizedTrigger;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.GamePiece;
import frc.robot.subsystems.intake.IntakeSubsystem.State;

public class IntakeDefault extends CommandBase {
    private PrioritizedTrigger take, eject, up, down, pieceToggle;
    private IntakeSubsystem intake;
    
    public IntakeDefault(
        IntakeSubsystem intake,
        OITrigger up,
        OITrigger down,
        OITrigger pieceToggle,
        OITrigger take,
        OITrigger eject
    ) {
        this.intake = intake;
        this.up = up.prioritize(0);
        this.down = down.prioritize(0);
        this.take = take.prioritize(0);
        this.eject = eject.prioritize(0);
        this.pieceToggle = pieceToggle.prioritize(0);
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.stop();
    }

    @Override
    public void execute() {
        intake.setArm((up.get() ? 0.75 : 0) - (down.get() ? 0.25 : 0));
        GamePiece gamePiece = intake.getPiece();
        if (pieceToggle.get()) {
            if (intake.getPiece() == GamePiece.CONE) {
                gamePiece = GamePiece.CUBE;
            } else if (intake.getPiece() == GamePiece.CUBE) {
                gamePiece = GamePiece.CONE;
            }
        }
        intake.setPiece(gamePiece);
        intake.setState(take.get() ? State.INTAKE : (eject.get() ? State.EJECT : State.HOLD));
    }

    @Override
    public void end(final boolean interrupted) {
        intake.stop();
        take.destroy();
        eject.destroy();
        pieceToggle.destroy();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
