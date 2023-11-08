package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

    public static enum State {
        HOLD,
        INTAKE,
        EJECT;

        public String toString() {
            return this == HOLD ? "Hold" : (this == INTAKE ? "INTAKE" : "Eject");
        }
    }

    public static enum GamePiece {
        CONE,
        CUBE,
        NONE;

        public String toString() {
            return this == CONE ? "Cone" : (this == CUBE ? "Cube" : "None");
        }
    }

    // TODO - tune these values
    // public static enum Preset {
        // HIGH(0),
        // MID(0),
        // LOW(0),
        // CONE and QUORB are substation positions
        // CONE(0),
        // QUORB(0),
        // HOME(0);

        // public double encoderPos;
        // Preset(double encoderPos) {
            // this.encoderPos = encoderPos;
        // }
    // }

    private final IntakeIO io;
    private final IntakeIOInputsAutoLogged inputs;
    private State state;
    private GamePiece gamePiece;

    public static final int
        ARM_CURRENT_LIMIT = 20,
        INTAKE_CURRENT_LIMIT = 25,
        INTAKE_HOLD_CURRENT_LIMIT = 5;

    public static final double
        ARM_POWER = 0.4,
        INTAKE_OUTPUT_POWER = 0.85,
        INTAKE_HOLD_POWER = 0.07;

    public IntakeSubsystem(IntakeIO io) {
        this.io = io;
        inputs = new IntakeIOInputsAutoLogged();
        state = State.INTAKE;
        gamePiece = GamePiece.NONE;

    }

    public void setState(State state) {
        this.state = state;
    }

    public void setArm(double speed) {
        io.setArm(speed);
    }

    public void setPiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    public GamePiece getPiece() {
        return gamePiece;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Intake", inputs);
        Logger.getInstance().recordOutput("Intake/State", state.toString());
        Logger.getInstance().recordOutput("Intake/GamePiece", gamePiece.toString());

        if (gamePiece == GamePiece.CUBE) {
            io.setIntake(state == State.INTAKE ? INTAKE_OUTPUT_POWER : (state == State.HOLD ? INTAKE_HOLD_POWER : -INTAKE_OUTPUT_POWER));
        } else if (gamePiece == GamePiece.CONE) {
            io.setIntake(state == State.INTAKE ? -INTAKE_OUTPUT_POWER : (state == State.HOLD ? -INTAKE_HOLD_POWER : INTAKE_OUTPUT_POWER));
        } else {
            io.setIntake(0);
        }
        io.setIntakeCurrentLimit(state == State.HOLD ? INTAKE_HOLD_CURRENT_LIMIT : INTAKE_CURRENT_LIMIT);
    }

    public void stop() {
        gamePiece = GamePiece.NONE;
        io.setArm(0);
        io.setIntake(0);
    }
}
