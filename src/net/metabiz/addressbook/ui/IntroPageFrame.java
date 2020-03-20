package net.metabiz.addressbook.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.metabiz.addressbook.data.DataConstants;
import net.metabiz.addressbook.ui.mainframe.AbMainFrame;

public class IntroPageFrame extends JFrame implements ActionListener {

	/**
	 * Create the frame.
	 */
	public IntroPageFrame() {
		this.jbInit();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnFile)) {
			this.goMainFrame(DataConstants.SELECT_FILE);

		} else if (e.getSource().equals(btnDB)) {
			this.goMainFrame(DataConstants.SELECT_DB);
		}

	}

	private void goMainFrame(String data) {

		AbMainFrame main = AbMainFrame.getInstance();
		main.setParentFrame(data, this);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
		this.setVisible(false);
	}

	private void jbInit() {

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 444, 282);
		this.setLayout(new BorderLayout());

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		this.setContentPane(contentPane);

		btnDB = new JButton("데이터베이스");
		btnDB.setBounds(255, 150, 132, 40);
		contentPane.add(btnDB);

		JLabel lblTop = new JLabel("주소록");
		lblTop.setFont(new Font("휴먼모음T", Font.BOLD, 40));
		lblTop.setBounds(158, 18, 139, 61);
		contentPane.add(lblTop);

		JLabel lblQ = new JLabel("실행 할 방식을 선택하세요");
		lblQ.setBounds(133, 89, 157, 40);
		contentPane.add(lblQ);

		btnFile = new JButton("파일");
		btnFile.setBounds(55, 150, 132, 40);
		contentPane.add(btnFile);

		// 파일선택
		btnFile.addActionListener(this);

		// 데이터베이스 선택
		btnDB.addActionListener(this);

	}

	private JButton btnFile;
	private JButton btnDB;

}
