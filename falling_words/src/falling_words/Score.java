package falling_words;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private AtomicInteger missedWords;
	private AtomicInteger caughtWords;
	private AtomicInteger gameScore;
	
	Score() {
		missedWords = new AtomicInteger(0);
		caughtWords = new AtomicInteger(0);
		gameScore = new AtomicInteger(0);
	}
		
	// all getters and setters must be synchronized
	
	public synchronized int getMissed() {
		return missedWords.get();
	}

	public synchronized AtomicInteger getCaught() {
		return caughtWords;
	}
	
	public synchronized int getTotal() {
		return (missedWords.get()+caughtWords.get());
	}

	public synchronized AtomicInteger getScore() {
		return gameScore;
	}
	
	public void missedWord() {
		missedWords.getAndIncrement();
	}

	public void caughtWord(int length) {
		caughtWords.getAndIncrement();
		gameScore.getAndAdd(length);
	}

	public void resetScore() {
		caughtWords.set(0);
		missedWords.set(0);
		gameScore.set(0);
	}
}
