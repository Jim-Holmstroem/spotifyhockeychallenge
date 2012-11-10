package team12;

public class Forward extends BasePlayer {
	private int m_index;
	
	private static String[] s_names = { "Buzz Ostrich", "Yuri Guinna Pig" };
	
	public Forward(int index) {
		m_index = index;
	}
	
    // Number of forward
    public int getNumber() { return 15; }

    // Name of forward
    public String getName() { return s_names[m_index]; }

    // Intelligence of forward
    public void step() {
    	super.step();
    	
	/*if (hasPuck()) {
	    shoot(getPlayer(5), 4444); // pass center player
	}
	else {
	    skate(getPuck(), MAX_SPEED); // get the puck
	}*/
    }
}
