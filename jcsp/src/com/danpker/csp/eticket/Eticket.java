package com.danpker.csp.eticket;

import java.awt.Frame;
import java.awt.GridLayout;

import org.jcsp.awt.ActiveButton;
import org.jcsp.awt.ActiveClosingFrame;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;
import org.jcsp.util.OverWriteOldestBuffer;

public class Eticket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ActiveClosingFrame activeClosingFrame = new ActiveClosingFrame(
				"Eticket");
		Frame frame = activeClosingFrame.getActiveFrame();

		// Button channels
		final One2OneChannel arrive_event = Channel
				.one2one(new OverWriteOldestBuffer(10));
		final One2OneChannel icon_event = Channel.one2one();
		final One2OneChannel next_event = Channel.one2one();
		final One2OneChannel prev_event = Channel.one2one();
		final One2OneChannel del_event = Channel.one2one();
		final One2OneChannel send_event = Channel.one2one();

		// Arrive/Depart channels
		final One2OneChannel arrival = Channel.one2one();
		final One2OneChannel internet[] = { Channel.one2one(),
				Channel.one2one() };
		
		final One2OneChannel dispatch_email = Channel.one2one();

		// Buttons
		ActiveButton btn_arrive = new ActiveButton(null, arrive_event.out(),
				"Arrive");
		ActiveButton btn_icon = new ActiveButton(null, icon_event.out(), "Icon");
		final ActiveButton btn_next = new ActiveButton(null, next_event.out(),
				"Next");
		final ActiveButton btn_prev = new ActiveButton(null, prev_event.out(),
				"Previous");
		final ActiveButton btn_del = new ActiveButton(null, del_event.out(),
				"Delete");
		final ActiveButton btn_send = new ActiveButton(null, send_event.out(),
				"Send");

		// Frame Layout
		frame.setLayout(new GridLayout(2, 4));
		frame.add(btn_arrive);
		frame.add(btn_icon);
		frame.add(btn_next);
		frame.add(btn_prev);
		frame.add(btn_del);
		frame.add(btn_send);
		frame.pack();
		frame.setVisible(true);

		// Processes
		EmailArrive arrive = new EmailArrive(arrive_event.in(), arrival.out());
		Mailbag mailbag = new Mailbag(arrival.in(), internet);
		Icon icon = new Icon(icon_event.in(), internet, next_event, prev_event,
				del_event, send_event, dispatch_email.out());
		Dispatch dispatch = new Dispatch(dispatch_email.in());

		new Parallel(new CSProcess[] { activeClosingFrame, btn_arrive,
				btn_icon, btn_next, btn_prev, btn_del, btn_send, arrive,
				mailbag, icon, dispatch }).run();

	}

}
