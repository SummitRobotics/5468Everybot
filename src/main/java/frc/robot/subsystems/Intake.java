package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    public enum State {
        HOLD,
        INTAKE,
        EJECT;

        public String toString() {
            return this == HOLD ? "Hold" : this == INTAKE ? "INTAKE" : "Eject";
        }
    }

    public enum GamePiece {
        CONE,
        CUBE,
        NONE;

        public String toString() {
            return this == CONE ? "Cone" : this == CUBE ? "Cube" : "None";
        }
    }

    private CANSparkMax arm, intake;
    private State state;
    private GamePiece gamePiece;

    public static final int
        ARM_CURRENT_LIMIT = 20,
        INTAKE_CURRENT_LIMIT = 25,
        INTAKE_HOLD_CURRENT_LIMIT = 5;

    public static final double
        ARM_POWER = 0.4,
        INTAKE_OUTPUT_POWER = 1.0,
        INTAKE_HOLD_POWER = 0.07;

    public Intake() {
        arm = new CANSparkMax(5, MotorType.kBrushless);
        intake = new CANSparkMax(6, MotorType.kBrushless);
        state = State.INTAKE;
        gamePiece = GamePiece.NONE;

        arm.setInverted(true);
        arm.setIdleMode(IdleMode.kBrake);
        arm.setSmartCurrentLimit(ARM_CURRENT_LIMIT);
        intake.setInverted(false);
        intake.setIdleMode(IdleMode.kBrake);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    public void setArm(double speed) {
        arm.set(speed);
    }

    @Override
    public void periodic() {
        if (gamePiece == GamePiece.CUBE) {
            intake.set(state == State.INTAKE ? INTAKE_OUTPUT_POWER : state == State.HOLD ? INTAKE_HOLD_POWER : -INTAKE_OUTPUT_POWER);
        } else if (gamePiece == GamePiece.CONE) {
            intake.set(state == State.INTAKE ? -INTAKE_OUTPUT_POWER : state == State.HOLD ? -INTAKE_HOLD_POWER : INTAKE_OUTPUT_POWER);
        } else {
            intake.set(0);
        }
        intake.setSmartCurrentLimit(state == State.HOLD ? INTAKE_HOLD_CURRENT_LIMIT : INTAKE_CURRENT_LIMIT);
    }

    public void stop() {
        gamePiece = GamePiece.NONE;
        arm.set(0);
        intake.set(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Intake Velocity", intake.getEncoder()::getVelocity, null);
        builder.addDoubleProperty("Arm Position", arm.getEncoder()::getPosition, null);
        builder.addStringProperty("Game Piece", gamePiece::toString, null);
        builder.addStringProperty("State", state::toString, null);
    }
}
