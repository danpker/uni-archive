package com.danpker.csp.eticket;

import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;

public class Icon implements CSProcess {

	AltingChannelInput button_pressed;
	One2OneChannel[] con_info;
	ChannelOutput dispatch_email;

	// Button channels
	private One2OneChannel next_event;
	private One2OneChannel prev_event;
	private One2OneChannel del_event;
	private One2OneChannel send_event;

	public Icon(AltingChannelInput in, One2OneChannel[] internet,
			One2OneChannel next_event2, One2OneChannel prev_event2,
			One2OneChannel del_event2, One2OneChannel send_event2,
			ChannelOutput dispatch_email2) {
		button_pressed = in;
		con_info = internet;
		this.next_event = next_event2;
		this.prev_event = prev_event2;
		this.del_event = del_event2;
		this.send_event = send_event2;
		this.dispatch_email = dispatch_email2;
	}

	@Override
	public void run() {
		while (true) {
			if (button_pressed.read() == "Icon") {
				System.out.println("Icon pressed");
				UInt uint = new UInt(con_info, next_event, prev_event,
						del_event, send_event, dispatch_email);
				new Parallel(new CSProcess[] { uint }).run();
			}
		}
	}

}
