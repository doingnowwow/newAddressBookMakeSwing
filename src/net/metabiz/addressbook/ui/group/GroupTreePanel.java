package net.metabiz.addressbook.ui.group;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DropMode;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class GroupTreePanel extends JScrollPane {

	public GroupTreePanel() {
		tree = new JTree();
		jbInit();
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
		jbInit();
	}
	
//	public JScrollPane getScrollPane() {
//		return this.treeScrlPane;
//	}

	private void jbInit() {

		

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setPreferredSize(new Dimension(100,30));
		renderer.setLeafIcon(renderer.getDefaultClosedIcon());
		tree.setCellRenderer(renderer);

		tree.setEditable(true);
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		tree.setEditable(false);
	
		this.setViewportView(tree);

	}

	private JTree tree;
	private DefaultMutableTreeNode root;
}
