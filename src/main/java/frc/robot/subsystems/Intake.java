package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
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
    public static enum Preset {
        HIGH(0),
        MID(0),
        LOW(0),
        // CONE and QUORB are substation positions
        CONE(0),
        QUORB(0),
        HOME(0);

        public double encoderPos;
        Preset(double encoderPos) {
            this.encoderPos = encoderPos;
        }
    }

    private CANSparkMax arm, intake;
    private State state;
    private GamePiece gamePiece;
    private SparkMaxPIDController armController;

    public static final int
        ARM_CURRENT_LIMIT = 20,
        INTAKE_CURRENT_LIMIT = 25,
        INTAKE_HOLD_CURRENT_LIMIT = 5;

    public static final double
        ARM_POWER = 0.4,
        INTAKE_OUTPUT_POWER = 0.85,
        INTAKE_HOLD_POWER = 0.07;

    public Intake() {
        arm = new CANSparkMax(15, MotorType.kBrushless);
        intake = new CANSparkMax(60, MotorType.kBrushless);
        // TODO - tune these and figure out if feedforward is necessary
        armController = arm.getPIDController();
        armController.setP(0.3);
        armController.setI(0);
        armController.setD(0);
        // armController.setReference(Preset.HOME.encoderPos, ControlType.kPosition);
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

    public void setArm(Preset preset, double trim) {
        // TODO - tune trim value
        armController.setReference(preset.encoderPos + 5 * trim, ControlType.kPosition);
    }

    public void setArm(Preset preset) {
        armController.setReference(preset.encoderPos, ControlType.kPosition);
    }

    public double getArmCurrent() {
        return arm.getOutputCurrent();
    }

    public RelativeEncoder getArmEncoder() {
        return arm.getEncoder();
    }

    public void setArm(double speed) {
        arm.set(speed);
    }

    @Override
    public void periodic() {
        if (gamePiece == GamePiece.CUBE) {
            intake.set(state == State.INTAKE ? INTAKE_OUTPUT_POWER : (state == State.HOLD ? INTAKE_HOLD_POWER : -INTAKE_OUTPUT_POWER));
        } else if (gamePiece == GamePiece.CONE) {
            intake.set(state == State.INTAKE ? -INTAKE_OUTPUT_POWER : (state == State.HOLD ? -INTAKE_HOLD_POWER : INTAKE_OUTPUT_POWER));
        } else {
            // System.out.println("doing nothing");
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
        builder.addDoubleProperty("Arm Current", () -> getArmCurrent(), null);
        builder.addDoubleProperty("Arm Position", arm.getEncoder()::getPosition, null);
        builder.addStringProperty("Game Piece", () -> gamePiece.toString(), null);
        builder.addStringProperty("State", () -> state.toString(), null);
    }
}
