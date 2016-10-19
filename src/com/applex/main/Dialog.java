package com.applex.main;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Dialog {
	
	public static void showOpenFileErrorDialog(Component parent) {
		JOptionPane.showMessageDialog(parent, 
				DefaultValues.OPEN_FILE_ERROR_MSG, DefaultValues.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}

	public static void showAboutDialog(Component parent) {
		JOptionPane.showMessageDialog(parent, 
				DefaultValues.ABOUT_MSG, DefaultValues.ABOUT_TITLE, JOptionPane.PLAIN_MESSAGE);
	}
	
}
