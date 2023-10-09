package frc.robot.commands;

import com.summitrobotics.common.oi.inputs.OITrigger;
import com.summitrobotics.common.oi.inputs.OITrigger.PrioritizedTrigger;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;
import frc.robot.subsystems.Intake.Preset;
import frc.robot.subsystems.Intake.State;

public class IntakeDefault extends CommandBase {
    private PrioritizedTrigger take, eject, up, down, pieceToggle;
    // private PrioritizedAxis trim;
    private Intake intake;
    private Preset preset;
    private GamePiece gamePiece;
    
    public IntakeDefault(
        Intake intake,
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
        preset = Preset.HOME;
        gamePiece = GamePiece.CUBE;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.stop();
    }

    @Override
    public void execute() {
        // preset = low.get() ? Preset.LOW : mid.get() ? Preset.MID : high.get() ? Preset.HIGH : quorb.get() ? Preset.QUORB : cone.get() ? Preset.CONE : Preset.HOME;
        intake.setArm((up.get() ? 0.75 : 0) - (down.get() ? 0.25 : 0));
        if (pieceToggle.get()) {
            if (gamePiece == GamePiece.CONE) {
                gamePiece = GamePiece.CUBE;
            } else if (gamePiece == GamePiece.CUBE) {
                gamePiece = GamePiece.CONE;
            }
        }
        intake.setPiece(gamePiece);
        intake.setState(take.get() ? State.INTAKE : (eject.get() ? State.EJECT : State.HOLD));
    }

    @Override
    public void end(final boolean interrupted) {
        intake.stop();
        // trim.destroy();
        take.destroy();
        eject.destroy();
        pieceToggle.destroy();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
