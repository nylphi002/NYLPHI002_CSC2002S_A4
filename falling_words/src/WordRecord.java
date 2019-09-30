/**
 * The class WordRecord is a part of the model, representing a word and contains all the information
 * about it and the methods to modify/access it. The updating of the word is done on a separate thread.
 * 
 * @author      Philip Nylén
 * @version     1.8
 * @since       1.0
 */

public class WordRecord implements Runnable {
	private String text;
	private int x;
	private int y;
	private int maxY;
	private boolean dropped;

	private int fallingSpeed;
	private static int maxWait = 350;
	private static int minWait = 100;

	public static WordDictionary dict;

	private double startTime;

	private void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	private double timer() {
		return (System.currentTimeMillis() - this.startTime) / 1.0f;
	}

	WordRecord() {
		text = "";
		x = 0;
		y = 0;
		maxY = 300;
		dropped = false;
		fallingSpeed = (int) (Math.random() * (maxWait - minWait) + minWait);
	}

	WordRecord(String text) {
		this();
		this.text = text;
	}

	WordRecord(String text, int x, int maxY) {
		this(text);
		this.x = x;
		this.maxY = maxY;
	}

	public synchronized void setY(int y) {
		if (y > maxY) {
			y = maxY;
			dropped = true;
		}
		this.y = y;
	}

	public static synchronized void speedUpWords() {
		maxWait -= (int) (maxWait / 2) / WordApp.noWords;
		minWait -= (int) (minWait / 2) / WordApp.noWords;
	}

	public synchronized void setX(int x) {
		this.x = x;
	}

	public synchronized void setWord(String text) {
		this.text = text;
	}

	public synchronized String getWord() {
		return text;
	}

	public synchronized int getX() {
		return x;
	}

	public synchronized int getY() {
		return y;
	}

	public synchronized int getSpeed() {
		return fallingSpeed;
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}

	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		resetPos();
		text = dict.getNewWord();
		dropped = false;
		fallingSpeed = (int) (Math.random() * (maxWait - minWait) + minWait);
	}

	public synchronized boolean matchWord(String typedText) {
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		} else
			return false;
	}

	public synchronized void drop(int inc) {
		setY(y + inc);
	}

	public synchronized boolean dropped() {
		return dropped;
	}

	@Override
	public void run() {
		maxWait = 350;
		minWait = 100;
		startTimer();
		while (!WordApp.done) {
			if (timer() >= ((double) fallingSpeed)) {
				startTimer();
				synchronized (this) {
					drop(10);
				}
				if (dropped()) {
					synchronized (WordApp.dict) {
						WordApp.score.missedWord();
						WordApp.missed.setText("Missed: " + WordApp.score.getMissed() + "    ");
					}
					synchronized (this) {
						resetWord();
					}
				}
			}
		}
	}

}
