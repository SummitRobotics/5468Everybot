package frc.robot;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import com.summitrobotics.common.commands.SwerveArcade;
import com.summitrobotics.common.oi.drivers.ControllerDriver;
import frc.robot.commands.IntakeDefault;
import frc.robot.commands.autos.PlaceMove;
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
        // AdvantageKit setup, per instructions at https://github.com/Mechanical-Advantage/AdvantageKit/blob/main/docs/INSTALLATION.md
        Logger.getInstance().recordMetadata("ProjectName", "5468Everybot"); // Set a metadata value

        if (Robot.isReal()) {
            Logger.getInstance().addDataReceiver(new WPILOGWriter("/U")); // Log to a USB stick
            Logger.getInstance().addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
        } else {
            String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
            Logger.getInstance().setReplaySource(new WPILOGReader(logPath)); // Read replay log
            Logger.getInstance().addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
        }

        Logger.getInstance().start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.

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
        SmartDashboard.putData("Auto Choice", autoChooser);
    }

    private void createAutoCommands() {
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("Did nothing!"));
        autoChooser.addOption("Place and Move Quorb", new PlaceMove(drivetrain, intake, GamePiece.CUBE));
        autoChooser.addOption("Place and Move Cone", new PlaceMove(drivetrain, intake, GamePiece.CONE));
        autoChooser.addOption("Turn Stress Test", new TurnStressTest(drivetrain));
    }

    public void autonomousInit() {}
    public void autonomousPeriodic() {}
    public void teleopInit() {}

    public void teleopPeriodic() {
        // System.out.println("Gyro rotation: " + drivetrain.getGyroscopeRotation());
    }
}
