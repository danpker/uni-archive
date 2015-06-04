package com.danpker.csp.carpark;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;
public class Control implements CSProcess {
	
	private AltingChannelInput arrived;
	private AltingChannelInput departed;
	private ChannelOutput arrival_confirm;
	private ChannelOutput departure_confirm;
	private int spaces;
	private static final int MAX_SPACES = 5;

	public Control(AltingChannelInput arr, AltingChannelInput dep, ChannelOutput arr_conf, ChannelOutput dep_conf) {
		arrived = arr;
		departed = dep;
		arrival_confirm = arr_conf;
		departure_confirm = dep_conf;
		spaces = MAX_SPACES; // max spaces
	}
	@Override
	public void run() {
		boolean nospace = false;
		final Guard[] choices = {departed, arrived};
		final Alternative choice = new Alternative(choices);
		
		while(true) {
			switch(choice.priSelect()) {
			case 0:
				// Depart
				departed.read();
				if ( spaces < MAX_SPACES) {
					departure_confirm.write(++spaces);
				} else {
					departure_confirm.write(-1);
				}
				break;
			case 1:
				//Arrive
				if ( spaces > 0) {
					nospace = false;
					arrived.read();
					arrival_confirm.write(--spaces);
				} else {
					arrival_confirm.write(-1);
					nospace = true;
				}
				break;
			}
		}

	}

}
