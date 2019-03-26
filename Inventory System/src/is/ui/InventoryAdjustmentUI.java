package is.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import is.dbaccess.InventoryAdjustmentDA;
import is.dbaccess.ItemDA;
import is.dbaccess.UnitOfMeasureDA;
import is.domain.InventoryAdjustment;
import is.domain.InventoryAdjustmentDetails;

public class InventoryAdjustmentUI extends JFrame implements ActionListener//, PropertyChangeListener
{
	private JPanel upperPanel, bodyPanel, navPanel;
	private JButton closeB, nextB, prevB, firstB, lastB, saveB, deleteB;
	private JTextField transactionNoTF, transactionDateTF, totalCostTF;
	private JLabel titleL, transactionNoL, transactionDateL, totalCostL;
	private JTable detailTable;
	private JComboBox itemCB;
	private JScrollPane detailScrollPane;
	private Connection conn;
	private boolean isNew;
	private InventoryAdjustment navInvAdj;
	private InventoryAdjustmentDA inventoryAdjustmentDA;
	private ItemDA itemDA;
	private UnitOfMeasureDA uomDA;
	
	public InventoryAdjustmentUI(Connection dbConn, boolean isNew) 
	{
		setSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		this.getContentPane().setBackground(Color.WHITE);
		conn = dbConn;
		this.isNew = isNew;
		
		initialize();
		inventoryAdjustmentDA = new InventoryAdjustmentDA(conn);
		itemDA = new ItemDA(conn);
		uomDA = new UnitOfMeasureDA(conn);
		setupDetailTable();
		
		saveB.addActionListener(this);
		nextB.addActionListener(this);
		prevB.addActionListener(this);
		firstB.addActionListener(this);
		lastB.addActionListener(this);
		deleteB.addActionListener(this);
		closeB.addActionListener(this);
		
		if (isNew) 
			navPanelButtonsVisible(false);
		else
			navPanelButtonsVisible(true);
		
		setVisible(true);	
	}
	
	public void initialize() 
	{
		upperPanel = new JPanel();
		bodyPanel = new JPanel();
		navPanel = new JPanel();
		
		titleL = new JLabel("Inventory Adjustment Form");
		
		closeB = new JButton("X");
		nextB = new JButton(">");
		prevB = new JButton("<");
		firstB = new JButton("<<");
		lastB = new JButton(">>");
		saveB = new JButton("Update and Save");
		deleteB = new JButton("Delete");
		
		transactionNoL = new JLabel("Transaction No.:");
		transactionDateL = new JLabel("Transaction Date:");
		totalCostL = new JLabel("Total Cost:");

		transactionNoTF = new JTextField(300);
		transactionDateTF = new JTextField(300);
		totalCostTF = new JTextField(300);
		
		detailTable = new JTable();
		detailScrollPane = new JScrollPane(detailTable);
		
		upperPanel.setLayout(null);
		bodyPanel.setLayout(null);
		navPanel.setLayout(null);
		
		titleL.setForeground(Color.WHITE);
		titleL.setFont(new Font("Arial", Font.BOLD, 30));
		
		closeB.setBackground(Color.decode("#cc0000"));
		closeB.setForeground(Color.WHITE);
		closeB.setFocusable(false);
		closeB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		prevB.setBackground(Color.decode("#262626"));
		prevB.setForeground(Color.WHITE);
		prevB.setFont(new Font("Arial", Font.BOLD, 20));
		prevB.setFocusable(false);
		prevB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		firstB.setBackground(Color.decode("#262626"));
		firstB.setForeground(Color.WHITE);
		firstB.setFont(new Font("Arial", Font.BOLD, 20));
		firstB.setFocusable(false);
		firstB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		nextB.setBackground(Color.decode("#262626"));
		nextB.setForeground(Color.WHITE);
		nextB.setFont(new Font("Arial", Font.BOLD, 20));
		nextB.setFocusable(false);
		nextB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		lastB.setBackground(Color.decode("#262626"));
		lastB.setForeground(Color.WHITE);
		lastB.setFont(new Font("Arial", Font.BOLD, 20));
		lastB.setFocusable(false);
		lastB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		saveB.setBackground(Color.decode("#262626"));
		saveB.setForeground(Color.WHITE);
		saveB.setFont(new Font("Arial", Font.BOLD, 20));
		saveB.setFocusable(false);
		saveB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		deleteB.setBackground(Color.decode("#262626"));
		deleteB.setForeground(Color.WHITE);
		deleteB.setFont(new Font("Arial", Font.BOLD, 20));
		deleteB.setFocusable(false);
		deleteB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		transactionNoL.setForeground(Color.WHITE);
		transactionNoL.setFont(new Font("Arial", Font.BOLD, 15));
		
		transactionDateL.setForeground(Color.WHITE);
		transactionDateL.setFont(new Font("Arial", Font.BOLD, 15));
		
		totalCostL.setForeground(Color.WHITE);
		totalCostL.setFont(new Font("Arial", Font.BOLD, 15));
		totalCostTF.setEditable(false);
		
		upperPanel.setBackground(Color.decode("#262626"));
		bodyPanel.setBackground(Color.decode("#ff8000"));
		navPanel.setBackground(Color.decode("#262626"));
		
		this.add(upperPanel);
		this.add(bodyPanel);
		upperPanel.add(titleL);
		upperPanel.add(closeB);
		bodyPanel.add(navPanel);
		bodyPanel.add(transactionNoL);
		bodyPanel.add(transactionDateL);
		bodyPanel.add(totalCostL);
		bodyPanel.add(transactionNoTF);
		bodyPanel.add(transactionDateTF);
		bodyPanel.add(totalCostTF);
		bodyPanel.add(detailScrollPane);
		navPanel.add(firstB);
		navPanel.add(prevB);
		navPanel.add(nextB);
		navPanel.add(lastB);
		navPanel.add(saveB);
		navPanel.add(deleteB);
		
		upperPanel.setBounds(0, 0, 1000, 65);
		bodyPanel.setBounds(0, 70, 1000, 500);
		navPanel.setBounds(10, 400, 975, 60);
		titleL.setBounds(20, 5, 500, 50);
		closeB.setBounds(920, 20, 50, 20);
		
		transactionNoL.setBounds(40, 30, 120, 30);
		transactionDateL.setBounds(500, 30, 140,30);
		totalCostL.setBounds(40, 80, 120, 30);
		transactionNoTF.setBounds(200, 30, 200, 25);
		transactionDateTF.setBounds(660, 30, 200, 25);
		totalCostTF.setBounds(200, 80, 150, 25);
		
		detailScrollPane.setBounds(40, 130, 910, 240);
		
		firstB.setBounds(50, 15, 60, 30);
		prevB.setBounds(150, 15, 60, 30);
		nextB.setBounds(250, 15, 60, 30);
		lastB.setBounds(350, 15, 60, 30);
		saveB.setBounds(500, 15, 200, 30);
		deleteB.setBounds(770, 15, 150, 30);
	}
	
	public void navPanelButtonsVisible(boolean isHide) 
	{
		firstB.setVisible(isHide);
		prevB.setVisible(isHide);
		nextB.setVisible(isHide);
		lastB.setVisible(isHide);
		deleteB.setVisible(isHide);
		
		if(!isHide) {
			transactionNoTF.setText(inventoryAdjustmentDA.getTransactionNoSeries());
			saveB.setText("Save");
		}
		else 
			saveB.setText("Update and Save");
		
		transactionNoTF.setEditable(false);
	}
	
	public void setDataAndTextfields() 
	{
		transactionNoTF.setText(navInvAdj.getTransactionNo());
		totalCostTF.setText(String.valueOf(navInvAdj.getTotalCost()));
		transactionDateTF.setText(navInvAdj.getTransactionDate().toString());
		setupDetailTable();
	}

	@Override
	public void actionPerformed(ActionEvent act)
	{
		Object evt = act.getSource();
		if (evt.equals(closeB))
		{
			new HomeUI(conn);
			this.dispose();
		}else if (evt.equals(itemCB)) 
		{
			if(itemCB.getSelectedItem() != null) 
			{
				int itemunit[] = itemDA.getItemAndUnit(itemCB.getSelectedItem().toString());
				String uomName = uomDA.getIdValue(itemunit[1]).getCode();
				double itemPrice = itemDA.openSelectedItem(String.valueOf(itemunit[0])).getPrice();
				
				detailTable.setValueAt(uomName, detailTable.getSelectedRow(), 1);
				detailTable.setValueAt(itemPrice, detailTable.getSelectedRow(), 3);
			}
		}else if (evt.equals(saveB)) 
		{
			Date dateFormat = null;
			try {
				//dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(transactionDateTF.getText());
				dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(transactionDateTF.getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(isNew) 
			{
				InventoryAdjustment addedIA = new InventoryAdjustment();							
				addedIA.setTransactionNo(transactionNoTF.getText());
				addedIA.setTransactionDate(dateFormat);
				addedIA.setTotalCost(Double.parseDouble(totalCostTF.getText()));
				addedIA.setInvadjdetailList(getDetailFromTable());
				inventoryAdjustmentDA.addInventoryAdjustment(addedIA);
				
				isNew = false;
				navPanelButtonsVisible(true);
				navInvAdj = inventoryAdjustmentDA.moveToLastIA();
				
			}else { 
				navInvAdj.setTransactionNo(transactionNoTF.getText());
				navInvAdj.setTransactionDate(dateFormat);
				navInvAdj.setTotalCost(Double.parseDouble(totalCostTF.getText()));
				updateDetailTableBasedOnExistingAndDeletedRecords();
				
				/*for(InventoryAdjustmentDetails iad: navInvAdj.getInadjdetailList()) 
				{
					System.out.print(iad.getId() + " ");
					System.out.print(iad.getInventoryadjustmentid() + " ");
					System.out.print(iad.getItemid() + " ");
					System.out.print(iad.getQuantity() + " ");
					System.out.print(iad.getUnitprice() + " ");
					System.out.print(iad.getTotalCost() + " ");
				}*/
				
				inventoryAdjustmentDA.updateInventoryAdjustment(navInvAdj);
			}
		}else if (evt.equals(deleteB))
		{ 
			inventoryAdjustmentDA.deleteInventoryAdjustments(navInvAdj);
			navInvAdj = inventoryAdjustmentDA.moveToPrevIA();
			setDataAndTextfields();
		}else if (evt.equals(firstB)) 
		{
			navInvAdj = inventoryAdjustmentDA.moveToFirstIA();
			setDataAndTextfields();	
		}else if (evt.equals(prevB)) 
		{
			navInvAdj = inventoryAdjustmentDA.moveToPrevIA();
			setDataAndTextfields();
		}else if (evt.equals(nextB)) 
		{
			navInvAdj = inventoryAdjustmentDA.moveToNextIA();
			setDataAndTextfields();
		}else if (evt.equals(lastB)) 
		{
			navInvAdj = inventoryAdjustmentDA.moveToLastIA();
			setDataAndTextfields();
		}
	}
	
	public void openIA(String iaId) 
	{
		navInvAdj = inventoryAdjustmentDA.openSelectedIA(iaId);
		setDataAndTextfields();
	}
	
	public void setupDetailTable() 
	{
		if (isNew)
			navInvAdj = null;
		DefaultTableModel dm = inventoryAdjustmentDA.viewDetailTable(isNew, navInvAdj);
		detailTable.setModel(dm);
		setupItemColumn(detailTable, detailTable.getColumnModel().getColumn(2));
		//Remove Inventory Adjustment ID and ID
		detailTable.removeColumn(detailTable.getColumnModel().getColumn(0));
		detailTable.removeColumn(detailTable.getColumnModel().getColumn(0));
		
		detailTable.addKeyListener(new KeyListener() {
			
            public void keyPressed(KeyEvent e) {
            	DefaultTableModel model = (DefaultTableModel)detailTable.getModel();
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    model.addRow(new Object[7]);                
                else if(e.getKeyCode() == KeyEvent.VK_DELETE) 
                {
                	if(detailTable.getSelectedRow() > 0) 
                	{
                		if (detailTable.isEditing())
                		{
                			detailTable.getCellEditor().cancelCellEditing();
                		}
                		model.removeRow(detailTable.getSelectedRow());    
                	}
                	     
                }
                	 
            }
            public void keyReleased(KeyEvent e) { }

            public void keyTyped(KeyEvent e) { } } 
		);
		
		detailTable.getModel().addTableModelListener(new TableModelListener() 
		{
			public void tableChanged(TableModelEvent e) {
				int row = detailTable.getSelectedRow();
		       
				 if(e.getColumn() == 3 || e.getColumn() == 4) 
				 {
					 double total = 0.00;
					 
					 if (detailTable.getValueAt(row, 2) != null && detailTable.getValueAt(row, 3) != null) 
					 {
						 total = Double.parseDouble(detailTable.getValueAt(row, 2).toString()) * Double.parseDouble(detailTable.getValueAt(row, 3).toString());
						 
					 }
					 detailTable.setValueAt(total, row, 4);
				 }
				 
				 if(e.getColumn() == 6 && Double.parseDouble(detailTable.getValueAt(row, 4).toString()) > 0) 
				 {
					 double overall = 0.00;
					 for (int inc = 0; inc < detailTable.getRowCount(); inc++) 
					 {
						 overall += Double.parseDouble(detailTable.getValueAt(inc, 4).toString());
					 }
					 totalCostTF.setText(String.valueOf(overall));	 
				 }
		      }
		});	 
		
		itemCB.addActionListener(this);
		revalidate();
	}
	
	public void setupItemColumn(JTable tbl, TableColumn itemColumn)
	{
		//Set up the editor for the sport cells.
        itemCB = new JComboBox(itemDA.getListOfItems());
        itemColumn.setCellEditor(new DefaultCellEditor(itemCB));
	}
	
	public List<InventoryAdjustmentDetails> getDetailFromTable()
	{
		List<InventoryAdjustmentDetails> detail = new ArrayList<InventoryAdjustmentDetails>();
		
		for(int row = 0; row < detailTable.getRowCount(); row++) 
		{
			InventoryAdjustmentDetails detailRow = new InventoryAdjustmentDetails();
			int itemandunit[] = itemDA.getItemAndUnit(String.valueOf(detailTable.getModel().getValueAt(row, 2)));
			
			if(detailTable.getModel().getValueAt(row, 0) != null && detailTable.getModel().getValueAt(row, 1) != null) {
				detailRow.setId(Integer.parseInt(String.valueOf(detailTable.getModel().getValueAt(row, 0))));//Id
				detailRow.setInventoryadjustmentid(Integer.parseInt(String.valueOf(detailTable.getModel().getValueAt(row, 1))));//InventoryAdjustmentId
			}
			detailRow.setItemid(itemandunit[0]);//ItemId
			detailRow.setUomid(itemandunit[1]);//UOMId
			detailRow.setQuantity(Double.parseDouble(String.valueOf(detailTable.getModel().getValueAt(row, 4))));//Quantity
			detailRow.setUnitprice(Double.parseDouble(String.valueOf(detailTable.getModel().getValueAt(row, 5))));//UnitPrice
			detailRow.setTotalCost(Double.parseDouble(String.valueOf(detailTable.getModel().getValueAt(row, 6))));//TotalCost
			detail.add(detailRow);
		}
		
		return detail;
	}
	
	public void updateDetailTableBasedOnExistingAndDeletedRecords() 
	{
		List<InventoryAdjustmentDetails> updateInvAdj = new ArrayList<InventoryAdjustmentDetails>();
		List<InventoryAdjustmentDetails> deleteInvAdj = new ArrayList<InventoryAdjustmentDetails>();
		
		for(InventoryAdjustmentDetails iad: navInvAdj.getInadjdetailList()) 
		{
			int inc = 1;
			for(InventoryAdjustmentDetails iad2: getDetailFromTable()) 
			{
				if(iad.getId() == iad2.getId()) 
				{
					updateInvAdj.add(iad2);
					break;
				}
						
				if(inc == getDetailFromTable().size()) 
					deleteInvAdj.add(iad);
				
				inc++;
			}
		}
		
		for(InventoryAdjustmentDetails iad3: getDetailFromTable()) 
		{
			if(iad3.getId() == 0) 
				updateInvAdj.add(iad3);		
		}
		
		inventoryAdjustmentDA.deleteInventoryAdjustmentDetails(deleteInvAdj);
		navInvAdj.setInvadjdetailList(updateInvAdj);
	}

}
