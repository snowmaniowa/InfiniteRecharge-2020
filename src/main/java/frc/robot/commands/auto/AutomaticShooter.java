package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;

public class AutomaticShooter extends CommandBase {

    private Conveyor conveyor;
    private Shooter shooter;
    private double rpm;

    public AutomaticShooter(Shooter shooter, Conveyor conveyor, double rpm){
        this.conveyor = conveyor;
        this.shooter = shooter;
        this.rpm = rpm;
    }

    @Override
    public void initialize() {
        shooter.setSparkMaxVelocity(rpm);
        shooter.setTargetRpm(rpm);
    }

    @Override
    public void execute() {
        conveyor.setIntaking(true);
        conveyor.setExtaking(true);
        if (shooter.atSetpoint()) {
            conveyor.horizontalConveyor.set(0.35);
            conveyor.verticalConveyor.set(0.5);
        } else {
            conveyor.horizontalConveyor.set(0);
            conveyor.verticalConveyor.set(0);
        }
    }


    @Override
    public void end(boolean interrupted) {
        conveyor.setIntaking(true);
        conveyor.setExtaking(true);
        shooter.setShooterPower(0);
        conveyor.horizontalConveyor.set(0);
        conveyor.verticalConveyor.set(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
