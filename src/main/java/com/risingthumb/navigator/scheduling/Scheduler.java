package com.risingthumb.navigator.scheduling;

import com.risingthumb.navigator.EventHandler;

public class Scheduler {
	
	int tickWait;
	ScheduledEvent event;
	
	// Create the bad boy and add it to the list of scheduled tasks
	public Scheduler(int tickWait, ScheduledEvent event) {
		this.tickWait = tickWait;
		this.event = event;
		EventHandler.schedulerList.add(this);
	}
	
	// Plays each clientTickEvent. Be advised, this isn't accurate for TIME, it's accurate for CLIENT TICKS
	public void tickTock() {
		this.tickWait--;
		if (this.tickWait<=0){
			event.run();
		}
	}
	
	public int getTick() {
		return this.tickWait;
	}

}
