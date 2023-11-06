package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeIOHardware implements IntakeIO {
    private final CANSparkMax intake, arm;

    public static final int
        ARM_CURRENT_LIMIT = 20,
        INTAKE_CURRENT_LIMIT = 25,
        INTAKE_HOLD_CURRENT_LIMIT = 5;


    public IntakeIOHardware() {
        intake = new CANSparkMax(60, MotorType.kBrushless);
        arm = new CANSparkMax(15, MotorType.kBrushless);
        arm.setInverted(true);
        arm.setIdleMode(IdleMode.kBrake);
        arm.setSmartCurrentLimit(ARM_CURRENT_LIMIT);
        intake.setInverted(false);
        intake.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void updateInputs(IntakeIOInputsAutoLogged inputs) {
        inputs.armCurrent = arm.getOutputCurrent();
        inputs.armPosition = arm.getEncoder().getPosition();
        inputs.armVelocity = arm.getEncoder().getVelocity();
        inputs.armSetSpeed = arm.get();
        inputs.intakeCurrent = intake.getOutputCurrent();
        inputs.intakePosition = intake.getEncoder().getPosition();
        inputs.intakeVelocity = intake.getEncoder().getVelocity();
        inputs.intakeSetSpeed = intake.get();
    }

    @Override
    public void setArm(double speed) {
        arm.set(speed);
    }

    @Override
    public void setIntake(double speed) {
        intake.set(speed);
    }

    @Override
    public void setIntakeCurrentLimit(int current) {
        intake.setSmartCurrentLimit(current);
    }
}
