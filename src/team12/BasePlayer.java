package team12;

import java.awt.Color;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import hockey.api.IPlayer;
import hockey.api.IPuck;
import hockey.api.Position;
import hockey.api.Player;
import hockey.api.Util;

public abstract class BasePlayer extends Player {
    // The middle of the opponents goal, on the goal line
    protected static final Position GOAL_POSITION = new Position(2600, 0);
    static final int GOAL_RADIUS = 90;

    // Left handed?
    public boolean isLeftHanded() { return false; }

    // Initiate
    public void init() {
    	
    }

    // Face off
    public void faceOff() {
    }

    static boolean s_penaltyShot = false;
    
    // Penalty shot
    public void penaltyShot() {
    	s_penaltyShot = true;
    }

    /*
     * Manages states assigned for every player
     */
    
    public static final int STATE_CHASE_PUCK = 0, STATE_CHASE_PLAYER = 1, STATE_CHARGE_FOR_GOAL = 2, STATE_PUSH_FORWARD = 3, STATE_TAKE_PUCK = 4;
    
    public static final int CHASE_PLAYER_DISTANCE = 200;
    public static final double CHASE_PUCK_TIME = 0.1;
    
    private int m_state = STATE_CHASE_PUCK;
    private IPlayer m_chasePlayer = null;
    
	public void setChasePuck() {
		m_state = STATE_CHASE_PUCK;
		m_chasePlayer = null;
	}

	public void setChasePlayer(IPlayer targetPlayer) {
		m_state = STATE_CHASE_PLAYER;
		m_chasePlayer = targetPlayer;
	}
	
	public void setChargeForGoal() {
		m_state = STATE_CHARGE_FOR_GOAL;
		m_chasePlayer = null;
	}
	
	public void setPushForward() {
		m_state = STATE_PUSH_FORWARD;
		m_chasePlayer = null;
	}

	public void setTakePuck() {
		m_state = STATE_TAKE_PUCK;
		m_chasePlayer = null;
	}
	
	// Player intelligence goes here
    public void step() {
    	if (s_penaltyShot)
    		Tactics.step(Team.s_players[0]);
    	
    	showDebugPoint(true);
    	setAimOnStick(true);
    	
    	switch(m_state) {
    	case STATE_CHASE_PLAYER:
    		//Mathematics.radius(m_chasePlayer.getX(), m_chasePlayer.getY(), G, y2)
    		{
    			Position direction = Mathematics.direction(m_chasePlayer.getX(), m_chasePlayer.getY(), Goalie.GOAL_POSITION.getX(), Goalie.GOAL_POSITION.getY());
    			skate(m_chasePlayer.getX() + direction.getX() * CHASE_PLAYER_DISTANCE, m_chasePlayer.getY() + direction.getY() * CHASE_PLAYER_DISTANCE, MAX_SPEED);
    			
    			setDebugPoint(m_chasePlayer.getX() + direction.getX() * CHASE_PLAYER_DISTANCE, m_chasePlayer.getY() + direction.getY() * CHASE_PLAYER_DISTANCE, Color.MAGENTA);
    		}
    		
    		//skate(m_chasePlayer, MAX_SPEED);
    		
    		//setDebugPoint(m_chasePlayer.getX() + direction.getX() * CHASE_PLAYER_DISTANCE, m_chasePlayer.getY() + direction.getY() * CHASE_PLAYER_DISTANCE, Color.GREEN);
    		
    		break;
    	case STATE_CHASE_PUCK:
    		IPuck puck = getPuck();
    		
    		int direction_x = (int) (Util.cosd(puck.getHeading()) * puck.getSpeed() * CHASE_PUCK_TIME),
    			direction_y = (int) (Util.sind(puck.getHeading()) * CHASE_PUCK_TIME * puck.getSpeed());
    		
    		double radius = Mathematics.radiusSquared(getX(), getY(), direction_x + puck.getX(), direction_y + puck.getY());
    		
    		skate(direction_x + puck.getX(), direction_y + puck.getY(), (int) Math.min(MAX_SPEED, radius * 1.0));
    		
    		setDebugPoint(direction_x + puck.getX(), direction_y + puck.getY(), Color.GREEN);
    		
    		//skate(getPuck(), MAX_SPEED);
    		break;
    	case STATE_CHARGE_FOR_GOAL:
    		{
    			double r = Mathematics.radiusSquared(getX(), getY(), -Goalie.GOAL_POSITION.getX(), Goalie.GOAL_POSITION.getY());
    		
	    		if (r < (GOAL_RADIUS * 18) * (GOAL_RADIUS * 18)) {
	    			shoot(-Goalie.GOAL_POSITION.getX(), (GOAL_RADIUS - 6) * (Math.random() > 0.5 ? 1 : -1), MAX_SHOT_SPEED);
	    		}
	    		else
	    			skate(-Goalie.GOAL_POSITION.getX(), Goalie.GOAL_POSITION.getY(), MAX_SPEED);
	    		
	    		setDebugPoint(-Goalie.GOAL_POSITION.getX(), Goalie.GOAL_POSITION.getY(), Color.RED);
    		}
    		
    		break;
    	case STATE_PUSH_FORWARD:
    		if (getPuck().getX() < 1733 / 2) {
    			skate(1730 / 4, getY(), MAX_SPEED);
    			setDebugPoint(1730 / 4, getY(), Color.BLACK);
    		}
    		else {
    			skate(1730, getY(), MAX_SPEED);
    			setDebugPoint(1730, getY(), Color.BLACK);
    		}
    		break;
    	case STATE_TAKE_PUCK:
    		skate(getPuck(), MAX_SPEED);
    		
    		setDebugPoint(getPuck().getX(), getPuck().getY(), Color.BLACK);
    		break;
    	}
    	
    	if (Mathematics.radiusSquared(getX(), getY(), Goalie.GOAL_POSITION.getX(), Goalie.GOAL_POSITION.getY()) < GOAL_RADIUS * 2)
			skate(getX() + 1000, getY(), MAX_SPEED);
    }
}
