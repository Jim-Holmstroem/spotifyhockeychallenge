package team12;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hockey.api.IObject;
import hockey.api.IPlayerControl;
import hockey.api.IPuck;
import hockey.api.IPlayer;
import hockey.api.Player;
import hockey.api.Util;

public class Tactics {
	/*
	 * Our own global tracking of current tactics
	 */
	
	/*
	 * Called from goal keeper
	 */
	
	public static final int SUPER_DEFENSIVE = 0, DEFENSIVE = 1, CHARGE = 2, OFFENSIVE = 3, SUPER_OFFENSIVE = 4,
							   HOME = 0, AWAY = 1;
	
	public static final double DEFENSIVE_RADIUS = 120.0;
	
	public static int Tactics = DEFENSIVE, Side = HOME;
	
	public static void step(BasePlayer basePlayer) {
		IPuck puck = basePlayer.getPuck();
		
		boolean puckInOffensive = basePlayer.getPuck().getX() > 0,
				 hasPuck = puck.isHeld() && !puck.getHolder().isOpponent(),
				 hasOpponentPuck = puck.isHeld() && puck.getHolder().isOpponent();
		
		Side = puckInOffensive ? AWAY : HOME;
		
		if (hasPuck) {
			Tactics = OFFENSIVE;
		}
		else if (hasOpponentPuck)
			Tactics = DEFENSIVE;
		else
			Tactics = CHARGE;
		
		// FIXME: SUPER_*
		
		/*
		 * Tag our players for their different tasks
		 */
		
		LinkedList<IObject> assignPlayers = new LinkedList<IObject>(), opponentPlayers = new LinkedList<IObject>();
		
		for(int i = 1; i < Team.s_players.length; ++i) {
			assignPlayers.add(Team.s_players[i]);
			opponentPlayers.add(basePlayer.getPlayer(i + 6));
		}
		
		switch(Tactics) {
		case DEFENSIVE:
			/*
			 * Assign player to chase puck
			 */
			{
				Player closestPlayer = (Player) Mathematics.closest(assignPlayers, basePlayer.getPuck());
				
				((BasePlayer) closestPlayer).setChasePuck();
				assignPlayers.remove(closestPlayer);
			}
			
			/*
			 * Assign player to opponent player holding the puck
			 */
			
			{
				IPlayer opponentHoldingPuck = basePlayer.getPuck().getHolder();
				
				BasePlayer closestPlayer = (BasePlayer) Mathematics.closest(assignPlayers, opponentHoldingPuck);
				
				closestPlayer.setChasePlayer(opponentHoldingPuck);
				
				assignPlayers.remove(closestPlayer);
				opponentPlayers.remove(opponentHoldingPuck);
			}
			
			/*
			 * Remove the opponent player that is the furthest away from our goal
			 */
			{			
				
				opponentPlayers.remove(Mathematics.furthest(opponentPlayers, Goalie.GOAL_POSITION));
			}
			
			/*
			 * Assign the players to the remaining opponents
			 */
			
			for(int i = 0; i < assignPlayers.size(); ++i) {
				BasePlayer player = (BasePlayer) assignPlayers.get(i);
				
				IPlayer targetPlayer = (IPlayer) Mathematics.closest(opponentPlayers, player);
				
				player.setChasePlayer(targetPlayer);
				opponentPlayers.remove(targetPlayer);
			}
			
			break;
		case OFFENSIVE:
		case SUPER_OFFENSIVE:
			
			for(int i = 0; i < assignPlayers.size(); ++i) {
				BasePlayer player = (BasePlayer) assignPlayers.get(i);
				
				if (player.hasPuck())
					player.setChargeForGoal();
				else
					player.setPushForward();
			}
			
			break;
		case CHARGE:
			
			for(int i = 0; i < assignPlayers.size(); ++i)
				((BasePlayer) assignPlayers.get(i)).setTakePuck();
			
			break;
		case SUPER_DEFENSIVE:
			
			for(int i = 0; i < assignPlayers.size(); ++i) {
				double angle = (double) i / (assignPlayers.size() - 1) * 180.0;
				
				((BasePlayer) assignPlayers.get(i)).skate((int) (DEFENSIVE_RADIUS * Util.cosd(angle)), (int) (DEFENSIVE_RADIUS * Util.sind(angle)), IPlayerControl.MAX_SPEED);
			}
			
			break;
		}
	}
}
