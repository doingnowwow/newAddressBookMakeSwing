package net.metabiz.addressbook.ui.common.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.inf.DataReturnInterface;
import net.metabiz.addressbook.ui.mainframe.AbMainFrame;

public class InputGroupDailog extends JDialog implements ActionListener, WindowListener {

	private DataReturnInterface interData;
	private String groupName;
	// ok 버튼 : alert ==0 ///취소버튼 : alert ==1 확인/취소
	public int alert = 0;

	public InputGroupDailog() {
		this.Jbinit();
	}

	public InputGroupDailog(String name) {
		this.groupName = name;
		Jbinit();
		this.updateView();
		this.updateName();
	}

	private void updateName() {
		txtInput.setText(groupName);
	}

	private void updateView() {
		this.setTitle("그룹 수정");
		this.btnOK.setText("수정");

	}

	public void setInterface(DataReturnInterface data) {
		this.interData = data;

	}

	/**
	 * 그룹추가
	 */
	private void insertGroup() {

		if (txtInput.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "입력창은 공백일 수 없습니다.", "알림", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			if (!this.checkGroupName(txtInput.getText())) {
				interData.returnToStringData(txtInput.getText(), alert);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "그룹명이 중복입니다.", "실패", 2);
			}
		}

	}

	/**
	 * 그룹명 중복 체크
	 * 
	 * true : 중복 false : 사용가능
	 * 
	 * @param groupName
	 * @return boolean
	 */
	private boolean checkGroupName(String groupName) {
		boolean ok = false;

		JTree tree = AbMainFrame.getInstance().getTreePanel().getTree();

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			GroupData groupData = (GroupData) node.getUserObject();

			if (groupName.equals(groupData.getGroup_name())) {
				ok = true;
				break;
			}

		}

		return ok;

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// 윈도우닫기
		this.alert = 1;
		interData.returnToStringData(null, alert);
		this.dispose();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnOK)) {
			this.insertGroup();

		} else if (e.getSource().equals(btnCancel)) {
			this.alert = 1;
			interData.returnToStringData(null, alert);
			this.dispose();
		}
	}

	private void Jbinit() {

		this.setTitle("그룹 추가");
		this.setLayout(null);
		this.setSize(300, 150);
		this.setResizable(false);

		JLabel lblInput = new JLabel("입   력");
		lblInput.setBounds(25, 25, 68, 30);
		lblInput.setHorizontalAlignment(JLabel.LEFT);
		this.add(lblInput);
		txtInput = new JTextField();
		txtInput.setBounds(93, 25, 171, 30);
		this.add(txtInput);

		btnOK = new JButton();
		btnOK.setText("확인");
		btnOK.setBounds(113, 70, 70, 30);
		this.add(btnOK);

		btnCancel = new JButton();
		btnCancel.setText("취소");
		btnCancel.setBounds(193, 70, 70, 30);
		this.add(btnCancel);

		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);

		this.addWindowListener(this);

	}

	private JButton btnOK;
	private JButton btnCancel;
	private JTextField txtInput;

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

}
