package is.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import is.dbaccess.ItemDA;
import is.dbaccess.UnitOfMeasureDA;
import is.domain.Item;
import is.domain.UnitOfMeasure;

public class ItemUI extends JFrame implements ActionListener
{
	private JPanel upperPanel, bodyPanel, navPanel;
	private JButton closeB, nextB, prevB, firstB, lastB, saveB, deleteB;
	private JTextField codeTF, nameTF, priceTF;
	private JComboBox uomCB;
	private JLabel titleL, codeL, nameL, uomL, priceL;
	private Connection conn;
	private Item navItem;
	private ItemDA itemDA;
	private UnitOfMeasure currentUOM;
	private UnitOfMeasureDA uomDA;
	private boolean isNew;
	
	public ItemUI(Connection dbConn, boolean isNew) 
	{
		setSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		this.getContentPane().setBackground(Color.WHITE);
		conn = dbConn;
		this.isNew = isNew;
		itemDA = new ItemDA(conn);
		uomDA = new UnitOfMeasureDA(conn);
		
		initialize();
		
		saveB.addActionListener(this);
		nextB.addActionListener(this);
		prevB.addActionListener(this);
		firstB.addActionListener(this);
		lastB.addActionListener(this);
		deleteB.addActionListener(this);
		closeB.addActionListener(this);
		uomCB.addActionListener(this);
		
		uomCB.setSelectedIndex(0);
		
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
		
		titleL = new JLabel("Item Form");
		
		closeB = new JButton("X");
		nextB = new JButton(">");
		prevB = new JButton("<");
		firstB = new JButton("<<");
		lastB = new JButton(">>");
		saveB = new JButton("Update and Save");
		deleteB = new JButton("Delete");
		
		codeL = new JLabel("Code:");
		nameL = new JLabel("Name:");
		uomL = new JLabel("Unit:");
		priceL = new JLabel("Price:");

		codeTF = new JTextField(300);
		nameTF = new JTextField(500);
		priceTF = new JTextField(500);
		
		uomCB = new JComboBox(uomDA.getListOfUnits());
		
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
		
		codeL.setForeground(Color.WHITE);
		codeL.setFont(new Font("Arial", Font.BOLD, 15));
		
		nameL.setForeground(Color.WHITE);
		nameL.setFont(new Font("Arial", Font.BOLD, 15));
		
		uomL.setForeground(Color.WHITE);
		uomL.setFont(new Font("Arial", Font.BOLD, 15));
		
		priceL.setForeground(Color.WHITE);
		priceL.setFont(new Font("Arial", Font.BOLD, 15));
				
		upperPanel.setBackground(Color.decode("#262626"));
		bodyPanel.setBackground(Color.decode("#ff8000"));
		navPanel.setBackground(Color.decode("#262626"));
		
		this.add(upperPanel);
		this.add(bodyPanel);
		upperPanel.add(titleL);
		upperPanel.add(closeB);
		bodyPanel.add(navPanel);
		bodyPanel.add(codeL);
		bodyPanel.add(nameL);
		bodyPanel.add(uomL);
		bodyPanel.add(priceL);
		
		bodyPanel.add(codeTF);
		bodyPanel.add(nameTF);
		bodyPanel.add(uomCB);
		bodyPanel.add(priceTF);
		
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
		
		codeL.setBounds(40, 30, 120, 30);
		nameL.setBounds(40, 80, 120, 30);
		uomL.setBounds(40, 130, 120, 30);
		priceL.setBounds(40, 180, 120, 30);
		
		codeTF.setBounds(200, 30, 200, 25);
		nameTF.setBounds(200, 80, 200, 25);
		uomCB.setBounds(200, 130, 200, 25);
		priceTF.setBounds(200, 180, 200, 25);
		
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
		
		if(!isHide)
			saveB.setText("Save");
		else
			saveB.setText("Update and Save");
	}
	
	public void setDataAndTextfields() 
	{
		codeTF.setText(navItem.getCode());
		nameTF.setText(navItem.getName());
		uomCB.setSelectedItem(currentUOM.getCode());
		priceTF.setText(String.valueOf(navItem.getPrice()));
	}

	@Override
	public void actionPerformed(ActionEvent act)
	{
		Object evt = act.getSource();
		
		if (evt.equals(closeB))
		{
			new HomeUI(conn);
			this.dispose();
		}else if (evt.equals(saveB)) 
		{
			if(isNew) 
			{
				Item addedItem = new Item();			

				addedItem.setCode(codeTF.getText());
				addedItem.setName(nameTF.getText());
				addedItem.setUomId(currentUOM.getId());
				addedItem.setPrice(Double.parseDouble(priceTF.getText()));
				itemDA.addItem(addedItem);
				isNew = false;
				navPanelButtonsVisible(true);
				navItem = itemDA.moveToLastItem();
				currentUOM = uomDA.getIdValue(navItem.getUomId());
			}else {
				itemDA.updateItem(navItem, codeTF.getText(), nameTF.getText(), currentUOM.getId(), Long.parseLong(priceTF.getText()) );
			}
		}else if (evt.equals(deleteB))
		{
			itemDA.deleteItem(navItem);
			navItem = itemDA.moveToPrevItem();
			currentUOM = uomDA.getIdValue(navItem.getUomId());
			setDataAndTextfields();
		}else if (evt.equals(firstB)) 
		{
			navItem = itemDA.moveToFirstItem();
			currentUOM = uomDA.getIdValue(navItem.getUomId());
			setDataAndTextfields();	
		}else if (evt.equals(prevB)) 
		{
			navItem = itemDA.moveToPrevItem();
			currentUOM = uomDA.getIdValue(navItem.getUomId());
			setDataAndTextfields();
		}else if (evt.equals(nextB)) 
		{
			navItem = itemDA.moveToNextItem();
			currentUOM = uomDA.getIdValue(navItem.getUomId());
			setDataAndTextfields();
		}else if (evt.equals(lastB)) 
		{
			navItem = itemDA.moveToLastItem();
			currentUOM = uomDA.getIdValue(navItem.getUomId());
			setDataAndTextfields();
		}else if (evt.equals(uomCB)) 
		{
			currentUOM = uomDA.getUOMValue(uomCB.getSelectedItem().toString());
		}
	}
	
	public void openItem(String itemId) 
	{
		navItem = itemDA.openSelectedItem(itemId);
		currentUOM = uomDA.getIdValue(navItem.getUomId());
		setDataAndTextfields();
	}

}
