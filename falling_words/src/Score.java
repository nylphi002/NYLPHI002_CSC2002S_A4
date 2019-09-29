
/**
 * The class Score is a part of the model, storing all running totals and methods to modify/access these values. The totals are:
 * <p>
 * <ul>
 * <li> Number of missed words
 * <li> Number of caught words
 * <li> Number of points
 * </ul>
 * <p>
 * @author      Philip Nylén
 * @version     1.8
 * @since       1.0
 */

public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;

	Score() {
		missedWords = 0;
		caughtWords = 0;
		gameScore = 0;
	}

	public synchronized int getMissed() {
		return missedWords;
	}

	public synchronized int getCaught() {
		return caughtWords;
	}

	public synchronized int getTotal() {
		return (missedWords + caughtWords);
	}

	public synchronized int getScore() {
		return gameScore;
	}

	public synchronized void missedWord() {
		missedWords++;
	}

	public synchronized void caughtWord(int length) {
		if (WordApp.score.getCaught() < WordApp.totalWords) {
			caughtWords++;
			gameScore += length;
			WordRecord.speedUpWords();
		}
		// checks if the word limit is reached
		if (WordApp.score.getCaught() == WordApp.totalWords) {
			WordApp.done = true;
		}
	}

	public synchronized void resetScore() {
		caughtWords = 0;
		missedWords = 0;
		gameScore = 0;
	}
}
