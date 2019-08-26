package com.risingthumb.navigator.timing;

public class Timer extends Thread {
	private long time;
	private TimerEvent e;
	
	public Timer(long time, TimerEvent e) {
		this.time = time;
		this.e = e;
		this.start();
		this.run();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
			e.run();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
