package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.server.PathPlannerServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
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
import frc.robot.commands.autos.PathPlannerTest;
import frc.robot.commands.autos.TurnStressTest;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.intake.IntakeIOHardware;
import frc.robot.subsystems.intake.IntakeIOSim;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem.GamePiece;

public class RobotContainer {

    private final ControllerDriver controller;
    private final AHRS gyro;
    private final Drivetrain drivetrain;
    private final IntakeSubsystem intake;
    private final SwerveArcade arcadeDrive;
    private final IntakeDefault intakeDefault;
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {

        controller = new ControllerDriver(0);
        gyro = new AHRS();
        drivetrain = new Drivetrain(gyro);
        if (Robot.isReal()) {
            intake = new IntakeSubsystem(
                new IntakeIOHardware()
            );
        } else {
            intake = new IntakeSubsystem(
                new IntakeIOSim()
            );
        }
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
        // Sets drivetrain back to 0, reducing acumulated error
        drivetrain.setPose(new Pose2d(0, 0, new Rotation2d(Math.PI)));
        PathPlannerServer.startServer(5468);
    }

    public void robotPeriodic() {
        // SmartDashboard.putNumber("Time (seconds)", Timer.getFPGATimestamp());
    }

    private void initTelemetry() {
        SmartDashboard.putData("Drivetrain", drivetrain);
        SmartDashboard.putData("Auto Choice", autoChooser);
        SmartDashboard.putData("Controller", new Sendable() {
            @Override
            public void initSendable(SendableBuilder builder) {
                builder.addDoubleProperty("Left X", controller.leftX::get, null);     
                builder.addDoubleProperty("Left Y", controller.leftY::get, null);
            }
        });
    }

    private void createAutoCommands() {
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("Did nothing!"));
        autoChooser.addOption("Place and Move Quorb", new PlaceMove(drivetrain, intake, GamePiece.CUBE));
        autoChooser.addOption("Place and Move Cone", new PlaceMove(drivetrain, intake, GamePiece.CONE));
        autoChooser.addOption("Turn Stress Test", new TurnStressTest(drivetrain));
        autoChooser.addOption("PathPlannerTest", new PathPlannerTest(drivetrain));
    }

    public void autonomousInit() {}
    public void autonomousPeriodic() {}
    public void teleopInit() {}

    public void teleopPeriodic() {
        // System.out.println("Gyro rotation: " + drivetrain.getGyroscopeRotation());
    }
}
