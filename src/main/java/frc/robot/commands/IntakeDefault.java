package frc.robot.commands;

import com.summitrobotics.common.oi.inputs.OIAxis;
import com.summitrobotics.common.oi.inputs.OITrigger;
import com.summitrobotics.common.oi.inputs.OIAxis.PrioritizedAxis;
import com.summitrobotics.common.oi.inputs.OITrigger.PrioritizedTrigger;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;
import frc.robot.subsystems.Intake.Preset;
import frc.robot.subsystems.Intake.State;

public class IntakeDefault extends CommandBase {
    private PrioritizedTrigger funQuorb, funCone, take, eject, low, mid, high, quorb, cone;
    private PrioritizedAxis trim;
    private Intake intake;
    private Preset preset;
    
    public IntakeDefault(
        Intake intake,
        OIAxis trim,
        OITrigger funCone,
        OITrigger funQuorb,
        OITrigger take,
        OITrigger eject,
        OITrigger low,
        OITrigger mid,
        OITrigger high,
        OITrigger quorb,
        OITrigger cone
    ) {
        this.intake = intake;
        this.trim = trim.prioritize(0);
        this.funCone = funCone.prioritize(0);
        this.funQuorb = funQuorb.prioritize(0);
        this.take = take.prioritize(0);
        this.eject = eject.prioritize(0);
        this.low = low.prioritize(0);
        this.mid = mid.prioritize(0);
        this.high = high.prioritize(0);
        this.quorb = quorb.prioritize(0);
        this.cone = cone.prioritize(0);
        preset = Preset.HOME;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.stop();
    }

    @Override
    public void execute() {
        preset = low.get() ? Preset.LOW : mid.get() ? Preset.MID : high.get() ? Preset.HIGH : quorb.get() ? Preset.QUORB : cone.get() ? Preset.CONE : Preset.HOME;
        intake.setArm(preset, trim.get());
        intake.setPiece(funCone.get() ? GamePiece.CONE : funQuorb.get() ? GamePiece.CUBE : GamePiece.NONE);
        intake.setState(take.get() ? State.INTAKE : eject.get() ? State.EJECT : State.HOLD);
    }

    @Override
    public void end(final boolean interrupted) {
        intake.stop();
        trim.destroy();
        funCone.destroy();
        funQuorb.destroy();
        take.destroy();
        eject.destroy();
        low.destroy();
        mid.destroy();
        high.destroy();
        quorb.destroy();
        cone.destroy();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
