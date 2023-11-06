package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    /** Inputs and setters from intake hardware, physical or simulated */
    @AutoLog
    public static class IntakeIOInputs {
        public double intakeCurrent = 0.0;
        public double intakeVelocity = 0.0;
        public double intakePosition = 0.0;
        public double intakeSetSpeed = 0.0;
        public double armCurrent = 0.0;
        public double armVelocity = 0.0;
        public double armPosition = 0.0;
        public double armSetSpeed = 0.0;
    }

    public default void updateInputs(IntakeIOInputsAutoLogged inputs) {}

    public default void setArm(double speed) {}

    public default void setIntake(double speed) {}

    public default void setIntakeCurrentLimit(int current) {}
}
