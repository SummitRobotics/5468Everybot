package frc.robot.commands;

import com.summitrobotics.common.oi.inputs.OITrigger;
import com.summitrobotics.common.oi.inputs.OITrigger.PrioritizedTrigger;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;
import frc.robot.subsystems.Intake.State;

public class IntakeDefault extends CommandBase {
    PrioritizedTrigger lower, raise, funCube, funCone, take, eject;
    Intake intake;
    
    public IntakeDefault(
        Intake intake,
        OITrigger lower,
        OITrigger raise,
        OITrigger funCone,
        OITrigger funCube,
        OITrigger take,
        OITrigger eject
    ) {
        this.intake = intake;
        this.lower = lower.prioritize(0);
        this.raise = raise.prioritize(0);
        this.funCone = funCone.prioritize(0);
        this.funCube = funCube.prioritize(0);
        this.take = take.prioritize(0);
        this.eject = eject.prioritize(0);
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.stop();
    }

    @Override
    public void execute() {
        intake.setArm(lower.get() ? -Intake.ARM_POWER : raise.get() ? Intake.ARM_POWER : 0);
        intake.setPiece(funCone.get() ? GamePiece.CONE : funCube.get() ? GamePiece.CUBE : GamePiece.NONE);
        intake.setState(take.get() ? State.INTAKE : eject.get() ? State.EJECT : State.HOLD);
    }

    @Override
    public void end(final boolean interrupted) {
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
