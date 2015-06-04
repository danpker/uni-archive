package com.danpker.csp.eticket;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;

/***
 * An email arrives and is send to the mail-bag
 * 
 * @author danpker
 * 
 */
public class EmailArrive implements CSProcess {

	private AltingChannelInput button_pressed;
	private ChannelOutput send_to_mailbag;

	public EmailArrive(AltingChannelInput in, ChannelOutput out) {
		button_pressed = in;
		send_to_mailbag = out;
	}

	@Override
	public void run() {
		while (true) {
			if (button_pressed.read() == "Arrive") {
				System.out.println("Email arrived, sending to mailbag");
				send_to_mailbag.write(1);
			}
		}

	}

}
