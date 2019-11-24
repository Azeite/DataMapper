package com.algorithm.nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

//Searching class
public class PrimarySearch {

	// standard searching method-primary search
	public boolean primarySearch(String input, JFrame mainFrame) throws Exception {
		// return when empty input fileds
		if (input.equals(""))
			return false;
		// convert input to lowercase
		input = input.toLowerCase();
		String[] tokens = input.split("\\s+");

		// Parts-Of-Speech Tagging
		// reading parts-of-speech model to a stream
		InputStream posModelIn = new FileInputStream("machineLearningModel/en-pos-maxent.bin");

		// loading the parts-of-speech model from stream
		POSModel posModel = new POSModel(posModelIn);

		// initializing the parts-of-speech tagger with model
		POSTaggerME posTagger = new POSTaggerME(posModel);

		// reading the pos tags
		String tags[] = posTagger.tag(tokens);

		// reading the probability
		double[] probs = posTagger.probs();

		// loading the dictionary to input stream
		InputStream dictLemmatizer = new FileInputStream("data/data.txt");

		// loading the lemmatizer with dictionary
		DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

		// Loading the lemmas
		String[] lemmas = lemmatizer.lemmatize(tokens, tags);

		// Printing the result
		if (lemmas[0].length() == 1) {
			return false;
		} else {
			JOptionPane.showMessageDialog(mainFrame,
					//the probability has been removed from here...
					"Data field \"" + input + "\" maps with \"" + lemmas[0] + "\" with the probability ");
			return true;
		}

	}

	// Secondary search method
	public boolean secondarySearch(String input, JFrame mainFrame) throws Exception {

		// return when empty input fileds
		if (input.equals(""))
			return false;
		// convert input to lowercase
		input = input.toLowerCase();
		int index;
		if (input.length() >= 1 && input.length() <= 3) {
			index = input.length();
		} else {
			index = 4;
		}

		// Load dataset to input stream
		FileInputStream fis = new FileInputStream("data/data.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		// Read the dataset
		String line = br.readLine();
		boolean b = true;

		// iterating the input
		while (line != null) {

			// find lemma when input length is >=1 and <=3 with full word
			if (input.length() >= 1 && input.length() <= 3) {

				if (input.equals(line.substring(0, line.indexOf("	")))) {
					br.close();
					fis.close();
					JOptionPane.showMessageDialog(mainFrame,
							"Data field \"" + input + "\" maps with " + line.substring(0, line.indexOf("	")));
					return true;
				}

			}
			// Find field using some input fileds characters: example - mfr
			// -manifacturar
			if (line.charAt(0) == input.charAt(0)) {

				// Checking the input fields with dataset
				if (line.substring(0, line.indexOf("	")).equals(input.substring(0, index))) {
					br.close();
					fis.close();

					// show the output
					JOptionPane.showMessageDialog(mainFrame,
							"Data field \"" + input + "\" maps with " + line.substring(0, line.indexOf("	")));
					return true;
				}

				b = true;
				char[] str = input.substring(0, index).toCharArray();
				String str1 = line.substring(0, line.indexOf("	"));

				int ind = -1;
				for (int i = 0; i < str.length; i++) {
					if (str1.lastIndexOf(str[i]) > ind) {
						ind = str1.indexOf(str[i]);
					} else {
						b = false;
						break;
					}
				}

				// show the output
				if (b) {
					br.close();
					fis.close();
					JOptionPane.showMessageDialog(mainFrame, "Data field \"" + input + "\" maps with " + str1);
					return true;
				}
			}

			// reading the records from dataset
			line = br.readLine();
		}

		// close file pointer
		br.close();
		fis.close();
		return false;

	}

}
