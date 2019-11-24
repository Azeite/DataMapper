package com.algorithm.nlp;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

//Dictionary Lemmatizer class 
public class Lemmatizer {

	// Method for mapping the fields with correct output fileds
	public String[][] wordMapping(String input, JFrame mainFrame) throws IOException {
		if (input.equals(""))
			return null;
		// convert input to lowercase
		input = input.toLowerCase();
		String[] tokens = input.split("\\s+");

		// Parts-Of-Speech Tagging
		// reading parts-of-speech model to a stream
		InputStream posModelIn = new FileInputStream("machineLearningModel/en-pos-maxent.bin");

		// loading the parts-of-speech model from stream
		POSModel posModel = new POSModel(posModelIn);

		// Tagger tagging the tokens
		POSTaggerME posTagger = new POSTaggerME(posModel);

		// Loading the pos tags with probability
		String tags[] = posTagger.tag(tokens);
		double[] probs = posTagger.probs();

		// loading the dictionary to input stream
		InputStream dictLemmatizer = new FileInputStream("data/data.txt");

		// loading the lemmatizer with dictionary
		DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

		// finding the lemmas
		String[] lemmas = lemmatizer.lemmatize(tokens, tags);

		// open dataset file in append mode
		FileWriter fw = new FileWriter("data/data.txt", true);
		PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
		String[][] data;

		if (tokens.length == 1)
			data = new String[2][3];
		else
			data = new String[tokens.length][3];
		boolean b = false;

		// iterating the input fileds for finding the correct lemma
		for (int i = 0; i < tokens.length; i++) {

			if (lemmas[i].length() == 1) {
				input = tokens[i];
				b = true;

				int index;
				if (tokens[i].length() >= 1 && tokens[i].length() <= 3) {
					index = tokens[i].length();
				} else {
					index = 4;
				}

				// Read the dataset file for finding the result using secondary
				// method if not founded by primary method
				FileInputStream fis = new FileInputStream("data/data.txt");

				BufferedReader br = new BufferedReader(new InputStreamReader(fis));

				// Reading dataset records
				String line = br.readLine();

				// iterating the input fields in dataset
				while (line != null) {

					// find lemma when input length is >=1 and <=3
					if (input.length() >= 1 && input.length() <= 3) {
						if (input.equals(line.substring(0, line.indexOf("	")))) {
							br.close();
							fis.close();
							lemmas[i] = line.substring(line.lastIndexOf("	"), line.length());
							break;
						}

					} else {
						if (line.substring(0, index).equals(input.substring(0, index))) {
							br.close();
							fis.close();
							lemmas[i] = line.substring(line.lastIndexOf("	"), line.length());
							break;
						}
					}

					line = br.readLine();
				}
				br.close();
				fis.close();
			}
			if (b) {
				b = false;
				int result;
				if (lemmas[i].length() != 1)
					result = JOptionPane.showConfirmDialog(mainFrame, input + " maps with " + lemmas[i]);
				else
					result = JOptionPane.showConfirmDialog(mainFrame, input + " maps with " + tokens[i]);
				if (result == 1) {

					// Edit the fields when user say no
					lemmas[i] = JOptionPane.showInputDialog("Enter lemma");
					if (lemmas[i] != null) {
						for (int ind = 0; ind < tags.length; ind++) {
							pw.println(tokens[i] + "	" + tags[ind] + "	" + lemmas[i]);
						}
						JOptionPane.showMessageDialog(mainFrame, "Unmapping fileds added to dataset successfully");
					} else {
						JOptionPane.showMessageDialog(mainFrame, input + " not added to dataset");
						lemmas[i] = tokens[i];
					}
				} else {
					lemmas[i] = tokens[i];
				}

			}

			// Added data to String array
			if (tokens[i] != null) {

				data[i][0] = tokens[i];
				if (lemmas[i].length() == 1) {
					data[i][1] = tokens[i];
				} else {
					data[i][1] = lemmas[i];
				}
				data[i][2] = probs[i] + "";
			}

		}
		// closing the file pointers
		pw.flush();
		pw.close();
		fw.close();

		// Return output data
		return (data);
	}

}
