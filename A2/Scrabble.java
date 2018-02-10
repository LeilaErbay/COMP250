
// STUDENT_NAME: LEILA ERBAY
//STUDENT_ID:260672158

import java.util.*;

import java.io.*;

public class Scrabble {

	static HashSet<String> myDictionary; // this is where the words of the
											// dictionary are stored

	// DO NOT CHANGE THIS METHOD
	// Reads dictionary from file
	public static void readDictionaryFromFile(String fileName) throws Exception {
		myDictionary = new HashSet<String>();

		BufferedReader myFileReader = new BufferedReader(new FileReader(fileName));

		String word;
		while ((word = myFileReader.readLine()) != null)
			myDictionary.add(word);

		myFileReader.close();
	}

	/*
	 * Arguments: char availableLetters[] : array of characters containing the
	 * letters that remain available String prefix : Word assembled to date
	 * Returns: String corresponding to the longest English word starting with
	 * prefix, completed with zero or more letters from availableLetters. If no
	 * such word exists, it returns the String ""
	 */
	public static String longestWord(char[] availableLetters, String prefix) {

		/* WRITE YOUR CODE HERE */
		String longest = "";
		String tmpLongest = "";
		

		//base case
		if (myDictionary.contains(prefix)) {
			longest = prefix;
		}

		for (int i = 0; i < availableLetters.length; i++) {
			//temporary variable to hold the most recent longest word
			tmpLongest = prefix + availableLetters[i];
			
			//a new char array to store the letters that have not yet been removed
			char[] charArray = new char[availableLetters.length - 1];
			int indexToRemove = i;
			for (int j = 0; j < indexToRemove; j++) {
				charArray[j] = availableLetters[j];
			}
			for (int z = indexToRemove + 1; z < availableLetters.length; z++) {
				charArray[z-1] = availableLetters[z];
			}
			
			//recursively call longestWord with the inputs of the temporary longest word 
			//and newly updated char array
			tmpLongest = longestWord(charArray, tmpLongest);
			
			//if the temporary longest word is longer than the current longest word 
			//(that will be updated each time by the if-statement) then longest word is the temporary
			//longest word
			if (tmpLongest.length() > longest.length()) {
				longest = tmpLongest;
			}

		}
		return longest;

	}
	

	/*
	 * main method You should not need to change anything here.
	 */
	public static void main(String args[]) throws Exception {

		// First, read the dictionary
		try {
			readDictionaryFromFile("englishDictionary.txt");
		} catch (Exception e) {
			System.out.println("Error reading the dictionary: " + e);
		}

		// Ask user to type in letters
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		char letters[];
		do {
			System.out.println("Enter your letters (no spaces or commas):");
			//System.out.println(myDictionary.contains("cat"));
			letters = keyboard.readLine().toCharArray();

			// now, enumerate the words that can be formed
			String longest = longestWord(letters, "");
			

			System.out.println("The longest word is " + longest);
		} while (letters.length != 0);

		keyboard.close();

	}
}
