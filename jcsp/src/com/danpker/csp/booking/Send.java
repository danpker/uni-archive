package com.danpker.csp.booking;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;

public class Send implements CSProcess {
	
	AltingChannelInput rec;

	public Send(AltingChannelInput in) {
		rec = in;
	}

	@Override
	public void run() {
		
		while(true) {
			String temp = (String) rec.read();
			
			System.out.println(this + " received " + temp);
		}

	}

}
