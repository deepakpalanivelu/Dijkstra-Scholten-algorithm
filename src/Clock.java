package djikstraSchloten;

public class Clock {

	private int clockTime;

	public Clock(){
		this.clockTime = 0;
	}

	public synchronized void tick(int newTime) throws InterruptedException{

		if(newTime>getClockTime()) {
			setClockTime(newTime + 1);
		}
		else {
			setClockTime(getClockTime() + 1);
		}
	}

	public int getClockTime() {
		return clockTime;
	}

	public void setClockTime(int clockTime) {
		this.clockTime = clockTime;
	}
}

