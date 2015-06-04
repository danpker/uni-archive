package com.danpker.csp.carpark;

import java.awt.Frame;
import java.awt.GridLayout;

import org.jcsp.awt.ActiveButton;
import org.jcsp.awt.ActiveClosingFrame;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;
import org.jcsp.util.OverWriteOldestBuffer;

public class Carpark {

	public static void main(String argv[]) {
		final ActiveClosingFrame activeClosingFrame = new ActiveClosingFrame(
				"Carpark");
		Frame frame = activeClosingFrame.getActiveFrame();

		// Button channels
		final One2OneChannel arrive_event = Channel
				.one2one();
		final One2OneChannel depart_event = Channel
				.one2one();

		// Arrive/Depart channels
		final One2OneChannel arrival = Channel.one2one(new OverWriteOldestBuffer(10));
		final One2OneChannel departure = Channel.one2one(new OverWriteOldestBuffer(10));

		// Arrive/Depart confirm channels
		final One2OneChannel arrival_confirm = Channel.one2one();
		final One2OneChannel departure_confirm = Channel.one2one();

		// Buttons
		ActiveButton btn_arrive = new ActiveButton(null, arrive_event.out(),
				"Arrive");
		ActiveButton btn_depart = new ActiveButton(null, depart_event.out(),
				"Depart");

		// Frame Layout
		frame.setLayout(new GridLayout(1, 2));
		frame.add(btn_arrive);
		frame.add(btn_depart);
		frame.pack();
		frame.setVisible(true);

		// Processes
		Arrive arrive = new Arrive(arrive_event.in(), arrival.out(),
				arrival_confirm.in());
		Control control = new Control(arrival.in(), departure.in(),
				arrival_confirm.out(), departure_confirm.out());
		Depart depart = new Depart(depart_event.in(), departure.out(),
				departure_confirm.in());

		new Parallel(new CSProcess[] { activeClosingFrame, btn_arrive,
				btn_depart, arrive, control, depart }).run();
	}

}
