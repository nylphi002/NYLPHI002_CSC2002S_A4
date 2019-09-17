package falling_words;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
	public static volatile boolean done;
	private WordRecord[] words;
	private int noWords;
	private int maxY;

	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		/*try {
			Thread.sleep(32);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		g.clearRect(0, 0, width, height);
		g.setColor(Color.red);
		g.fillRect(0, maxY - 10, width, height);
		g.setColor(Color.black);
		g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		if (done) {
			g.drawString(WordApp.finishLine, width / 3, height / 2);
		} else {
		// draw the words
		// animation must be added
		for (int i = 0; i < noWords; i++) {
			// g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
			g.drawString(WordApp.words[i].getWord(), WordApp.words[i].getX(), WordApp.words[i].getY());
			//g.drawString(words[i].getWord(), words[i].getX(), words[i].getY() + 20); // y-offset for skeleton so that
																						// you can see the words
		}
	}

	}

	WordPanel(WordRecord[] words, int maxY) {
		this.words = words; // will this work?
		noWords = words.length;
		done = false;
		this.maxY = maxY;
	}

	public void run() {
		// add in code to animate this
		while (!done) {
			for (int i = 0; i < noWords; i++) {
				WordApp.words[i].setY(WordApp.words[i].getY() + WordApp.words[i].getSpeed());
				if (WordApp.words[i].dropped()) {
					WordApp.score.missedWord();
					WordApp.missed.setText("Missed: " + WordApp.score.getMissed() + "    ");
					WordApp.words[i].resetWord();
				}
			}
			try {
				Thread.sleep(32);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (WordApp.score.getCaught() >= WordApp.totalWords) {
				done = true;
			}
			this.repaint();
		}
	}

}


