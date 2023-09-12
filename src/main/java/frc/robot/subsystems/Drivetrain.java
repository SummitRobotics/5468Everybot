package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import com.summitrobotics.common.swerve.Swerve;
import com.summitrobotics.common.swerve.SwerveConstellation;
import com.summitrobotics.common.swerve.SwerveModule;
import com.summitrobotics.common.swerve.SwerveModuleBuilder;
import com.summitrobotics.common.swerve.SwerveModuleBuilder.SWERVE_MODULE_PRESETS;

public class Drivetrain extends Swerve {
  private SwerveConstellation constellation;
  public SwerveModule mod0, mod1, mod2, mod3;
  private AHRS gyro;

  /** Creates a new Drivetrain. */
  public Drivetrain(AHRS gyro) {
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 3);
    double[] drivePid = new double[]{0.1,0,0};

    mod0 = new SwerveModuleBuilder(new Translation2d(67.6 / 2, 58.7 / 2), SWERVE_MODULE_PRESETS.SDS_MK4i_L2)
      .driveNEO1650(16).turnNEO1650(15).CANCoder(25, -2.6215732
      ).driveFeedforward(feedforward).drivePID(drivePid).build();
    mod1 = new SwerveModuleBuilder(new Translation2d(-67.6 / 2, 58.7 / 2), SWERVE_MODULE_PRESETS.SDS_MK4i_L2)
      .driveNEO1650(14).turnNEO1650(13).CANCoder(23, 2.630777).driveFeedforward(feedforward).drivePID(drivePid).build();
    mod2 = new SwerveModuleBuilder(new Translation2d(67.6 / 2, -58.7 / 2), SWERVE_MODULE_PRESETS.SDS_MK4i_L2)
      .driveNEO1650(18).turnNEO1650(17).CANCoder(27, -1.20893).driveFeedforward(feedforward).drivePID(drivePid).build();
    mod3 = new SwerveModuleBuilder(new Translation2d(-67.6 / 2, -58.7 / 2), SWERVE_MODULE_PRESETS.SDS_MK4i_L2)
      .driveNEO1650(12).turnNEO1650(11).CANCoder(21, 0.050602).driveFeedforward(feedforward).drivePID(drivePid).build();
    
    constellation = new SwerveConstellation(mod0, mod1, mod2, mod3);
    this.gyro = gyro;
  }

  @Override
  public SwerveConstellation getConstellation() {
    return constellation;
  }

  @Override
  public Rotation2d getGyroscopeRotation() {
    return gyro.getRotation2d().unaryMinus();
  }

  public void stop() {
    this.drive(new ChassisSpeeds(0, 0, 0));
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    // For AdvantageScope swerve visualizer; see https://github.com/Mechanical-Advantage/AdvantageScope/blob/main/docs/tabs/SWERVE.md.
    builder.addDoubleArrayProperty("Module States", () -> new double[] {
      mod0.getState().angle.getRadians(), mod0.getState().speedMetersPerSecond,
      mod1.getState().angle.getRadians(), mod1.getState().speedMetersPerSecond,
      mod2.getState().angle.getRadians(), mod2.getState().speedMetersPerSecond,
      mod3.getState().angle.getRadians(), mod3.getState().speedMetersPerSecond,
    }, null);
  }
}
