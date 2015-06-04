package com.danpker.csp.booking;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInput;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.ProcessManager;

/***
 * Awaits a user connection on a defined channel
 * Creates a new channel and gives it to use and spawned session
 * @author danpker
 *
 */
public class Server implements CSProcess {
	
	ChannelInput port;
	ChannelOutput send_booking;
	
	public Server(ChannelInput number, ChannelOutput send) {
		port = number;
		send_booking = send;
	}

	@Override
	public void run() {
	
		while(true) {
			// listen on 'port' for incoming
			One2OneChannel temp = (One2OneChannel) port.read();
			new ProcessManager(new Session(temp, send_booking)).start();
		}
	}
}
