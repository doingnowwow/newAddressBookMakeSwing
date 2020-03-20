package net.metabiz.addressbook.ui.userList;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.mbiz.edt.barcode.table.model.MbizTableModel;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.data.tableData.UserTableData;
import net.metabiz.addressbook.handler.AbHandler;
import net.metabiz.addressbook.handler.FileHandler;
import net.metabiz.addressbook.ui.common.renderer.TableCBCellRenderer;
import net.metabiz.addressbook.ui.mainframe.AbMainFrame;

public class UserListTablePanel extends JPanel implements ActionListener {

	// 테이블 초기 셋팅
	String colNames[] = { " ", "이름", "핸드폰번호", "이메일", "회사", "부서", "직책", "그룹", "메모" };
	int cols[] = { 50, 100, 200, 200, 130, 150, 100, 150, 220 };
	private AbHandler abHandler;

	public UserListTablePanel() {
		model = new MbizTableModel(colNames, cols);
		table = new JTable(model);
		jbInit();
	}

	public MbizTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	public void setHandler(AbHandler abHandler) {
		this.abHandler = abHandler;
	}
	
	public JScrollPane getScroll() {
		return tableScrlPane;
	}

//	public void setTable(JTable table, MbizTableModel model) {
//		this.table = table;
//		this.model = model;
//		jbInit();
//	}

	public void setTable(JTable table) {
		this.table = table;
		jbInit();
	}

	private void searchKeyword() {
		String keyword = txtSearch.getText();

		ArrayList<UserData> userList = null;
		userList = abHandler.selectUserListBySearch(null, keyword);

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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnSearch)) {
			this.searchKeyword();
		}

	}

	private void jbInit() {

		this.setLayout(new BorderLayout());
		// BorderLayout North
		notrhPanel = new JPanel();
		notrhPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 0));

		txtSearch = new JTextField();
		notrhPanel.add(txtSearch);
		txtSearch.setColumns(10);

		btnSearch = new JButton("검색");
		notrhPanel.add(btnSearch);

		// BorderLayout Center
		tableScrlPane = new JScrollPane();

		model.setEditColumn(0);
		model.setSorting(false);

		JCheckBox checkBox = new JCheckBox();

		DefaultCellEditor checkboxRend = new DefaultCellEditor(checkBox);
		TableCBCellRenderer tableR = new TableCBCellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(tableR);
		table.getColumnModel().getColumn(0).setCellEditor(checkboxRend);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setAutoCreateColumnsFromModel(false);

		for (int i = 0; i < cols.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(cols[i]);
		}

		tableScrlPane.setViewportView(table);
	

		this.add(notrhPanel, BorderLayout.NORTH);
		this.add(tableScrlPane, BorderLayout.CENTER);
		btnSearch.addActionListener(this);
	}

	private MbizTableModel model;
	private JTable table;
	private JScrollPane tableScrlPane;
	private JPanel notrhPanel;
	private JTextField txtSearch;
	private JButton btnSearch;

}
