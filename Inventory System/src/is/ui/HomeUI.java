package is.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import is.dbaccess.InventoryAdjustmentDA;
import is.dbaccess.ItemDA;
import is.dbaccess.UserDA;

public class HomeUI extends JFrame implements ActionListener 
{
	private JLabel backgroundL, titleL, subTitleL;
	private JButton newB, openB, logoutB;
	private JPanel upperPanel;
	private JTree tree;
	private JTable recordsTable;
	private DefaultTableModel dm;
	private JScrollPane scrollPane;
	private DefaultMutableTreeNode root, masterfileNode, inventoryNode, userNode, itemNode, inventoryAdjustmentNode;
	private Connection conn;
	 
	public HomeUI(Connection dbConn)
	{
		setSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		this.getContentPane().setBackground(Color.WHITE);
		conn = dbConn;
		
		initialize();
		newB.addActionListener(this);
		openB.addActionListener(this);
		logoutB.addActionListener(this);
		
		setVisible(true);	
	}
	
	public void initialize() 
	{
        root = new DefaultMutableTreeNode("Root");
        masterfileNode = new DefaultMutableTreeNode("Masterfiles");
        userNode = new DefaultMutableTreeNode("Users");
        itemNode = new DefaultMutableTreeNode("Items");
        inventoryNode = new DefaultMutableTreeNode("Inventory");
        inventoryAdjustmentNode = new DefaultMutableTreeNode("Inventory Adjustments");
        
        root.add(masterfileNode);
        root.add(inventoryNode);
        
        masterfileNode.add(userNode);
        masterfileNode.add(itemNode);
        inventoryNode.add(inventoryAdjustmentNode);
        
        tree = new JTree(root);
        if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer)
        {
            final DefaultTreeCellRenderer renderer = 
                (DefaultTreeCellRenderer)(tree.getCellRenderer());
            renderer.setBackgroundNonSelectionColor(Color.decode("#262626"));
            renderer.setTextNonSelectionColor(Color.WHITE);
        }
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
            
        expandTree(tree, true);
        tree.getSelectionModel().addTreeSelectionListener(new TreeActionEvent());
       
		upperPanel = new JPanel();
		backgroundL = new JLabel(new ImageIcon("C://Users/User/eclipse-workspace/Inventory System/Images/BackGroundHome.jpg"));
		titleL = new JLabel("Sistema De Inventario");
		subTitleL = new JLabel("Home");
		
		logoutB = new JButton("Logout");
		newB = new JButton("New");
		openB = new JButton("Open");
		
		recordsTable = new JTable(dm);
		scrollPane = new JScrollPane(recordsTable);
		recordsTable.setFocusable(false);
		recordsTable.setRowSelectionAllowed(true);
		
		upperPanel.setLayout(null);
		
		upperPanel.setBackground(Color.decode("#262626"));
		titleL.setForeground(Color.WHITE);
		subTitleL.setForeground(Color.WHITE);
		logoutB.setForeground(Color.WHITE);
		logoutB.setBackground(Color.decode("#ff8000"));
		tree.setBackground(Color.decode("#262626"));
		
		newB.setForeground(Color.WHITE);
		newB.setBackground(Color.decode("#262626"));
		openB.setForeground(Color.WHITE);
		openB.setBackground(Color.decode("#262626"));
		
		logoutB.setFocusable(false);
		newB.setFocusable(false);
		openB.setFocusable(false);
		
		logoutB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		newB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		openB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		titleL.setFont(new Font("Arial", Font.BOLD, 20));
		subTitleL.setFont(new Font("Arial", Font.ITALIC, 15));
		
		this.add(backgroundL);
		this.add(upperPanel);
		upperPanel.add(titleL);
		upperPanel.add(subTitleL);
		upperPanel.add(logoutB);
		backgroundL.add(tree);
		backgroundL.add(newB);
		backgroundL.add(openB);
		backgroundL.add(scrollPane);
		
		upperPanel.setBounds(0, 0, 1000, 65);
		backgroundL.setBounds(0, 70, 1000, 500);
		titleL.setBounds(20, 0, 300, 60);
		subTitleL.setBounds(500, 30, 100, 40);
		logoutB.setBounds(900, 40, 80, 20);
		tree.setBounds(10, 10, 280, 500);
		newB.setBounds(330, 10, 80, 20);
		openB.setBounds(430, 10, 80, 20);
		scrollPane.setBounds(330, 40, 630, 420);
		
		// Select User in Tree by default
		tree.setSelectionRow(1);
	}
	
	private void expandTree(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath path, boolean expand) {
        TreeNode node = (TreeNode) path.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreeNode n = (TreeNode) enumeration.nextElement();
                TreePath p = path.pathByAddingChild(n);

                expandAll(tree, p, expand);
            }
        }

        if (expand) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }
    
    
    class TreeActionEvent implements TreeSelectionListener{
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			 DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
             
             if (selectedNode.getParent().toString() == "Masterfiles")
             {
             	if (selectedNode.getUserObject().toString() == "Users") 
             	{
             		UserDA ud = new UserDA(conn);
             		recordsTable.setModel(ud.viewAllUsers());  
             	}else if (selectedNode.getUserObject().toString() == "Items") 
             	{
             		ItemDA ud = new ItemDA(conn);
             		recordsTable.setModel(ud.viewAllItems());   
             	}
             	recordsTable.removeColumn(recordsTable.getColumnModel().getColumn(0)); // Hide Id Column
             }
             else if (selectedNode.getParent().toString() == "Inventory")
             {
            	if (selectedNode.getUserObject().toString() == "Inventory Adjustments") 
              	{
            		InventoryAdjustmentDA iaDA = new InventoryAdjustmentDA(conn);
              		recordsTable.setModel(iaDA.viewAllInventoryAdjustments());   				
              	}
            	recordsTable.removeColumn(recordsTable.getColumnModel().getColumn(0)); // Hide Id Column
             }
             //System.out.println(selectedNode.getParent().toString());
             //System.out.println(selectedNode.getUserObject().toString());
             //selectedLabel.setText(selectedNode.getUserObject().toString());
         	//System.out.println(e.getPath().toString());
         		
         }
	}
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
    	Object clickSource = e.getSource();
    	
    	if (clickSource.equals(newB)) // Click New
    	{
    		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        	
        	if (selectedNode.getParent().toString() == "Masterfiles")
            {
        		if (selectedNode.getUserObject().toString() == "Users") 
        		{
        			new UserUI(conn, true);
        			this.dispose();
        		}else if (selectedNode.getUserObject().toString() == "Items") 
        		{
        			new ItemUI(conn, true);
        			this.dispose();
        		} 
            }else if (selectedNode.getParent().toString() == "Inventory")
            {
            	if (selectedNode.getUserObject().toString() == "Inventory Adjustments") 
              	{
              		new InventoryAdjustmentUI(conn, true);
        			this.dispose();
              	}
            }
    	}else if (clickSource.equals(openB)) // Click Open
    	{
    		String value = recordsTable.getModel().getValueAt(recordsTable.getSelectedRow(), 0).toString();
    		//System.out.println(value);
    		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        	
        	if (selectedNode.getParent().toString() == "Masterfiles")
            {
        		if (selectedNode.getUserObject().toString() == "Users") 
        		{
        			UserUI openUI = new UserUI(conn, false);
        			openUI.openUser(value);
        			this.dispose();
        		}else if (selectedNode.getUserObject().toString() == "Items") 
        		{
        			ItemUI openUI = new ItemUI(conn, false);
        			openUI.openItem(value);
        			this.dispose();
        		}
            }else if (selectedNode.getParent().toString() == "Inventory")
            {
            	if (selectedNode.getUserObject().toString() == "Inventory Adjustments") 
        		{
        			InventoryAdjustmentUI openUI = new InventoryAdjustmentUI(conn, false);
        			openUI.openIA(value);
        			this.dispose();
        		}
            }
    		//dm.getDataVector().elementAt(recordsTable.getSelectedRow()
    	}else if (clickSource.equals(logoutB)) //Click Logout
    	{
    		try{conn.close();}catch(SQLException t) {t.printStackTrace();}
    		new LoginUI();
    		this.dispose();
    	}
    }

}
