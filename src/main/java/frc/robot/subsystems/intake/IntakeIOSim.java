package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;

@SuppressWarnings("unused")
public class IntakeIOSim implements IntakeIO {
    // TODO - finish this
    // Is the Everybot mechanically similar enough to be represented by the single-jointed arm model?
    private SingleJointedArmSim armSim = new SingleJointedArmSim(
        DCMotor.getNEO(1), 
        1.0, 
        0.0, 
        0.0, 
        0.0, 
        0.0, 
        true
    );

    public IntakeIOSim() {}
}
