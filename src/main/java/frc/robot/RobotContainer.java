package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import com.summitrobotics.common.commands.SwerveArcade;
import com.summitrobotics.common.oi.drivers.ControllerDriver;
import frc.robot.commands.IntakeDefault;
import frc.robot.commands.autos.PlaceMove;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.GamePiece;

public class RobotContainer {

    private final ControllerDriver controller;
    private final AHRS gyro;
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final SwerveArcade arcadeDrive;
    private final IntakeDefault intakeDefault;
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {

        controller = new ControllerDriver(0);
        gyro = new AHRS();
        drivetrain = new Drivetrain(gyro);
        intake = new Intake();
        autoChooser = new SendableChooser<Command>();

        arcadeDrive = new SwerveArcade(
            drivetrain,
            gyro,
            controller.leftY,
            controller.leftX,
            controller.rightX,
            controller.dPadDown,
            controller.buttonA,
            controller.dPadUp
        );

        intakeDefault = new IntakeDefault(
            intake,
            controller.leftBumper,
            controller.rightBumper,
            controller.buttonB,
            controller.buttonX,
            controller.buttonY
        );

        initTelemetry();
        setDefaultCommands();
        createAutoCommands();
    }

    private void setDefaultCommands() {
        drivetrain.setDefaultCommand(arcadeDrive);
        intake.setDefaultCommand(intakeDefault);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void robotInit() {
        gyro.calibrate();
        gyro.reset();
        gyro.setAngleAdjustment(180);
        // Sets drivetrain back to 0, reducing acumulated error
        drivetrain.setPose(new Pose2d(0, 0, new Rotation2d(Math.PI)));
    }

    public void robotPeriodic() {
        // SmartDashboard.putNumber("Time (seconds)", Timer.getFPGATimestamp());
    }

    private void initTelemetry() {
        SmartDashboard.putData("Drivetrain", drivetrain);
        SmartDashboard.putData("Controller", new Sendable() {
            @Override
            public void initSendable(SendableBuilder builder) {
                builder.addDoubleProperty("Right X", controller.rightX::get, null);
                builder.addDoubleProperty("Right Y", controller.rightY::get, null);
                builder.addDoubleProperty("Left X", controller.leftX::get, null);
                builder.addDoubleProperty("Left Y", controller.leftY::get, null);
            }
        });
        SmartDashboard.putData("Intake", intake);
        SmartDashboard.putData("Auto Choice", autoChooser);
    }

    private void createAutoCommands() {
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("Did nothing!"));
        autoChooser.addOption("Place and Move Quorb", new PlaceMove(drivetrain, intake, GamePiece.CUBE));
        autoChooser.addOption("Place and Move Cone", new PlaceMove(drivetrain, intake, GamePiece.CONE));
    }

    public void autonomousInit() {}
    public void autonomousPeriodic() {}
    public void teleopInit() {
        // drivetrain.drive(new ChassisSpeeds(0, 0.25, 0));
    }
    public void teleopPeriodic() {
        // System.out.println("Gyro rotation: " + drivetrain.getGyroscopeRotation());
    }
}
