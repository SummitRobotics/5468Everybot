package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.oi.drivers.ControllerDriver;
import frc.robot.oi.drivers.LaunchpadDriver;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.lists.Ports;

public class RobotContainer {

  private final CommandScheduler scheduler;
  private final ControllerDriver controller;
  private final LaunchpadDriver launchpad;
  private final AHRS gyro;
  private final Drivetrain drivetrain;
  private final ArcadeDrive arcadeDrive;

  public RobotContainer() {

    scheduler = CommandScheduler.getInstance();
    controller = new ControllerDriver(Ports.OI.XBOX_PORT);
    launchpad = new LaunchpadDriver(Ports.OI.LAUNCHPAD_PORT);
    gyro = new AHRS();
    drivetrain = new Drivetrain(gyro);

    arcadeDrive = new ArcadeDrive(
      drivetrain,
      controller.rightTrigger,
      controller.leftTrigger,
      controller.leftX,
      controller.dPadAny
    );

    setDefaultCommands();
    configureBindings();
  }

  private void setDefaultCommands() {
    drivetrain.setDefaultCommand(arcadeDrive);
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  public void robotInit() {
    gyro.calibrate();
    //sets drivetrain back to 0, reducing acumulated error and bad stuff
    drivetrain.setPose(new Pose2d());
}
}
