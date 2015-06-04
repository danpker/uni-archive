package com.danpker.csp.booking;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;
import org.jcsp.util.OverWriteOldestBuffer;

public class Booking {

	public static void main(String[] args) {
		
		One2OneChannel internet = Channel.one2one(new OverWriteOldestBuffer(65645));
		One2OneChannel booking_send = Channel.one2one(new OverWriteOldestBuffer(64645));
		
		Server server = new Server(internet.in(), booking_send.out());
		Send send = new Send(booking_send.in());
		User user = new User(internet.out());
		User user2 = new User(internet.out());
		User user3 = new User(internet.out());
		
		new Parallel(new CSProcess[]{
				server,
				send,
				user,
				user2,
				user3
		}).run();
	}

}
