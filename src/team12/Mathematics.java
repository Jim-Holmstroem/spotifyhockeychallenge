package team12;

import hockey.api.IObject;
import hockey.api.IPlayer;
import hockey.api.Position;

public class Mathematics {
	public static final int PLAYER_RADIUS = 80;
	
	public static double radiusSquared(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}
	
	public static Position direction(int x1, int y1, int x2, int y2) {
		int delta_x = x2 - x1, delta_y = y2 - y1;
		double radius = Math.sqrt(delta_x * delta_x + delta_y * delta_y);
		
		return new Position((int) (delta_x / radius), (int) (delta_y / radius));
	}
	
	public static Position normalize(int x, int y) {
		double radius = Math.sqrt(x * x + y * y);
		
		return new Position((int) (x / radius), (int) (y / radius));
	}
	
	public static IObject closest(Iterable<IObject> elements, IObject target) {
		double closestRadius = Double.MAX_VALUE;
		IObject closestObject = null;
		
		for(IObject o : elements) {
			double radius = Mathematics.radiusSquared(target.getX(), target.getY(), o.getX(), o.getY());
			
			if (radius < closestRadius) {
				closestObject = o;
				closestRadius = radius;
			}
		}
		
		return closestObject;
	}
	
	public static IObject furthest(Iterable<IObject> elements, IObject target) {
		double furthestRadius = Double.MIN_VALUE;
		IObject furthestObject = null;
		
		for(IObject o : elements) {
			double radius = Mathematics.radiusSquared(target.getX(), target.getY(), o.getX(), o.getY());
			
			if (radius > furthestRadius) {
				furthestObject = o;
				furthestRadius = radius;
			}
		}
		
		return furthestObject;
	}
	
	public boolean isInLineOfSight(Goalie goalie, IObject source, IObject target) {
		Position normal = new Position(-(target.getY() - source.getY()), (target.getX() - source.getY()));
		int c = -(normal.getX() * source.getX() + normal.getY() * source.getY());
		
		for(int i = 1; i < 6; ++i) {
			IPlayer player = goalie.getPlayer(i);
			
			double distance = Math.abs(normal.getX() * player.getX() + normal.getY() * player.getY() + c) / Math.sqrt(normal.getX() * normal.getX() + normal.getY() * normal.getY());
			
			if (distance < PLAYER_RADIUS)
				return false;
		}
		
		return true;
	}
	
	private static Position topGoalPosition = new Position(Goalie.GOAL_POSITION.getX() + 80, Goalie.GOAL_POSITION.getY()),
							  bottomGoalPosition = new Position(Goalie.GOAL_POSITION.getX() - 80, Goalie.GOAL_POSITION.getY());
	
	public boolean isGoalInLineOfSight(Goalie goalie, IObject source) {
		
		
		return isInLineOfSight(goalie, source, Goalie.GOAL_POSITION) ||
				isInLineOfSight(goalie, source, topGoalPosition) ||
				isInLineOfSight(goalie, source, bottomGoalPosition);
	}
}
