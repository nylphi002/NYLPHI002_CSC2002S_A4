/**
 * The class WordDictionary is a part of the model which contains the words to be drawn by the panel {@link WordPanel}.
 * 
 * @author      Philip Nylén
 * @version     1.8
 * @since       1.0
 */

public class WordDictionary {
	int size;
	// initializes a default dictionary
	static String[] theDict = { "litchi", "banana", "apple", "mango", "pear", "orange", "strawberry", "cherry", "lemon",
			"apricot", "peach", "guava", "grape", "kiwi", "quince", "plum", "prune", "cranberry", "blueberry",
			"rhubarb", "fruit", "grapefruit", "kumquat", "tomato", "berry", "boysenberry", "loquat", "avocado" };

	// initializes a new dictionary if provided
	WordDictionary(String[] tmp) {
		size = tmp.length;
		theDict = new String[size];
		for (int i = 0; i < size; i++) {
			theDict[i] = tmp[i];
		}

	}

	WordDictionary() {
		size = theDict.length;

	}

	// generates a new random word from the dictionary
	public synchronized String getNewWord() {
		int wdPos = (int) (Math.random() * size);
		return theDict[wdPos];
	}

}
