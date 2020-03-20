package net.metabiz.addressbook.main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import net.metabiz.addressbook.ui.IntroPageFrame;

public class NewAddressBookMain {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IntroPageFrame frame = new IntroPageFrame();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
