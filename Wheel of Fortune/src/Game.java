import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
public class Game {
	public int number;
	public File countries, high_sore_table, alphabet;
	public Stack country;
	public Stack S1;  //sorted country stack
	public Stack S2;
	public Stack name;
	public Stack S3;    //sorted name stack
	public Stack score;
	public Stack S4;    //sorted score stack
	public Stack wheel;
	public String playerName;
	public Queue Q1;
	public Queue Q2;
	public static int step;
	public static int playerScore;
	public String choosenWheel;
	public int counter;

	
	Game() throws IOException{
		countries = new File("countries.txt");
		high_sore_table = new File("HighScoreTable.txt");
		alphabet = new File("alphabet.txt");
		country = new Stack(196);
		S1 = new Stack(1000);
		S2 = new Stack(1000);
		name = new Stack(196);
		S3 = new Stack(1000);
		score = new Stack(196);
		S4 = new Stack(1000);
		wheel = new Stack(1000);
		Q1 = new Queue(3000);
		Q2 = new Queue(3000);
		wheel.push("10");
		wheel.push("50");
		wheel.push("100");
		wheel.push("250");
		wheel.push("500");
		wheel.push("1000");
		wheel.push("Double Money");
		wheel.push("Bankrupt");
		step = 1;
		playerScore = 0;
		counter = 0;
		
		fileOperations();  // reads all files and put them in stacks
		sortCountryStack();
		sortHighScoreTable();
		start();
		
	}
	private void start() throws IOException {
		Scanner s = new Scanner(System.in);
		System.out.println("Please enter your name");
		playerName = s.nextLine();  //to take name of the player
		
			number = (int) (Math.random() * 196 + 1);   //to generate a random number
			System.out.println("Randomly generated number: " + number);
			
			int size = S1.size();
			Stack tmp = new Stack(196);
			String choosen_Country = "";
			for(int i = 1; i <= size; i++) {
				if(i == number) {
					choosen_Country = (String) S1.pop();	//to choose country according to number			
				}
				else {
					tmp.push(S1.pop());
				}
			}
			for(int i = 1; i <= size - 1; i++) {
				S1.push(tmp.pop());
			}
			
			for(int i = 0; i < choosen_Country.length(); i++) {   //to write word in Q1
				Q1.enqueue(choosen_Country.charAt(i));
			}
			int nmr = Q1.size();
			for(int i = 0; i < nmr; i++) {     //to write '-' to Q2
				Q2.enqueue('-');
			}
			writeScreen(Q2, S2);
			
			while(true) {   // main loop of the game
				choosenWheel = spiningWheel();
				if(choosenWheel.equalsIgnoreCase("bankrupt")) {
					step++;
					playerScore = 0;
					writeScreen(Q2, S2);
				}
				else {
					counter = 0;
					makingGuess();
					if(choosenWheel.equalsIgnoreCase("Double Money") && counter != 0) {
						playerScore = playerScore*2;
					}	
					else if(counter != 0) {
						playerScore = playerScore + (Integer.parseInt(choosenWheel) * counter);		
					}
					writeScreen(Q2,S2);
				}
				if(isComplete()) {
					writeNewName();
					highScoreTableFile();
					writeHighScoreTable();
					break;
				}
				
			} 
	}
	
	private void fileOperations() {
		try {
			Scanner in = new Scanner(countries);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				line = line.replaceAll("\\W", "");  //remove all punctuations as spaces 
				line = line.toUpperCase(Locale.ENGLISH);
				if (line != null) {
					country.push(line);  //writes all lines in stack
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Scanner in = new Scanner(high_sore_table);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] values = line.split(" ");
				if (line != null) {
					name.push(values[0]);  //writes all lines in stack
					score.push(values[1]);  //writes all lines in stack
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Scanner in = new Scanner(alphabet);
			while(in.hasNextLine()) {
				char line = in.next().charAt(0);   //casting to char type
				if (!(line == ' ')) {
					S2.push(line);    //writes all lines in stack
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void sortCountryStack() {
		Stack tempStack = new Stack(196);
		
		while(!country.isEmpty()) {
			String tmp = (String) country.pop();
			
			while(!tempStack.isEmpty() && ((String) tempStack.isPeek()).compareTo(tmp) > 0 ) {  //if this condition does not satisfy, all words will be written in tempStack stack. 
				country.push(tempStack.pop());                                                  //if it is, until condition satisfies, words will be written in country stack again.
			}
			
			tempStack.push(tmp);
		}
		for(int i = 0; i < 196; i++) {		
			S1.push(tempStack.pop());
		}
		
	}
	
	public void sortHighScoreTable() {
		Stack tempStack = new Stack(1000);
		Stack tempSt = new Stack(1000);
		
		while(!name.isEmpty()) {
			int tmp =Integer.parseInt((String)score.pop());
			String temp = (String) name.pop();
			
			while(!tempStack.isEmpty() && (Integer.parseInt((String)tempStack.isPeek())) > tmp) {  //if this condition does not satisfy, all words will be written in tempStack stack. 
				score.push(String.valueOf(tempStack.pop()));                                       //if it is, until condition satisfies, words will be written in country stack again.
				name.push(tempSt.pop());
			}
			tempStack.push(String.valueOf(tmp));
			tempSt.push(temp);
		}
		for(int i = 0; i < 196; i++) {	
			if(!tempStack.isEmpty()) {
				S4.push(String.valueOf(tempStack.pop()));
				S3.push(tempSt.pop());
			}
		}
	}

	
	public void writeScreen(Queue Q2, Stack S2) {  //writes step, score, alphabet and Q2 on screen
		int nmr = Q2.size();
		Queue temp = new Queue(50);
		for(int i = 0; i < nmr; i++) {
			System.out.print(Q2.peek() + " ");
			temp.enqueue(Q2.dequeue());
		}
		nmr = temp.size();
		for(int i = 0; i < nmr; i++) {
			Q2.enqueue(temp.dequeue());
		}
		
		System.out.print("        " + "Step = " + step);
		System.out.print("        " + "Score = " + playerScore + "        ");
		
		nmr = S2.size();
		Stack tmp = new Stack(50);
		for(int i = 0; i < nmr; i++) {
			System.out.print(S2.isPeek());
			tmp.push(S2.pop());
		}
		nmr = tmp.size();
		for(int i = 0; i < nmr; i++) {
			S2.push(tmp.pop());
		}
		System.out.println();
	}
	
	public String spiningWheel() {
		int number = (int) (Math.random() * 8 + 1);  //assign a random value
		
		Stack tmp = new Stack(196);
		int size = wheel.size();
		String choosingValue = " ";
		for(int i = 1; i <= size; i++) {
			if(i == number ) {  //to check random number and i are equal or not 
				choosingValue = (String) wheel.pop();  //take the random value
				tmp.push(choosingValue);
				break;
			}
			else {
				tmp.push(wheel.pop());
			}
		}
		for(int i = 1; i <= size; i++) {
			if(!tmp.isEmpty()) {
				wheel.push(tmp.pop());
			}
		}
		System.out.println("Wheel = " + choosingValue);
		return choosingValue;
		
	}
		
	public void makingGuess() {
		int number = (int) (Math.random() * 26 + 1);  //generate random number
		Stack tmp = new Stack(196);
		int size = S2.size();
		char guessedLetter = ' ';
		for(int i = 1; i <= size; i++) { 
			if(i == number && (char) S2.isPeek() != ' ') {    //to find guessed letter
				guessedLetter = (char) S2.pop();
				break;
			}
			else if((char) S2.isPeek() == ' ') {
				S2.pop();
				i--;
			}
			else {
				tmp.push(S2.pop());
			}
		}
		for(int i = 1; i <= size; i++) {
			if(!tmp.isEmpty()) {
				S2.push(tmp.pop());
			}
		}
		if(guessedLetter != ' ') {
			System.out.println("Guess: " + guessedLetter); 
			step++;
			Queue temp = new Queue(3000);
			int size2 = Q1.size();
			for(int i = 0; i < size2; i++) {
				
				if((char)Q1.peek() == guessedLetter) {    //to find Q1 has guessed letter or not
					temp.enqueue(Q1.dequeue());
					counter++;    //to find how many guessed letter exist
					writeLetterInQueue2(guessedLetter, i);
					
				}
				else {
					temp.enqueue(Q1.dequeue());
				}
			}
			for(int i = 0; i < size2; i++) {
				if(!temp.isEmpty()) {
					Q1.enqueue(temp.dequeue());
				}
			}
		}
		else if(guessedLetter == ' ') {
			makingGuess();
		}
	}
		
	public void writeLetterInQueue2(char guessedLetter, int position) {  //writes the guessed letter in Q2
		Queue temp = new Queue(196);
		int size2 = Q2.size();
		for(int i = 0; i < size2; i++) {
			if(i == position) {
				Q2.dequeue();
				temp.enqueue(guessedLetter);;
			}
			else {
				temp.enqueue(Q2.dequeue());
			}
		}
		for(int i = 0; i < size2; i++) {
			if(!temp.isEmpty()) {
				Q2.enqueue(temp.dequeue());
			}
		}
	}	
		
	public boolean isComplete() {  //to check word complete or not
		int size = Q2.size();
		Queue tmp = new Queue(196);
		boolean flag = true;
		if(!Q1.isEmpty() && !Q2.isEmpty()) {
			for(int i = 0; i < size; i++) {
				char letter2 = (char) Q2.dequeue();
				tmp.enqueue(letter2);
	 			if(letter2 == '-') {
	 				flag = false;
	 			}	
			}
			for(int i = 0; i < size; i++) {
				Q2.enqueue(tmp.dequeue());
			}
			if(flag) {
				System.out.println("You win $" + playerScore);
			}
		}
		return flag;
	}
		
	public void highScoreTableFile() throws IOException {  //writes High Score Table in file
		if (!high_sore_table.exists()) {    
			high_sore_table.createNewFile();   
        }
		Stack tmp = new Stack(196);
		Stack temp = new Stack(196);
		
		String newline = System.lineSeparator();
        FileWriter fileWriter = new FileWriter(high_sore_table, false);   //it helps to write all high score table again and again.   
        BufferedWriter bWriter = new BufferedWriter(fileWriter); 
		int size = S3.size();
		for(int i = 0; i < size; i++) {
			tmp.push(S3.pop());
			temp.push(S4.pop());
		}
		for(int i = 0; i < size; i++) {
			String name2 = (String)tmp.pop();
			String score2 = (String)temp.pop();
			bWriter.write(name2 + " " + score2 + newline);
			S3.push(name2);
			S4.push(score2);
		}
		bWriter.close(); 
	}
	
	
	private void writeHighScoreTable() {  //writes High Score Table on screen
		System.out.println();
		System.out.println("-High Score Table-");
		Stack temp = new Stack(200);
		Stack temporary = new Stack(200);
		int size1 = S3.size();
		
		for(int i = 0; i < size1; i++) {
			temporary.push(S3.pop());
			temp.push(S4.pop());
		}
		for(int i = 0; i < size1; i++) {
			String name1 = (String)temporary.pop();
			String score1 = (String)temp.pop();
			System.out.println("  " + name1 + " " + score1);
			S3.push(name1);
			S4.push(score1);
		}
	}
		
	public void writeNewName() {  //writes new name on High Score Table.
		if(Integer.parseInt((String)S4.isPeek()) < playerScore ) {
			S4.pop();
			S3.pop();
			Stack temp = new Stack(1000);
			Stack tmp = new Stack(1000);
			while(Integer.parseInt((String)S4.isPeek()) < playerScore && !S4.isEmpty()) {    
				temp.push(S4.pop()); 
				tmp.push(S3.pop());
			}
				temp.push(String.valueOf(playerScore));
				tmp.push(playerName);
			int size1 = temp.size();
			for(int i = 0; i < size1; i++) {
				S3.push(tmp.pop());
				S4.push(temp.pop());
			}
			
		}
	}
}
