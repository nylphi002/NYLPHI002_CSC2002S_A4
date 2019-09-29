import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * WordApp is the main class which sets up the frame and initializes all other objects.
 * 
 * @author      Philip Nylén
 * @version     1.8
 * @since       1.0
 */

public class WordApp {
//shared variables
	static int noWords = 4;
	static int totalWords;

	static int frameX = 1000;
	static int frameY = 600;
	static int yLimit = 480;

	static WordDictionary dict = new WordDictionary(); // uses default dictionary, to read from file eventually
	static WordRecord[] words;
	static Score score = new Score();
	static WordPanel w;

	public static boolean done;

	static JLabel caught;
	static JLabel missed;
	static JLabel scr;

	static String finishLine; // text to be drawn by WordPanel when done is set to true

	public static void setupGUI(int frameX, int frameY, int yLimit) {
		// Frame init and dimensions
		JFrame frame = new JFrame("WordGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameX, frameY);

		JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
		g.setSize(frameX, frameY);

		w = new WordPanel(words, yLimit);
		w.setSize(frameX, yLimit + 100);
		g.add(w);

		JPanel txt = new JPanel();
		txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
		caught = new JLabel("Caught: " + score.getCaught() + "    ");
		missed = new JLabel("Missed:" + score.getMissed() + "    ");
		scr = new JLabel("Score:" + score.getScore() + "    ");
		txt.add(caught);
		txt.add(missed);
		txt.add(scr);

		final JTextField textEntry = new JTextField("", 20);
		textEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String text = textEntry.getText();
				for (int i = 0; i < noWords; i++) {
					if (words[i].matchWord(text)) {
						score.caughtWord(text.length());
						caught.setText("Caught: " + score.getCaught() + "    ");
						scr.setText("Score: " + score.getScore() + "    ");
					}
				}
				textEntry.setText("");
				textEntry.requestFocus();
			}
		});

		txt.add(textEntry);
		txt.setMaximumSize(txt.getPreferredSize());
		g.add(txt);

		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton startB = new JButton("Start");
		;

		// added listener to the jbutton to handle the "pressed" event
		startB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if (WordPanel.done) {
				startB.setText("Restart");
				finishLine = "You have won!!!";
				Thread[] tArr = new Thread[noWords];
				for (int i = 0; i < noWords; i++) {
					words[i].resetWord();
				}
				if (!done) {
					done = true;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e1) {
						System.err.println("Could not put the thread to sleep");
						e1.printStackTrace();
					}
				}
				score.resetScore();
				caught.setText("Caught: " + score.getCaught() + "    ");
				missed.setText("Missed:" + score.getMissed() + "    ");
				scr.setText("Score: " + score.getScore() + "    ");
				done = false;
				Thread t = new Thread(w);
				t.start();
				for (int i = 0; i < noWords; ++i) {
					tArr[i] = new Thread(words[i]);
					tArr[i].start();
				}
				textEntry.setText("");
				textEntry.requestFocus(); // return focus to the text entry field
			}
		});
		JButton endB = new JButton("End");
		;

		// added listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finishLine = "You have ended the game!";
				done = true;
			}
		});

		JButton quitB = new JButton("Quit");

		// added listener to the jbutton to handle the "pressed" event
		quitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done = true;
				frame.dispose();
			}
		});

		b.add(startB);
		b.add(endB);
		b.add(quitB);

		g.add(b);

		frame.setLocationRelativeTo(null); // Center window on screen.
		frame.add(g); // add contents to window
		frame.setContentPane(g);
		frame.setVisible(true);

	}

	public static String[] getDictFromFile(String filename) {
		String[] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();

			dictStr = new String[dictLength];
			for (int i = 0; i < dictLength; i++) {
				dictStr[i] = new String(dictReader.next());
			}
			dictReader.close();
		} catch (IOException e) {
			System.err.println("Problem reading file " + filename + " default dictionary will be used");
		}
		return dictStr;

	}

	public static void main(String[] args) {

		// deal with command line arguments
		totalWords = Integer.parseInt(args[0]); // total words to fall - takes in args[0]
		noWords = Integer.parseInt(args[1]); // total words falling at any point - takes in args[1]
		assert (totalWords >= noWords); // this could be done more neatly

		String[] tmpDict = getDictFromFile(args[2]); // file of words - takes in args[2]
		// String[] tmpDict = WordDictionary.theDict;
		if (tmpDict != null) {
			dict = new WordDictionary(tmpDict);
		}
		WordRecord.dict = dict; // sets the class dictionary for the words.

		words = new WordRecord[noWords]; // shared array of current words
		int x_inc = (int) frameX / noWords;
		// initializes shared array of current words
		for (int i = 0; i < noWords; i++) {
			words[i] = new WordRecord(dict.getNewWord(), i * x_inc, yLimit);
		}
		// Start WordPanel thread - for redrawing animation
		setupGUI(frameX, frameY, yLimit);

	}

}
