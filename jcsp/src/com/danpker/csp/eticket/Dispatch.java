package com.danpker.csp.eticket;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;

public class Dispatch implements CSProcess {
	
	private ChannelInput dispatch_email;

	public Dispatch(AltingChannelInput altingChannelInput) {
		dispatch_email = altingChannelInput;
	}

	@Override
	public void run() {
		while(true) {
			dispatch_email.read();
			System.out.println(this + " send email");
		}
	}

}
