package com.algorithm.nlp;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//Data mapping algorithm desining class
public class MainUI implements ActionListener {

	// Frame for user interaction
	JFrame mainFrame = new JFrame();

	// creating Main panel
	JPanel MainPanel = new JPanel();

	// creating input panel
	JPanel inputPanel = new JPanel();

	// creating output panel
	JPanel outputPanel = new JPanel();

	// creating search panel
	JPanel searchPanel = new JPanel();

	// Creating card layout
	CardLayout cardLayout = new CardLayout();

	// Creating input fileds
	JTextArea ta, ts;
	JTextField tf;
	JScrollPane sbrText, st;
	JButton btnQuit, search, primary, secondary;
	JButton mainWindow = new JButton("Back");
	JButton back = new JButton("Back");

	// Method for showing the GUI
	public void showWindow() {

		// set position and size for input frame
		mainFrame.setBounds(10, 10, 600, 500);

		// Creating the Textarea
		ta = new JTextArea("", 10, 30);
		ta.setBackground(Color.CYAN);
		ta.setLineWrap(true);
		ta.setFont(new Font(Font.MONOSPACED, 40, 20));

		// Creating the scrollbar
		sbrText = new JScrollPane(ta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		/*
		 * ts = new JTextArea("", 10, 30); ts.setBackground(Color.CYAN);
		 * ts.setLineWrap(true); ts.setFont(new Font(Font.MONOSPACED, 40, 20));
		 * st = new JScrollPane(ts);
		 * st.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 */

		tf = new JTextField("", 20);

		// Creating buttons for search panel
		primary = new JButton("primary");
		secondary = new JButton("secondary");
		primary.addActionListener(this);
		secondary.addActionListener(this);
		primary.setBackground(Color.green);
		secondary.setBackground(Color.green);
		// Added buttons and text field to search panel
		searchPanel.add(tf);
		searchPanel.add(primary);
		searchPanel.add(secondary);
		searchPanel.add(back);
		searchPanel.setBackground(Color.CYAN);

		// Create Quit Button
		btnQuit = new JButton("Process");
		search = new JButton("Search");
		search.addActionListener(this);
		btnQuit.addActionListener(this);
		mainWindow.addActionListener(this);
		back.addActionListener(this);

		// Added input fileds to input panel
		inputPanel.add(sbrText);
		inputPanel.add(btnQuit);
		inputPanel.add(search);
		inputPanel.setBackground(Color.CYAN);

		outputPanel.setBackground(Color.CYAN);

		// Added panels to mainPanel
		MainPanel.setLayout(cardLayout);
		MainPanel.add(inputPanel, "input");
		MainPanel.add(outputPanel, "output");
		MainPanel.add(searchPanel, "search");

		// Add main Panel to frame
		mainFrame.add(MainPanel);
		mainFrame.setBackground(Color.CYAN);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);

	}

	// Method for performing the action taken by user
	public void actionPerformed(ActionEvent ae) {

		// Check action is process
		if (ae.getActionCommand() == "Process") {
			try {

				// Reapint the frame
				mainFrame.repaint();

				// Create object of Lemmatizer class
				Lemmatizer lemmatizer = new Lemmatizer();

				// Feading the input fileds from input panel
				String input = ta.getText();
				String[][] data = lemmatizer.wordMapping(input, mainFrame);

				if (input != null && !input.equals("") && input.length() >= 1) {

					String[] columnNames = { "Table 1", "Table 2", "Probability" };
					// Creating table for output
					JTable outputTable = new JTable(data, columnNames);
					outputTable.setBackground(Color.CYAN);
					outputTable.setRowHeight(10, 20);

					// Creating scrollbar for table
					JScrollPane scroll = new JScrollPane(outputTable);
					scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

					outputPanel.add(scroll);
					outputPanel.add(mainWindow);

					cardLayout.show(MainPanel, "output");
				} else {
					JOptionPane.showMessageDialog(mainFrame, "No input from user");
					cardLayout.show(mainFrame, "input");
				}

			}

			catch (Exception e) {

			}
		}

		// Action performs when user press back button
		if (ae.getActionCommand() == "Back") {
			outputPanel.removeAll();
			ta.setText("");
			primary.setBackground(Color.green);
			secondary.setBackground(Color.green);
			cardLayout.show(MainPanel, "input");
		}
		// Action performs when user press search button
		if (ae.getActionCommand() == "Search") {
			tf.setText("");
			cardLayout.show(MainPanel, "search");
		}

		// Action performs when user press for primary or secondary search
		if (ae.getActionCommand() == "primary" || ae.getActionCommand() == "secondary") {
			String input = tf.getText();
			PrimarySearch ps = new PrimarySearch();
			try {

				// Setting up the colors for buttons
				if (ae.getActionCommand() == "primary") {
					primary.setBackground(Color.red);
					secondary.setBackground(Color.green);
				}
				if (ae.getActionCommand() == "primary" && ps.primarySearch(input, mainFrame)) {
					primary.setBackground(Color.red);
					secondary.setBackground(Color.green);
				} else if (ae.getActionCommand() == "secondary" && ps.secondarySearch(input, mainFrame)) {
					primary.setBackground(Color.green);
					secondary.setBackground(Color.red);

				} else {
					if (ae.getActionCommand() == "primary") {
						primary.setBackground(Color.red);
						secondary.setBackground(Color.green);
					} else {
						primary.setBackground(Color.green);
						secondary.setBackground(Color.red);
					}
			
					JOptionPane.showMessageDialog(mainFrame, "Data field \"" + input + "\" not found");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Main method for the application
	public static void main(String[] args) {

		// Creating the object of data mapping
		MainUI dataMappingObject = new MainUI();
		dataMappingObject.showWindow();
	}
}

