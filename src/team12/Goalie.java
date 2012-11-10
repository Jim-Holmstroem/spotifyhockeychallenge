package team12;

import hockey.api.GoalKeeper;
import hockey.api.Position;
import hockey.api.Util;
import hockey.model.Puck;

public class Goalie extends GoalKeeper {
	// How far will the goalie move?
	private static final int RADIUS = 90;
	private static final double MAX_THETA = 90.0 - Math.toDegrees(Math.asin(4.0 / 9.0)),
									SPEED_SCALE = 2.0;
	
    // Middle of our own goalcage, on the goal line
    protected static final Position GOAL_POSITION = new Position(-2600, 0);

    // Number of the goalie.
    public int getNumber() { return 1; }

    // Name of the goalie.
    public String getName() { return "Alan Sheep"; }

    // Left handed goalie
    public boolean isLeftHanded() { return true; }

    // Initiate
    public void init() { }

    // Face off
    public void faceOff() { }

    // Called when the goalie is about to receive a penalty shot
    public void penaltyShot() { }

    // Intelligence of goalie.
    public void step() {
	/*skate(GOAL_POSITION.getX() + 50, GOAL_POSITION.getY(), 200);
	turn(getPuck(), MAX_TURN_SPEED);*/
    	
    	Tactics.step(Team.s_players[0]);
    	
    	if (hasPuck()) {
    		shoot(Goalie.GOAL_POSITION.getX() + 300, (int) (Math.signum(getY()) * 1500), MAX_SHOT_SPEED);
    	}
    	else {
	    	double theta = Util.clamp(Math.toDegrees(Math.atan2((getPuck().getY() - GOAL_POSITION.getY()), (getPuck().getX() - GOAL_POSITION.getX()))), -MAX_THETA, MAX_THETA);
	    
	    	int target_x = GOAL_POSITION.getX() + (int) (RADIUS * Util.cosd(theta)),
	    		target_y = GOAL_POSITION.getY() + (int) (RADIUS * Util.sind(theta)),
	    		current_x = getX(), current_y = getY();
	    	
	    	double length = Math.sqrt((current_x - target_x) * (current_x - target_x) + (current_y - target_y) * (current_y - target_y));
	    	
	    	skate(target_x, target_y, Math.min((int) (length * SPEED_SCALE), 200));
	    
	    	turn(getPuck(), MAX_TURN_SPEED);	
    	}
    	
    }
}
