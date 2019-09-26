


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
		g.clearRect(0, 0, width, height);
		g.setColor(Color.red);
		g.fillRect(0, maxY - 10, width, height);
		g.setColor(Color.black);
		g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		if (done) {
			g.drawString(WordApp.finishLine, width / 3, height / 2);
		} else {
		// draw the words
		for (int i = 0; i < noWords; i++) {
			synchronized(WordApp.words[i]) {
			g.drawString(WordApp.words[i].getWord(), WordApp.words[i].getX(), WordApp.words[i].getY());
			}
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
			if (WordApp.score.getCaught().get() >= WordApp.totalWords) {
				done = true;
			}
			this.repaint();
		}
	}

}


