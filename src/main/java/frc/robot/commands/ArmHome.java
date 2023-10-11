package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class ArmHome extends CommandBase {
    Intake intake;
    
    public ArmHome(Intake intake) {
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.setArm(0.5);
    }

    @Override
    public boolean isFinished() {
        return intake.getArmCurrent() > 3;
    }

    @Override
    public void end(final boolean interrupted) {
        intake.setArm(0);
        intake.getArmEncoder().setPosition(0);
    }
}
