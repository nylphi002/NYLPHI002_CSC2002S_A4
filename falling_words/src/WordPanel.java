import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JPanel;

/**
 * The class WordDictionary functions as the view which displays and animate the panel as well as the words. 
 * The animation runs on its own thread.
 * 
 * @author      Philip Nylén
 * @version     1.8
 * @since       1.0
 */

public class WordPanel extends JPanel implements Runnable {
	private int noWords;
	private int maxY;

	public void paintComponent(Graphics g) {
		// paints the background
		int width = getWidth();
		int height = getHeight();
		g.clearRect(0, 0, width, height);
		g.setColor(Color.red);
		g.fillRect(0, maxY - 10, width, height);
		g.setColor(Color.black);
		g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		if (WordApp.done) {
			// draws either the win text or ended text
			g.drawString(WordApp.finishLine + " You got " + WordApp.score.getScore() + " points", width / 3,
					height / 2);
		} else {
			// drawing of the words
			for (int i = 0; i < noWords; i++) {
				synchronized (WordApp.words[i]) {
					g.drawString(WordApp.words[i].getWord(), WordApp.words[i].getX(), WordApp.words[i].getY());
				}
			}
		}

	}

	WordPanel(WordRecord[] words, int maxY) {
		noWords = words.length;
		this.maxY = maxY;
	}

	public void run() {
		while (!WordApp.done) {
			// animation/redrawing of the view
			this.repaint();
		}
		this.repaint();
	}

}


