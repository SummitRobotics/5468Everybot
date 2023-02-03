package frc.robot.commands.homing;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utilities.RollingAverage;
import frc.robot.utilities.homing.Homeable;

/**
 * Class which allows a subsystem to home a mechanism by looking for current spikes.
 */
public class HomeByCurrent extends CommandBase {

    private final boolean limits;
    private final Homeable toHome;
    private final double homingPower;
    private final double currentThreshold;
    private double reverseLimit;
    private double forwardLimit;

    private final RollingAverage currentAverage = new RollingAverage(10, false);

    /**
     * Creates a new HomeByCurrent.
     *
     * @param toHome subsystem to home.
     * @param homingPower power to home with.
     * @param currentThreshold Amount of current till it is determined
     *                         that it has reached the hard stop.
     */
    public HomeByCurrent(Homeable toHome, double homingPower, double currentThreshold) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.currentThreshold = currentThreshold;

        limits = false;

        addRequirements(toHome.getSubsystemObject());
    }

    /**
     * The Constructor.
     *
     * @param toHome the subsystem to home
     * @param homingPower the motor power [-1, 1] used to move the object into the hard stop
     * @param currentThreshold the amount of amps the motor needs to draw before the motor is considered homed
     * @param reversLimit the encoder revs that should be the min before the motor stops
     * @param forwardLimit the encoder revs that should be the max before the motor stops
     */
    public HomeByCurrent(
            Homeable toHome,
            double homingPower,
            double currentThreshold,
            double reversLimit,
            double forwardLimit) {
        this.toHome = toHome;
        this.homingPower = homingPower;
        this.currentThreshold = currentThreshold;
        this.reverseLimit = reversLimit;
        this.forwardLimit = forwardLimit;

        limits = true;

        addRequirements(toHome.getSubsystemObject());
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        currentAverage.reset();

        // System.out.println("running");
        toHome.disableSoftLimits();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("homing current: " + toHome.getCurrent());
        toHome.setPower(homingPower);
        currentAverage.update(toHome.getCurrent());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        toHome.setPower(0);
        // prints out homing completed message
        System.out.println("homing of " + toHome.getSubsystemObject().getClass().getCanonicalName() + " ended with interrupted " + interrupted);
        if (!interrupted) {
            toHome.setHome(0);
            if (limits) {
                toHome.setSoftLimits(reverseLimit, forwardLimit);
                toHome.enableSoftLimits();
            }
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        double current = currentAverage.getAverage();
        return current >= currentThreshold;
    }
}
