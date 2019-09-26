package falling_words;


public class WordRecord implements Runnable  {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=350;
	private static int minWait=100;

	//private static int maxWait=4;
	//private static int minWait=1;
	
	public static WordDictionary dict;
	
	private double startTime;
	
	private void tick(){
		this.startTime = System.currentTimeMillis();
	}
	
	private double tock(){
		return (System.currentTimeMillis() - this.startTime) / 1.0f; 
	}
	
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	WordRecord(String text) {
		this();
		this.text=text;
	}
	
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}
	
// all getters and setters must be synchronized
	public synchronized void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
			this.y=y;
	}
	
	public synchronized void setX(int x) {
		this.x=x;
	}
	
	public synchronized void setWord(String text) {
		this.text=text;
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
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());

	}
	
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	

	public synchronized void drop(int inc) {
		setY(y+inc);
	}
	
	public synchronized boolean dropped() {
		return dropped;
	}

	@Override
	public void run() {
		tick();
		while (!WordPanel.done) {
			if (tock() >= ((double) fallingSpeed)) {
				tick();
				synchronized(this) {
					drop(10);
				}
				if (dropped()) {
					WordApp.score.missedWord();
					synchronized(WordApp.dict) {
						WordApp.missed.setText("Missed: " + WordApp.score.getMissed() + "    ");
					}
					synchronized(this) {
						resetWord();
					}
				}
				//tick();
			}
		}
	}

}
