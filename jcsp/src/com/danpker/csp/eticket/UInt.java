package com.danpker.csp.eticket;

import java.util.ArrayList;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannel;

public class UInt implements CSProcess {

	private ChannelOutput req_email;
	private AltingChannelInput rec_email;
	private ChannelOutput dispatch_email;

	private ArrayList<Integer> emails;
	private int index;

	// Button channels
	private One2OneChannel next_event;
	private One2OneChannel prev_event;
	private One2OneChannel del_event;
	private One2OneChannel send_event;

	public UInt(One2OneChannel[] con_info, One2OneChannel next_event2,
			One2OneChannel prev_event2, One2OneChannel del_event2,
			One2OneChannel send_event2, ChannelOutput dispatch_email2) {
		req_email = con_info[0].out();
		rec_email = con_info[1].in();

		next_event = next_event2;
		prev_event = prev_event2;
		del_event = del_event2;
		send_event = send_event2;

		this.dispatch_email = dispatch_email2;
		emails = new ArrayList<Integer>();
		index = 0;

	}

	@Override
	public void run() {

		final Guard[] choices = { next_event.in(), prev_event.in(),
				del_event.in(), send_event.in() };
		final Alternative choice = new Alternative(choices);
		syncEmail();
		while (true) {
			switch (choice.priSelect()) {
			case 0:
				// Next email
				next_event.in().read();
				if (index < emails.size() - 1) {
					index++;
					System.out.println("Next email, index: " + index);
				}
				break;
			case 1:
				// Prev email
				prev_event.in().read();
				if (index > 0) {
					index--;
					System.out.println("Prev email, index: " + index);
				}
				break;
			case 2:
				// del email
				del_event.in().read();
				if (emails.size() > 0) {
					emails.remove(index);
					System.out.println("Deleted email, index: " + index);
				}
				// put index back in scope
				if (index > emails.size() - 1) {
					index--;
				}
				break;
			case 3:
				send_event.in().read();
				// send email
				dispatch_email.write("email");
			}
			syncEmail();
			printInbox();

		}

	}

	public void syncEmail() {
		req_email.write("getemail");
		emails = (ArrayList<Integer>) rec_email.read();
		System.out.println("Got emails, size:" + emails.size());
	}

	public void printInbox() {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < emails.size(); i++) {
			if (index == i) {
				output.append('[');
				output.append(emails.get(i));
				output.append(']');
			} else {
				output.append(emails.get(i));
			}

			output.append(' ');

		}

		System.out.println(output);
	}

}
