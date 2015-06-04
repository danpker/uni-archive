package com.danpker.csp.eticket;

import java.util.ArrayList;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannel;

public class Mailbag implements CSProcess {
	
	private int id = 0;
	private ArrayList<Integer> unread_emails;
	private AltingChannelInput rec_email;
	private AltingChannelInput email_req;
	private ChannelOutput send_inbox;

	public Mailbag(AltingChannelInput in, One2OneChannel[] internet) {
		unread_emails = new ArrayList<Integer>();
		email_req = internet[0].in();
		send_inbox = internet[1].out();
		rec_email = in;
	}

	@Override
	public void run() {
		final Guard[] choices = { rec_email, email_req };
		final Alternative choice = new Alternative(choices);
		while(true) {
			
			switch(choice.priSelect()) {
			case 0:
				rec_email.read();
				unread_emails.add(id++);
				System.out.println("Mailbag: There are " + unread_emails.size() + " emails");
				break;
			case 1:
				if(email_req.read() == "getemail") {
					send_inbox.write(unread_emails);
					break;
				}
			}

		}

	}

}
