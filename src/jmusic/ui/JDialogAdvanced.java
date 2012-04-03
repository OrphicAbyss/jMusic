/*
 * Copyright (C) 2012 Chris Hallson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmusic.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jmusic.jMusicController;

/**
 * Helper for displaying error message dialogs.
 * 
 * @author DrLabman
 */
public class JDialogAdvanced {
	static String linkValue;
	
	private static void showDialogWithLink(String text, String link, String linkText){
		// Label objec to display the text
		JLabel label = new JLabel();
		label.setText(text);
		// Button object to display the link and open in browser when clicked
		JButton button = new JButton();
		button.setText(linkText);
		linkValue = link;
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI(linkValue));
				} catch (URISyntaxException ex){
					Logger.getLogger(jMusicController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(jMusicController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		// Panel object to show both the label and button together
		JPanel panel = new JPanel();
		BorderLayout layout = new BorderLayout();
		panel.setLayout(layout);
		panel.add(label);
		panel.add(button);
		layout.addLayoutComponent(label, BorderLayout.CENTER);
		layout.addLayoutComponent(button, BorderLayout.SOUTH);
		// Dispaly the message
		JOptionPane.showMessageDialog(null, panel, "jMusic Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Display a message telling the user that only a 32bit version of GStreamer
	 * is available on Windows and that they need to use the 32bit version of
	 * java.
	 */
	public static void showWindowsJava64bitError(){
		showDialogWithLink("<html>Unable to find the GStreamer "
				+ "libraries.<p><p>Because only 32bit builds of GStreamer "
				+ "are provided for windows it is<p>required that a 32bit "
				+ "Java Virtual Machine is used to run " + jMusicController.PLAYER_NAME 
				+ ".<p><p>You can download the 32bit version of java at: ",
				"http://www.java.com",
				"http://www.java.com");
	}
	
	/**
	 * Display a message telling the user that they need the GStreamer libraries
	 * and a link to where they can download them from.
	 */
	public static void showWindowsJava32bitError(){
		showDialogWithLink("<html>Unable to find the GStreamer libraries.<p><p>"
				+ "GStreamer libraries are required to run " + jMusicController.PLAYER_NAME
				+ "<p>You can download the GStreamer libraries at:",
				"http://code.google.com/p/ossbuild/downloads/detail?name=GStreamer-WinBuilds-GPL-x86.msi",
				"Download GStreamer Library");
	}
	
	/**
	 * Show a generic error telling the user that they are missing the GStreamer
	 * libraries.
	 */
	public static void showOtherPlatformError(){
		JOptionPane.showMessageDialog(null, "Unable to find the GStreamer "
						+ "libraries.\n\nCheck that GStreamer is installed and match "
						+ "the\narchitecture (32bit or 64bit) of the Java Virtual "
						+ "Machine.");	
	}
}
