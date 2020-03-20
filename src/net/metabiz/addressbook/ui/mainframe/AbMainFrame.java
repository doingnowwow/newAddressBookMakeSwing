package net.metabiz.addressbook.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.mbiz.edt.barcode.table.model.MbizTableModel;
import net.metabiz.addressbook.data.DataConstants;
import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.data.inf.DataReturnInterface;
import net.metabiz.addressbook.data.tableData.UserTableData;
import net.metabiz.addressbook.handler.AbHandler;
import net.metabiz.addressbook.handler.DBHandler;
import net.metabiz.addressbook.handler.FileHandler;
import net.metabiz.addressbook.net.file.FileConnect;
import net.metabiz.addressbook.ui.IntroPageFrame;
import net.metabiz.addressbook.ui.common.dialog.InputAddressDialog;
import net.metabiz.addressbook.ui.common.dialog.InputGroupDailog;
import net.metabiz.addressbook.ui.group.GroupTreePanel;
import net.metabiz.addressbook.ui.userList.UserListTablePanel;
import net.metabiz.addressbook.util.AbUtil;

public class AbMainFrame extends JFrame implements ActionListener, TreeSelectionListener, MouseListener, DataReturnInterface {

	private static final AbMainFrame instance = new AbMainFrame();

	private String returnGroupName = "";
	// ok 버튼 : alert ==0 취소버튼 : alert ==1
	private int alert;
	private String selectType;
	private DefaultMutableTreeNode node;
	private MbizTableModel model;
	private AbHandler abHandler;

	public static AbMainFrame getInstance() {
		return instance;
	}

	/**
	 * Create the frame.
	 */
	public AbMainFrame() {
		jbInit();

	}

	/**
	 * intro에서 넘어오는 메서드
	 * 
	 * @param data
	 * @param intro
	 */
	public void setParentFrame(String data, IntroPageFrame intro) {
		selectType = data;
		if (selectType.equals(DataConstants.SELECT_FILE)) {
			abHandler = FileHandler.getInstance();
		} else if (selectType.equals(DataConstants.SELECT_DB)) {
			abHandler = DBHandler.getInstance();
		}
		introFrame = intro;
		jbInit();
		userTablePanel.setHandler(abHandler);
		// 데이터 초기화
		this.initData();
	}

	public GroupTreePanel getTreePanel() {

		return this.groupTreePanel;
	}

	/**
	 * 그룹,사용자 목록 초기화
	 */
	public void initData() {
		// 그룹 정보
		ArrayList<GroupData> groupList = abHandler.selectGroup();
		this.initTreeData(groupList);
		AbUtil.recodeGroupList = groupList;

		// 사용자정보
		ArrayList<UserData> userList = abHandler.selectUserListByGroup(null);
		this.setTableData(userList);
		this.getUserMaxNum(userList);

		userTablePanel.getTable().addMouseListener(this);
	}

	/**
	 * 유저리스트에서 가장 큰 번호를 찾는 메서드
	 * 
	 * @param userList
	 */
	private void getUserMaxNum(ArrayList<UserData> userList) {
		int userNum = 0;
		int max_num = 0;

		for (int i = 0; i < userList.size(); i++) {
			userNum = userList.get(i).getAd_no();
			if (userNum > max_num) {
				max_num = userNum;
			}
		}

		DataConstants.MAX_USER_NO = max_num;

	}

	/**
	 * 트리 데이터 셋팅해주는 메서드
	 * 
	 * @param groupList
	 */

	private void initTreeData(ArrayList<GroupData> groupList) {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(DataConstants.ROOT_NAME);

		for (GroupData groupData : groupList) {
			node = new DefaultMutableTreeNode(groupData);
			root.add(node);
		}
		tree = new JTree(root);
		groupTreePanel.setTree(tree);

		groupTreePanel.getTree().addTreeSelectionListener(this);
		groupTreePanel.getTree().addMouseListener(this);

	}

	/**
	 * 트리에서 선택한 값 테이블에 넣어주기
	 * 
	 * @param userList
	 */
	public void setTableData(ArrayList<UserData> userList) {

		model = userTablePanel.getModel();

		ArrayList<UserTableData> tableDataList = new ArrayList<UserTableData>();

		for (int i = 0; i < userList.size(); i++) {
			UserTableData tableData = new UserTableData();
			tableData.setUserData(userList.get(i));
			tableDataList.add(tableData);
		}

		model.removeAll();
		model.addDataList(tableDataList);
		model.fireTableDataChanged();

	}

	/**
	 * 사용자 추가/수정 dialog 창 띄어주는 메서드 input == INSERT : 추가 input == UPDATE : 수정
	 * 
	 * @param input
	 */
	private void inputUserDialog(int input) {

		ArrayList<GroupData> groupList = new ArrayList<GroupData>();

		tree = groupTreePanel.getTree();

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);

			GroupData groupListData = (GroupData) node.getUserObject();

			groupList.add(groupListData);

		}

		// 선택한 트리정보 가져오기
		DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		GroupData group = null;
		if (selectNode != null) {
			if (selectNode.getUserObject() instanceof GroupData) {
				group = (GroupData) selectNode.getUserObject();
			}
		}

		if (input == DataConstants.INSERT) {
			// insert
			InputAddressDialog insertUser = new InputAddressDialog();
			insertUser.setLocationRelativeTo(this);
			insertUser.setHandler(abHandler);
			insertUser.setSelectGroup(group);
			insertUser.setComboBox(groupList);
			insertUser.setVisible(true);
		} else {
			// update
			model = userTablePanel.getModel();

			UserTableData updateData = (UserTableData) model.getData(userTablePanel.getTable().getSelectedRow());
			InputAddressDialog insertUser = new InputAddressDialog(updateData);

			insertUser.setLocationRelativeTo(this);
			insertUser.setHandler(abHandler);
			insertUser.setSelectGroup(group);
			insertUser.setComboBox(groupList);
			insertUser.setVisible(true);
		}

	}

	/**
	 * 테이블에 사용자 업데이트
	 * 
	 * @param userData
	 */
	public void updateUserData(UserData userData) {

		model = userTablePanel.getModel();
		model.remove(userTablePanel.getTable().getSelectedRow());

		ArrayList<UserData> userDataList = new ArrayList<UserData>();
		userDataList.add(userData);

		if (abHandler.updateUser(userDataList)) {
			UserTableData tableData = new UserTableData();
			tableData.setUserData(userData);
			model.addData(tableData);
			JOptionPane.showMessageDialog(this, "사용자 수정 성공했습니다", "성공", 1);
			this.selectGroupForUserList();

		} else {
			JOptionPane.showMessageDialog(this, "사용자 수정 실패했습니다", "실패", 2);
		}

	}

	/**
	 * 그룹명 수정하기
	 * 
	 * @param selectTree
	 */
	private void updateGroup(JTree selectTree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectTree.getLastSelectedPathComponent();
		if (node == null) {
			JOptionPane.showMessageDialog(this, "변경할 그룹을 선택하세요", "알림", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (node.toString().equals(DataConstants.ROOT_NAME)) {
			JOptionPane.showMessageDialog(this, "전체그룹은 수정할 수 없습니다.", "알림", JOptionPane.ERROR_MESSAGE);
			return;
		}

		InputGroupDailog groupDialog = new InputGroupDailog(node.toString());
		groupDialog.setInterface(this);
		groupDialog.setLocationRelativeTo(this);
		groupDialog.setModal(true);
		groupDialog.setVisible(true);

		if (alert == 0) {

			// 그룹명 중복검사
			GroupData group = (GroupData) node.getUserObject();
			group.setGroup_name(returnGroupName);

			if (abHandler.updateGroup(group)) {
				JOptionPane.showMessageDialog(this, "그룹 수정이 완료되었습니다.", "성공", 1);
				model = (MbizTableModel) userTablePanel.getTable().getModel();
				model.fireTableDataChanged();
			} else {
				JOptionPane.showMessageDialog(this, "그룹 수정이 실패되었습니다.", "실패", 2);
			}

		}

	}

	/**
	 * 테이블에 데이터를 넣는 메서드
	 * 
	 * @param userData
	 */
	public void insertUserData(UserData userData) {

		model = userTablePanel.getModel();

		if (abHandler.insertUser(userData)) {
			UserTableData tableData = new UserTableData();
			tableData.setUserData(userData);
			model.addData(tableData);
			model.fireTableDataChanged();

			JOptionPane.showMessageDialog(this, "사용자 추가 성공했습니다", "성공", 1);
			this.selectGroupForUserList();
			JScrollPane scroll=	userTablePanel.getScroll();
			scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());

		} else {
			JOptionPane.showMessageDialog(this, "사용자 추가 실패했습니다.", "실패", 2);

		}

	}

	/**
	 * tree 그룹 추가 메서드
	 */
	private void insertGroup() {
		InputGroupDailog group = new InputGroupDailog();
		group.setInterface(this);
		group.setLocationRelativeTo(this);
		group.setModal(true);
		group.setVisible(true);

		if (alert == 0) {
			tree = groupTreePanel.getTree();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

			GroupData groupInsert = new GroupData();
			groupInsert.setGroup_no(this.getMaxGroupNum() + 1);
			groupInsert.setGroup_name(this.returnGroupName);

			DefaultMutableTreeNode newGroup = new DefaultMutableTreeNode(groupInsert);

			((DefaultTreeModel) tree.getModel()).insertNodeInto(newGroup, root, root.getChildCount());
			((DefaultTreeModel) tree.getModel()).reload();

			if (abHandler.insertGroup(groupInsert)) {
				JOptionPane.showMessageDialog(this, "그룹추가가 완료되었습니다.", "성공", 1);
				groupTreePanel.getVerticalScrollBar().setValue(groupTreePanel.getVerticalScrollBar().getMaximum());
				
			} else {
				JOptionPane.showMessageDialog(this, "그룹추가가 실패했습니다.", "실패", 2);

			}

		}

	}

	/**
	 * 그룹데이터의 최대 번호를 찾는 메서드
	 * 
	 * @return
	 */
	private int getMaxGroupNum() {
		int groupNum = 0;
		int max_groupNum = 0;

		tree = groupTreePanel.getTree();

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);

			GroupData groupData = (GroupData) node.getUserObject();

			groupNum = groupData.getGroup_no();

			if (groupNum > max_groupNum) {
				max_groupNum = groupNum;
			}

		}

		return max_groupNum;

	}

	/**
	 * 테이블에 잇는 사용자 삭제
	 */
	private void deleteUser() {
		// 단일삭제
//		model.remove(userTablePanel.getTable().getSelectedRow());

		ArrayList<UserTableData> deleteUserList = new ArrayList<UserTableData>();
		int check = 0;

		// 여러명 삭제
		int tableRowCount = userTablePanel.getTable().getModel().getRowCount();
		this.model = userTablePanel.getModel();
		UserTableData user = new UserTableData();

		int res = JOptionPane.showConfirmDialog(this, "선택된 사용자들을 삭제하시겠습니까?", "삭제확인", JOptionPane.YES_NO_OPTION);

		if (res == JOptionPane.OK_OPTION) {
			for (int i = 0; i < tableRowCount; i++) {
				user = (UserTableData) model.getData(i);
				if (user.isChecked()) {
					check++;
					deleteUserList.add((UserTableData) user);
				}
			}
		}

		if (check == 0) {
			JOptionPane.showMessageDialog(this, "삭제할 정보가 없습니다.", "취소", JOptionPane.OK_OPTION);
		} else {
			// 파일에서 삭제
			if (abHandler.deleteUser(deleteUserList)) {
				// 화면에서 삭제처리
				for (UserTableData userTableData : deleteUserList) {
					this.model.remove(userTableData);
				}
				JOptionPane.showMessageDialog(this, "사용자 삭제가 완료되었습니다.", "성공", 1);
			} else {
				JOptionPane.showMessageDialog(this, "사용자 삭제를 실패했습니다.", "실패", 2);

			}

		}

	}

	/**
	 * tree 에서 그룹 삭제
	 */
	private void deleteGroup() {
		int res = JOptionPane.showConfirmDialog(this, "삭제하면 다시 복원 불가능합니다.삭제하시겠습니까?", "삭제확인", JOptionPane.YES_NO_OPTION);
		if (res == JOptionPane.OK_OPTION) {
			node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			DefaultTreeModel treemodel = (DefaultTreeModel) (tree.getModel());
			TreePath[] paths = tree.getSelectionPaths();

			if (abHandler.deleteGroup((GroupData) node.getUserObject())) {
				for (int i = 0; i < paths.length; i++) {
					node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
					treemodel.removeNodeFromParent(node);
				}
				JOptionPane.showMessageDialog(this, "그룹 삭제가 완료되었습니다.", "성공", 1);
				this.selectGroupForUserList();
			} else {
				JOptionPane.showMessageDialog(this, "그룹 삭제가 실패했습니다.", "실패", 2);
			}
		}
	}

	/**
	 * 트리선택이벤트
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		this.selectGroupForUserList();

	}

	/**
	 * 그룹 선택시 사용자 리스트
	 */
	private void selectGroupForUserList() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		ArrayList<UserData> userList = null;

		if (node == null) {
			userList = abHandler.selectUserListByGroup(null);
			this.setTableData(userList);
		} else {
			GroupData selectGroup = null;
			if (!node.isRoot()) {
				selectGroup = (GroupData) node.getUserObject();
			}
			userList = abHandler.selectUserListByGroup(selectGroup);
			this.setTableData(userList);
		}

	}

	@Override
	public void returnToStringData(String data, int alert) {
		// 인터페이스 그룹명 추가,수정
		this.returnGroupName = data;
		this.alert = alert;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {
			if (e.getSource().equals(groupTreePanel.getTree())) {

				JTree selectTree = (JTree) e.getSource();

				this.updateGroup(selectTree);

			} else if (e.getSource().equals(userTablePanel.getTable())) {
				this.inputUserDialog(DataConstants.UPDATE);
			}

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	protected void processWindowEvent(WindowEvent e) {

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			super.processWindowEvent(e);
			introFrame.setLocationRelativeTo(null);
			introFrame.setVisible(true);
			abHandler.closeProgram();

			this.dispose();

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnInsertGroup)) {
			this.insertGroup();
		} else if (e.getSource().equals(btnDeleteGroup)) {
			this.deleteGroup();
		} else if (e.getSource().equals(btnInsertAddress)) {
			this.inputUserDialog(DataConstants.INSERT);
		} else if (e.getSource().equals(btnDeleteAddress)) {
			this.deleteUser();
		}

	}

	private void jbInit() {

		this.setTitle("주소록");
		this.setMinimumSize(new Dimension(900, 500));
		this.setLayout(new BorderLayout());

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		this.setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);

		///////////////////////////////////////////////////

		// Left Tree
		JPanel leftSplitPanel = new JPanel();
		leftSplitPanel.setLayout(new BorderLayout(0, 0));
		splitPane.setLeftComponent(leftSplitPanel);

		// Left North
		JPanel leftNorthPanel = new JPanel();
		leftNorthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		leftSplitPanel.add(leftNorthPanel, BorderLayout.NORTH);

		JLabel lblGroup = new JLabel("그룹관리");
		lblGroup.setFont(new Font("양재참숯체B", Font.PLAIN, 14));
		leftNorthPanel.add(lblGroup);

		// Left South
		JPanel leftSouthPanel = new JPanel();
		leftSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		leftSplitPanel.add(leftSouthPanel, BorderLayout.SOUTH);

		// Left South Button
		btnInsertGroup = new JButton("그룹추가");
		leftSouthPanel.add(btnInsertGroup);
		btnInsertGroup.addActionListener(this);

		btnDeleteGroup = new JButton("그룹삭제");
		leftSouthPanel.add(btnDeleteGroup);
		btnDeleteGroup.addActionListener(this);

		// Left Tree Scroll Pane
		groupTreePanel = new GroupTreePanel();
		leftSplitPanel.add(groupTreePanel, BorderLayout.CENTER);


		///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////

		// Right Table
		JPanel rightSplitPanel = new JPanel();
		rightSplitPanel.setLayout(new BorderLayout(0, 0));
		splitPane.setRightComponent(rightSplitPanel);

		// Right Table Panel
		userTablePanel = new UserListTablePanel();
		rightSplitPanel.add(userTablePanel, BorderLayout.CENTER);

		// Right South
		JPanel rigthSouthPanel = new JPanel();
		rigthSouthPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		rightSplitPanel.add(rigthSouthPanel, BorderLayout.SOUTH);

		// Right South Button
		btnInsertAddress = new JButton("주소록추가");
		rigthSouthPanel.add(btnInsertAddress);
		btnInsertAddress.addActionListener(this);

		btnDeleteAddress = new JButton("주소록삭제");
		rigthSouthPanel.add(btnDeleteAddress);
		btnDeleteAddress.addActionListener(this);

	}

	private JButton btnInsertGroup;
	private JButton btnDeleteGroup;
	private JButton btnInsertAddress;
	private JButton btnDeleteAddress;

	private IntroPageFrame introFrame;
	private GroupTreePanel groupTreePanel;
	private UserListTablePanel userTablePanel;
	private JTree tree;
	private JTable table;

}
