package team12;

public class Center extends BasePlayer {
    // Number of center player
    public int getNumber() { return 19; }

    // Name of center player
    public String getName() { return "Michael Cow"; }

    // Center player's intelligence
    public void step() {
    	super.step();
    	
	/*if (hasPuck())
	    skate(GOAL_POSITION, MAX_SPEED);
	else
	    skate(0, getPuck().getY(), 1000);*/
    }
}
