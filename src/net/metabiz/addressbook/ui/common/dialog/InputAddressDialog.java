package net.metabiz.addressbook.ui.common.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.metabiz.addressbook.data.DataConstants;
import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.handler.AbHandler;
import net.metabiz.addressbook.handler.FileHandler;
import net.metabiz.addressbook.ui.mainframe.AbMainFrame;
import net.metabiz.addressbook.util.AbUtil;

public class InputAddressDialog extends JDialog implements ActionListener {

	private UserData userData;
	private String groupName = "";
	private boolean update = false;
	private ArrayList<GroupData> groupList;
	private GroupData selectGroup;
	private AbHandler abHandler;

	public InputAddressDialog() {
		jbInit();
		update = false;
	}

	public InputAddressDialog(UserData data) {
		userData = data;
		jbInit();
		this.updateView();
		this.setupdateData();
		update = true;
	}
	
	public void setHandler(AbHandler abHandler) {
		this.abHandler = abHandler;
	}

	public void setComboBox(ArrayList<GroupData> groupList) {
		this.groupList = groupList;
		groupCombo.removeAll();

		GroupData firstGroup = new GroupData();
		firstGroup.setGroup_no(0);
		firstGroup.setGroup_name("미지정");

		groupCombo.addItem(firstGroup);
		for (int i = 0; i < groupList.size(); i++) {
			groupCombo.addItem(groupList.get(i));
		}
		if (update) {
			txtGroup.setText(AbUtil.getGroupName(userData.getGroup_no(), true, this.groupList));
		} else {
			if (this.selectGroup != null) {
				groupCombo.setSelectedItem(this.selectGroup);
			}
		}
	}

	public void setSelectGroup(GroupData group) {
		this.selectGroup = group;

	}

	private void updateView() {
		this.setTitle("주소록 수정");
		this.btnReset.setVisible(false);
		this.btnInsert.setText("수정");

	}

	/**
	 * 업데이트 데이터셋팅
	 */
	private void setupdateData() {
		txtName.setText(userData.getAd_name());
		txtPhone.setText(userData.getAd_hp());

		if (userData.getAd_mail() != null) {

			if (!userData.getAd_mail().equals("")) {
				txtEmail.setText(userData.getAd_mail().split("@")[0]);
				txtEmail2.setText(userData.getAd_mail().split("@")[1]);
			} else {
				txtEmail.setText("");
				txtEmail2.setText("");
			}
		} else {
			txtEmail.setText("");
			txtEmail2.setText("");
		}

		txtCom.setText(userData.getAd_com());
		txtDepartment.setText(userData.getAd_department());
		txtPosition.setText(userData.getAd_position());
		txtMemo.setText(userData.getAd_memo());

	}

	/**
	 * 전체 텍스트 필드를 지워주는 메서드
	 */
	private void clearTextFiled() {
		txtName.setText("");
		txtPhone.setText("");
		txtEmail.setText("");
		txtEmail2.setText("");
		txtCom.setText("");
		txtDepartment.setText("");
		txtPosition.setText("");
		txtMemo.setText("");
		txtGroup.setText("");
		groupName = "";
	}

	/**
	 * 메일선택
	 */
	private void selectMail() {

		String mail = mailCombo.getSelectedItem().toString();
		if (mail.equals("직접입력하기")) {
			txtEmail2.setText("");
			txtEmail2.setEnabled(true);
		} else {
			txtEmail2.setText(mail);
			txtEmail2.setEnabled(false);
		}

	}

	/**
	 * 그룹선택
	 */
	private boolean flag = true;

	private void selectGroup() {

		String txtGroupFiled = this.txtGroup.getText();

		if (groupCombo.getSelectedIndex() == 0) {
			return;
		} else {

			if (update && flag) {
				txtGroupFiled = "";
				flag = false;
			}
			String combo = ((GroupData) groupCombo.getSelectedItem()).getGroup_name();

			if (this.isSelectedGroup(txtGroupFiled, combo)) {

				if (!txtGroupFiled.isEmpty()) {
					txtGroupFiled += "," + combo;
				} else {
					txtGroupFiled += combo;
				}
			}

		}

		this.txtGroup.setText(txtGroupFiled);
	}

	/**
	 * 이미 선택된 그룹인지 아닌지 알려주는 메서드
	 * 
	 * true : input값을 비교하여 동일한 값이 없을때 , 값이 비어있을때 false : input값을 비교하여 동일한 값이 있을때
	 * 
	 * @param txtGroupFiled ex)A,B,C ....
	 * @param selectedGroup
	 * @return boolean
	 */
	private boolean isSelectedGroup(String txtGroupFiled, String selectedGroup) {

		boolean result = true;

		if (txtGroupFiled.isEmpty()) {
			result = true;
		} else {
			String[] groupList = txtGroupFiled.split(",");

			for (int i = 0; i < groupList.length; i++) {

				if (groupList[i].equals(selectedGroup)) {
					result = false;
					break;
				}
			}
		}
		return result;

	}

	private boolean checkDuplData(String code, String txtFiled) {
		boolean result = false;
		ArrayList<UserData> userList = abHandler.selectUserListBySearch(code, txtFiled);

		if (userList != null) {
			// 널이아니면 사용 불가
			if (userList.size() > 0) {
				// userList.size 가 0보다 크면 중복되는 번호
				result = true;
			}
		}
		return result;
	}

	/**
	 * 사용자 등록
	 */
	private void insertUser() {

		UserData user = new UserData();

		// 이름 필수 입력 발리데이션
		if (txtName.getText().length() <= 0) {
			JOptionPane.showMessageDialog(this, "이릅입력은 필수입니다. \n이름을입력해주세요\n", "경고", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// 핸드폰번호 or 이메일 입력 발리데이션
		if (txtPhone.getText().length() <= 0 && txtEmail.getText().length() <= 0) {

			JOptionPane.showMessageDialog(this, "핸드폰번호 또는 이메일  둘중 하나는 \n필수로 입력사항입니다.\n 입력해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
			return;

		} else if (txtEmail.getText().trim().length() > 1 && txtEmail2.getText().length() <= 0) {
			JOptionPane.showMessageDialog(this, "이메일 주소를 선택하거나 입력해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 핸드폰
		if (txtPhone.getText().trim().length() > 0) {
			boolean no = false;
			no = isPhone(txtPhone.getText());

			if (no == false) {
				JOptionPane.showMessageDialog(this, "핸드폰 번호 입력 형식이 잘못되었습니다\n 예)010-1111-4444.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// 기본핸드폰번호, 변경된번호 비교
			if (!update) {
				if (this.checkDuplData("hp", txtPhone.getText())) {
					JOptionPane.showMessageDialog(this, "해당 번호가 이미 존재합니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				if (userData.getAd_hp() != null) {
					if (!userData.getAd_hp().equals("")) {
						if (!txtPhone.getText().trim().equals("")) {
							if (!userData.getAd_hp().equals(txtPhone.getText())) {
								if (this.checkDuplData("hp", txtPhone.getText())) {
									JOptionPane.showMessageDialog(this, "해당 번호가 이미 존재합니다.", "경고", JOptionPane.ERROR_MESSAGE);
									return;
								}
							}
						}
					}
				}
			}
		}

		// 메일
		String userMail ="";
		
		if (txtEmail.getText().trim().length() > 0) {
			boolean no = false;
			no = isMailId(txtEmail.getText());

			if (no == false) {
				JOptionPane.showMessageDialog(this, "이메일 아이디가 잘못되었습니다. 영어,숫자로만 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// 기존 메일, 변경된 메일 비교

			userMail = txtEmail.getText() + "@" + txtEmail2.getText();
			
			if (!update) {
				if (this.checkDuplData("mail", userMail)) {
					JOptionPane.showMessageDialog(this, "해당 이메일은 이미 존재합니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				if (userData.getAd_mail() != null) {
					if (!userData.getAd_mail().equals("")) {
						if (!txtEmail.getText().trim().equals("")) {
							if (!userData.getAd_mail().equals(userMail)) {
								if (this.checkDuplData("mail", userMail)) {
									JOptionPane.showMessageDialog(this, "해당 이메일은 이미 존재합니다.", "경고", JOptionPane.ERROR_MESSAGE);
									return;
								}
							}
						}
					}
				}
			}
		}

		// 값 보내기
		if (update) {
			user.setAd_no(userData.getAd_no());
		} else {

			int max_userNum = DataConstants.MAX_USER_NO + 1;
			DataConstants.MAX_USER_NO = max_userNum;
			user.setAd_no(max_userNum);
		}

		user.setAd_name(txtName.getText());
		user.setAd_hp(txtPhone.getText());

		if (txtEmail.getText().trim().length() > 1) {
			user.setAd_mail(userMail);
		} else {
			user.setAd_mail("");
		}
		user.setAd_com(txtCom.getText());
		user.setAd_department(txtDepartment.getText());
		user.setAd_position(txtPosition.getText());
		user.setAd_memo(txtMemo.getText());
		user.setGroup_no(AbUtil.getGroupName(txtGroup.getText(), false, groupList));

		if (update) {
			AbMainFrame.getInstance().updateUserData(user);
		} else {
			AbMainFrame.getInstance().insertUserData(user);
		}
		this.dispose();
	}

	/**
	 * 
	 * 핸드폰 유효성 검사
	 * 
	 * @param hp
	 * @return
	 */
	private boolean isPhone(String hp) {

		Pattern p = Pattern.compile("^\\d{3,4}-\\d{3,4}-\\d{4}$");

		Matcher m = p.matcher(hp);

		if (m.find()) {

			return true;

		} else {
			return false;
		}
	}

	/**
	 * 메일아이디 확인
	 * 
	 * @param mailid
	 * @return
	 */
	private boolean isMailId(String mailid) {

		Pattern p = Pattern.compile("^[0-9a-zA-Z][0-9a-zA-Z\\_\\-\\.\\+]+[0-9a-zA-Z]$");

		Matcher m = p.matcher(mailid);

		if (m.find()) {

			return true;

		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnClose)) {
			// 닫기버튼이벤트
			this.dispose();
		} else if (e.getSource().equals(btnReset)) {
			// 초기화 버튼 이벤트
			this.clearTextFiled();
		} else if (e.getSource().equals(btnInsert)) {
			// 사용자추가
			this.insertUser();
		} else if (e.getSource().equals(mailCombo)) {
			// 메일선택이벤트
			this.selectMail();
		} else if (e.getSource().equals(groupCombo)) {
			// 그룹선택
			this.selectGroup();

		}

	}

	public void jbInit() {

		this.setSize(450, 390);
		this.setLayout(new BorderLayout());
		this.setTitle("주소록 추가");
		this.setModal(true);
		this.setResizable(false);

		// 메인페널
		JPanel mainContentPanel = new JPanel();
		mainContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPanel.setLayout(new BorderLayout(0, 0));
		this.setContentPane(mainContentPanel);

		// 가운데 부분 시작
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);

		// 라벨 >>>start=====>>>

		Font lblFont = new Font("굴림", Font.BOLD, 14);
		int x;
		int y = 25;
		int height = 25;
		int yGap = 30;

		// 이름
		JLabel lblName = new JLabel("이   름");
		lblName.setBounds(25, y, 70, height);
		lblName.setFont(lblFont);
		lblName.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblName);
		txtName = new JTextField();
		txtName.setBounds(100, y, 310, height);
		centerPanel.add(txtName);

		y = y + yGap;
		// 핸드폰번호
		JLabel lblPhone = new JLabel("폰번호");
		lblPhone.setBounds(25, y, 100, height);
		lblPhone.setFont(lblFont);
		lblPhone.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblPhone);
		txtPhone = new JTextField();
		txtPhone.setBounds(100, y, 310, height);
		centerPanel.add(txtPhone);

		y = y + yGap;
		// 이메일
		JLabel lblEmail = new JLabel("이메일");
		lblEmail.setBounds(25, y, 100, height);
		lblEmail.setFont(lblFont);
		lblEmail.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblEmail);
		txtEmail = new JTextField();
		txtEmail.setBounds(100, y, 80, height);
		centerPanel.add(txtEmail);

		JLabel lblGol = new JLabel(" @ ");
		lblGol.setBounds(180, y, 100, height);
		centerPanel.add(lblGol);
		txtEmail2 = new JTextField();
		txtEmail2.setBounds(200, y, 100, height);
		txtEmail2.setEnabled(false);
		centerPanel.add(txtEmail2);

		// 이메일리스트
		String mailList[] = { "naver.com", "gmail.com", "kakao.com", "hanmail.net", "직접입력하기" };
		mailCombo = new JComboBox(mailList);
		mailCombo.setBounds(310, y, 100, height);
		centerPanel.add(mailCombo);

		y = y + yGap;
		// 회사
		JLabel lblCom = new JLabel("회   사");
		lblCom.setBounds(25, y, 100, height);
		lblCom.setFont(lblFont);
		lblCom.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblCom);
		txtCom = new JTextField();
		txtCom.setBounds(100, y, 310, height);
		centerPanel.add(txtCom);

		y = y + yGap;
		// 부서
		JLabel lblDepartment = new JLabel("부   서");
		lblDepartment.setBounds(25, y, 100, height);
		lblDepartment.setFont(lblFont);
		lblDepartment.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblDepartment);
		txtDepartment = new JTextField();
		txtDepartment.setBounds(100, y, 310, height);
		centerPanel.add(txtDepartment);

		y = y + yGap;
		// 직책
		JLabel lblPosition = new JLabel("직   책");
		lblPosition.setBounds(25, y, 100, height);
		lblPosition.setFont(lblFont);
		lblPosition.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblPosition);
		txtPosition = new JTextField();
		txtPosition.setBounds(100, y, 310, height);
		centerPanel.add(txtPosition);

		y = y + yGap;
		// 메모
		JLabel lblMemo = new JLabel("메   모");
		lblMemo.setBounds(25, y, 100, height);
		lblMemo.setFont(lblFont);
		lblPosition.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblMemo);

		txtMemo = new JTextArea();
		txtMemo.setBounds(100, y, 310, height * 2);

		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		txtMemo.setBorder(lineBorder);

		centerPanel.add(txtMemo);

		y = y + yGap + height;
		// 그룹
		JLabel lblGroup = new JLabel("그   룹");
		lblGroup.setBounds(25, y, 100, height);
		lblGroup.setFont(lblFont);
		lblGroup.setHorizontalAlignment(JLabel.LEFT);
		centerPanel.add(lblGroup);
		txtGroup = new JTextField();
		txtGroup.setBounds(100, y, 200, height);
		centerPanel.add(txtGroup);
		txtGroup.setEditable(false);

		groupCombo = new JComboBox<GroupData>();
		groupCombo.setBounds(310, y, 100, height);
		centerPanel.add(groupCombo);

		mainContentPanel.add(centerPanel, BorderLayout.CENTER);

		// South
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnReset = new JButton("초기화");
		btnInsert = new JButton("등록");
		btnClose = new JButton("닫기");

		Dimension btsize = new Dimension(70, 30);

		btnReset.setPreferredSize(btsize);
		btnInsert.setPreferredSize(btsize);
		btnClose.setPreferredSize(btsize);

		southPanel.add(btnReset);
		southPanel.add(btnInsert);
		southPanel.add(btnClose);
		southPanel.add(Box.createHorizontalStrut(10));
		southPanel.add(Box.createVerticalStrut(40));
		mainContentPanel.add(southPanel, BorderLayout.SOUTH);

		// 메일 콤보박스 이벤트
		mailCombo.addActionListener(this);
		// 그룹 콤보박스 이벤트
		groupCombo.addActionListener(this);
		// 닫기버튼이벤트
		btnClose.addActionListener(this);
		// 초기화 버튼 이벤트
		btnReset.addActionListener(this);
		// 주소록 등버튼 이벤트
		btnInsert.addActionListener(this);

	}

	private JTextField txtName;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtEmail2;
	private JTextField txtCom;
	private JTextField txtDepartment;
	private JTextField txtPosition;
	private JTextArea txtMemo;
	private JTextField txtGroup;
	private JComboBox mailCombo;
	private JComboBox<GroupData> groupCombo;

	private JButton btnReset;
	private JButton btnInsert;
	private JButton btnClose;

}
