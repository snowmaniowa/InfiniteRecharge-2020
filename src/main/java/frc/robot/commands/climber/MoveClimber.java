package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class MoveClimber extends CommandBase {

    private Climber climber;
    private ClimberMotionType climberMotionType;

    public MoveClimber(Climber climber, ClimberMotionType climberMotionType){
        addRequirements(climber);
        this.climber = climber;
        this.climberMotionType = climberMotionType;
    }

    @Override
    public void execute() {
        switch (climberMotionType){
            case AUTO_EXTEND:
                climber.setClimberLock(false);
                climber.setToTargetInches(10);
                break;
            case AUTO_LOCK:
                climber.setClimberLock(true);
                break;
            case MANUAL_EXTEND:
                climber.setClimberLock(false);
                climber.setClimberUp();
                break;
            case MANUAL_RETRACT:
                climber.setClimberLock(false);
                climber.setClimberDown();
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return climber.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {

    }

    public enum ClimberMotionType {
        MANUAL_EXTEND,
        MANUAL_RETRACT,
        AUTO_EXTEND,
        AUTO_LOCK,
    }

}