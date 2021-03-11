package Questions;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Question class Reads a list of multiple choice question from a file and store its data in appropriate collection, a
 * llowing for a random question to be accessed.
 *
 */

public class Question {
	//mathsQuestions is an arrayList of String that stores all data relating to Maths questions including question text, options and answers.
	private static ArrayList<String> mathsQuestions = new ArrayList<String>();
	//englishQuestions is an arrayList of String that stores all data relating to english questions including question text, options and answers.
	private static ArrayList<String> englishQuestions = new ArrayList<String>();
	
	private int numberOfQuestions=21;
	private int linesperQuestion=7;
	
	
	public Question() {
		englishQuestions();
		mathsQuestions();
	}
	
	/**
	 * Reads all the Maths questions from the file mathsQuestions.txt and store its content on the ArrayList mathsQuestions
	 */
	public  void mathsQuestions() {
		try {
			BufferedReader br = null;
			FileHandle fr= Gdx.files.internal("mathsQuestions.txt");
			br = new BufferedReader(new InputStreamReader(fr.read()));
			String mCurrentLine="";
			while((mCurrentLine = br.readLine()) != null) {
				mathsQuestions.add(mCurrentLine);
				}
			} catch (Exception e) {
				e.printStackTrace();
				}
	}
	
	
	/**
	 * Reads all the English questions from the file englishQuestions.txt and store its content on the ArrayList mathsQuestions
	 */
	public void englishQuestions() {
	try {
		BufferedReader br = null;
		FileHandle fr= Gdx.files.internal("englishQuestions.txt");
		br = new BufferedReader(new InputStreamReader(fr.read()));
		
		String eCurrentLine="";
		while((eCurrentLine = br.readLine()) != null) {
			englishQuestions.add(eCurrentLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
	
		}
	
	/**
	 * 
	 * @return an Array lList on strings, where index 0 is the question text, index 1 to 4 the 4 possible options and at index 5 the answer.
	 */
		
	public ArrayList<String> randomQuestionGenerator(int difficultyLevel) {
		///ramdom number generator
		Random r = new Random();
		
		int randomSubject = r.nextInt(2);
		int randomQuestion = new Random().nextInt(this.numberOfQuestions/3)*this.linesperQuestion;
		
		
		if (difficultyLevel==2) {
			randomQuestion+=(numberOfQuestions/3)*linesperQuestion;
			System.out.println("yessir");
		}else if (difficultyLevel==3) {
			randomQuestion+= (2*numberOfQuestions/3)*linesperQuestion;
		}
		
		
		
		ArrayList<String> tempQuestion = new ArrayList<String>();
		
	
		//if the random number generator is below half the amount of lines of all questions combined, the question chose would be a math
		//question otherwise it would be an English question
		if (randomSubject==1){
			for (int questionLine = randomQuestion; questionLine < randomQuestion +6; questionLine++) {
				tempQuestion.add(mathsQuestions.get(questionLine));
			}
		}else {
			for (int questionLine = randomQuestion; questionLine < randomQuestion +6; questionLine++) {
				tempQuestion.add(englishQuestions.get(questionLine));
			}
		}
		return tempQuestion;
	}
	
	
	
	
	


}
