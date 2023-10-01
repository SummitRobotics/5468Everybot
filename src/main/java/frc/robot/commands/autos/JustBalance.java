package frc.robot.commands.autos;

import com.kauailabs.navx.frc.AHRS;
import com.summitrobotics.common.utilities.Functions;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class JustBalance extends CommandBase {
    private final double P = 0.005, I = 0, D = 0;
   
    private final Drivetrain drivetrain;
    private final AHRS gyro;
    private final PIDController controller;

    public JustBalance(Drivetrain drivetrain, AHRS gyro) {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.controller = new PIDController(P, I, D);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        controller.reset();
        controller.setTolerance(0.25, 1);
        controller.setSetpoint(0);
    }

    @Override
    public void execute() {
        double power = -Functions.clampDouble(controller.calculate(-gyro.getRoll()), 0.5, -0.5);
        drivetrain.drive(new ChassisSpeeds(0, power, 0));
    }

    @Override
    public void end(final boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
