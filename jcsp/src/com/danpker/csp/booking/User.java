package com.danpker.csp.booking;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2OneChannel;

/***
 * Models a user using the booking system
 * @author danpker
 *
 */
public class User implements CSProcess {
	
	ChannelOutput address; // 'address' of the booking system
	One2OneChannel con; // 'hos address' of user
	One2OneChannel socket[];
	
	public User(ChannelOutput addr) {
		address = addr;
		con = Channel.one2one();
	}

	@Override
	public void run() {
		
		// Send id for system and await a 'socket'
		address.write(con);
		AltingChannelInput wait = con.in();
		socket = (One2OneChannel[]) wait.read();
		
		ChannelOutput send = socket[0].out();
		ChannelInput rec = socket[1].in();
		
		System.out.println(this + " connected");
		
		// Communicate with session
		send.write("book");
		String resp = (String) rec.read();
		if (resp.contentEquals("booked")) {
			System.out.println(this + " has booked");
		}
		send.write("quit");
	}

}
