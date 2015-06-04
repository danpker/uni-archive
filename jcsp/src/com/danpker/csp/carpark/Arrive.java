package com.danpker.csp.carpark;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;

public class Arrive implements CSProcess {
	
	private AltingChannelInput button_pressed;
	private ChannelOutput send_to_control;
	private AltingChannelInput arr_conf;
	private int queue;
	private int prev_queue;
	
	public Arrive(AltingChannelInput button_press, ChannelOutput send, AltingChannelInput arr_conf) {
		button_pressed = button_press;
		send_to_control = send;
		this.arr_conf = arr_conf;
		queue = 0;
	}

	@Override
	public void run() {
		final Guard[] choices = {button_pressed, arr_conf};
		final Alternative choice = new Alternative(choices);
		prev_queue = 0;
		while(true) {
			switch(choice.priSelect()) {
			case 0:
				if (button_pressed.read() == "Arrive") {
					send_to_control.write(1);
					queue++;
				}
				break;
			case 1:
				int res = (Integer) arr_conf.read();
				if( 0 <= res) {
					// Car arrived
					queue--;
					System.out.println("Car arrived. " + res + " spaces left");
					// if there is a queue, show it
					if (queue > 0) {
						prev_queue = queue;
						System.out.println("No space, " + queue + " cars in queue");
					}
				} else if (queue > prev_queue) {
					// show queue
					prev_queue = queue;
					System.out.println("No space, " + queue + " cars in queue");
				}
				break;
			}
			// queue can never be more than 10
			if (queue > 10) {
				queue = 10;
			}
		}
		
	}

}
