package com.danpker.csp.carpark;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;

public class Depart implements CSProcess {
	
	private AltingChannelInput button_pressed;
	private ChannelOutput send_to_control;
	private AltingChannelInput dep_conf;
	
	public Depart(AltingChannelInput button_press, ChannelOutput send, AltingChannelInput dep_conf) {
		button_pressed = button_press;
		send_to_control = send;
		this.dep_conf = dep_conf;
	}

	@Override
	public void run() {
		final Guard[] choices = {button_pressed, dep_conf};
		final Alternative choice = new Alternative(choices);
		while(true) {
			switch(choice.priSelect()) {
			case 0:
				if (button_pressed.read() == "Depart") {
					send_to_control.write(1);	
				}
				break;
			case 1:
				int res = (Integer) dep_conf.read();
				if (0 <=  res) {
					// Car departed
					System.out.println("Car departed. " + res + " spaces left");
				} else {
					// Car not departed
					System.out.println("No cars to depart");
				}
			}

		}
	}

}
