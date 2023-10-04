package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import com.summitrobotics.common.commands.SwerveArcade;
import com.summitrobotics.common.oi.drivers.ControllerDriver;
import com.summitrobotics.common.oi.drivers.LaunchpadDriver;
import frc.robot.commands.IntakeDefault;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class RobotContainer {

    private final ControllerDriver controller;
    private final LaunchpadDriver launchpad;
    private final AHRS gyro;
    private final Drivetrain drivetrain;
    // private final Intake intake;
    private final SwerveArcade arcadeDrive;
    // private final IntakeDefault intakeDefault;
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {

        controller = new ControllerDriver(0);
        launchpad = new LaunchpadDriver(1);
        gyro = new AHRS();
        drivetrain = new Drivetrain(gyro);
        // intake = new Intake();
        autoChooser = new SendableChooser<Command>();

        arcadeDrive = new SwerveArcade(
            drivetrain,
            controller.leftY,
            controller.leftX,
            controller.rightX,
            controller.dPadDown,
            controller.buttonA,
            controller.dPadUp
        );

        // intakeDefault = new IntakeDefault(
            // intake,
            // controller.rightY,
            // launchpad.funLeft,
            // launchpad.funRight,
            // controller.buttonX,
            // controller.buttonY,
            // launchpad.buttonA,
            // launchpad.buttonB,
            // launchpad.buttonC,
            // launchpad.buttonD,
            // launchpad.buttonE
        // );

        initTelemetry();
        setDefaultCommands();
        createAutoCommands();
    }

    private void setDefaultCommands() {
        // drivetrain.setDefaultCommand(arcadeDrive);
        // intake.setDefaultCommand(intakeDefault);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void robotInit() {
        gyro.calibrate();
        // Sets drivetrain back to 0, reducing acumulated error
        drivetrain.setPose(new Pose2d());
    }

    public void robotPeriodic() {
        // SmartDashboard.putNumber("Time (seconds)", Timer.getFPGATimestamp());
    }

    private void initTelemetry() {
        SmartDashboard.putData("Drivetrain", drivetrain);
        // SmartDashboard.putData("Intake", intake);
        // SmartDashboard.putData("Auto Choice", autoChooser);
    }

    private void createAutoCommands() {
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("Did nothing!"));
    }

    public void autonomousInit() {}
    public void autonomousPeriodic() {}
    public void teleopInit() {
        drivetrain.drive(new ChassisSpeeds(0, 0.25, 0));
    }
    public void teleopPeriodic() {}
}
