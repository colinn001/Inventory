package is.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import is.dbaccess.UserDA;
import is.domain.User;

public class UserUI extends JFrame implements ActionListener
{
	private JPanel upperPanel, bodyPanel, navPanel;
	private JButton closeB, nextB, prevB, firstB, lastB, saveB, deleteB;
	private JTextField nameTF, emailTF;
	private JPasswordField passPF;
	private JLabel titleL, nameL, passL, emailL;
	private Connection conn;
	private boolean isNew;
	private User navUser;
	private UserDA userDA;
	
	public UserUI(Connection dbConn, boolean isNew) 
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
		
		saveB.addActionListener(this);
		nextB.addActionListener(this);
		prevB.addActionListener(this);
		firstB.addActionListener(this);
		lastB.addActionListener(this);
		deleteB.addActionListener(this);
		closeB.addActionListener(this);
		
		userDA = new UserDA(conn);
		
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
		
		titleL = new JLabel("User Form");
		
		closeB = new JButton("X");
		nextB = new JButton(">");
		prevB = new JButton("<");
		firstB = new JButton("<<");
		lastB = new JButton(">>");
		saveB = new JButton("Update and Save");
		deleteB = new JButton("Delete");
		
		nameL = new JLabel("Full Name:");
		passL = new JLabel("Password:");
		emailL = new JLabel("Email Address:");

		nameTF = new JTextField(300);
		emailTF = new JTextField(500);
		passPF = new JPasswordField(300);
		
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
		//prevB.setBackground(Color.decode("#ff8000"));
		prevB.setForeground(Color.WHITE);
		prevB.setFont(new Font("Arial", Font.BOLD, 20));
		prevB.setFocusable(false);
		prevB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		firstB.setBackground(Color.decode("#262626"));
		//firstB.setBackground(Color.decode("#ff8000"));
		firstB.setForeground(Color.WHITE);
		firstB.setFont(new Font("Arial", Font.BOLD, 20));
		firstB.setFocusable(false);
		firstB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		nextB.setBackground(Color.decode("#262626"));
		//nextB.setBackground(Color.decode("#ff8000"));
		nextB.setForeground(Color.WHITE);
		nextB.setFont(new Font("Arial", Font.BOLD, 20));
		nextB.setFocusable(false);
		nextB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		lastB.setBackground(Color.decode("#262626"));
		//lastB.setBackground(Color.decode("#ff8000"));
		lastB.setForeground(Color.WHITE);
		lastB.setFont(new Font("Arial", Font.BOLD, 20));
		lastB.setFocusable(false);
		lastB.setCursor(new Cursor(Cursor.HAND_CURSOR));

		saveB.setBackground(Color.decode("#262626"));
		//saveB.setBackground(Color.decode("#ff8000"));
		saveB.setForeground(Color.WHITE);
		saveB.setFont(new Font("Arial", Font.BOLD, 20));
		saveB.setFocusable(false);
		saveB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		deleteB.setBackground(Color.decode("#262626"));
		//deleteB.setBackground(Color.decode("#ff8000"));
		deleteB.setForeground(Color.WHITE);
		deleteB.setFont(new Font("Arial", Font.BOLD, 20));
		deleteB.setFocusable(false);
		deleteB.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		nameL.setForeground(Color.WHITE);
		nameL.setFont(new Font("Arial", Font.BOLD, 15));
		
		passL.setForeground(Color.WHITE);
		passL.setFont(new Font("Arial", Font.BOLD, 15));
		
		emailL.setForeground(Color.WHITE);
		emailL.setFont(new Font("Arial", Font.BOLD, 15));
		
		passPF.setEchoChar('*');
		
		upperPanel.setBackground(Color.decode("#262626"));
		bodyPanel.setBackground(Color.decode("#ff8000"));
		navPanel.setBackground(Color.decode("#262626"));
		
		this.add(upperPanel);
		this.add(bodyPanel);
		upperPanel.add(titleL);
		upperPanel.add(closeB);
		bodyPanel.add(navPanel);
		bodyPanel.add(nameL);
		bodyPanel.add(passL);
		bodyPanel.add(emailL);
		bodyPanel.add(nameTF);
		bodyPanel.add(emailTF);
		bodyPanel.add(passPF);
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
		
		nameL.setBounds(40, 30, 120, 30);
		passL.setBounds(40, 80, 120,30);
		emailL.setBounds(40, 130, 120, 30);
		nameTF.setBounds(200, 30, 200, 25);
		passPF.setBounds(200, 80, 200, 25);
		emailTF.setBounds(200, 130, 200, 25);
		
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
		nameTF.setText(navUser.getFullName());
		passPF.setText(navUser.getPassword());
		emailTF.setText(navUser.getEmail());
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
			String pass = new String(passPF.getPassword());
			if(isNew) 
			{
				if (pass != null)
				{
					User addedUser = new User();			

					addedUser.setFullName(nameTF.getText());
					addedUser.setEmail(emailTF.getText());
					addedUser.setPassword(pass);
					userDA.addUser(addedUser);
					isNew = false;
					navPanelButtonsVisible(true);
					navUser = userDA.moveToLastUser();
				}
			}else {
				userDA.updateUser(navUser, nameTF.getText(), pass, emailTF.getText());
			}
		}else if (evt.equals(deleteB))
		{
			userDA.deleteUser(navUser);
			navUser = userDA.moveToPrevUser();
			setDataAndTextfields();
		}else if (evt.equals(firstB)) 
		{
			navUser = userDA.moveToFirstUser();
			setDataAndTextfields();	
		}else if (evt.equals(prevB)) 
		{
			navUser = userDA.moveToPrevUser();
			setDataAndTextfields();
		}else if (evt.equals(nextB)) 
		{
			navUser = userDA.moveToNextUser();
			setDataAndTextfields();
		}else if (evt.equals(lastB)) 
		{
			navUser = userDA.moveToLastUser();
			setDataAndTextfields();
		}
	}
	
	public void openUser(String userId) 
	{
		navUser = userDA.openSelectedUser(userId);
		setDataAndTextfields();
	}

}
