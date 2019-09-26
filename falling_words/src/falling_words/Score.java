package falling_words;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private AtomicInteger missedWords;
	private AtomicInteger caughtWords;
	private int gameScore;
	
	Score() {
		missedWords = new AtomicInteger(0);
		caughtWords = new AtomicInteger(0);
		gameScore = 0;
	}
		
	// all getters and setters must be synchronized
	
	public synchronized AtomicInteger getMissed() {
		return missedWords;
	}

	public synchronized AtomicInteger getCaught() {
		return caughtWords;
	}
	
	public synchronized int getTotal() {
		return (missedWords.get()+caughtWords.get());
	}

	public synchronized int getScore() {
		return gameScore;
	}
	
	public void missedWord() {
		missedWords.getAndIncrement();
	}

	public void caughtWord(int length) {
		caughtWords.getAndIncrement();
		gameScore+=length;
	}

	public void resetScore() {
		caughtWords.set(0);
		missedWords.set(0);
		gameScore = 0;
	}
}
