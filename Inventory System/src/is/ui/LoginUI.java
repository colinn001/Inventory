package is.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import is.dbaccess.UserDA;

public class LoginUI extends JFrame 
{
	private JLabel backgroundL;
	private JLabel titleL, subTitle1L, subTitle2L, subTitle3L, usernameL, passwordL;
	private JLabel subContent1L, subContent2L, subContent3L, sponsorL;
	private JTextField usernameTF;
	private JPasswordField passwordTF;
	private JButton signInB, signUpB;
	private LoginCredentials actEvent;
	private UserDA userDA;
	
	
	public LoginUI() 
	{
		setSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		this.getContentPane().setBackground(Color.decode("#262626"));
		
		initialize();
		
		actEvent = new LoginCredentials(this);
		signInB.addActionListener(actEvent);
		
		setVisible(true);
	}
	
	public void initialize()
	{
		backgroundL = new JLabel(new ImageIcon("C://Users/User/eclipse-workspace/Inventory System/Images/BackGroundlogin.jpg"));
		
		titleL = new JLabel("Sistema De Inventario");
		subTitle1L = new JLabel("Sub Title 1");
		subTitle2L = new JLabel("Sub Title 2");
		subTitle3L = new JLabel("Sub Title 3");
		subContent1L = new JLabel("Please enter a message here");
		subContent2L = new JLabel("Please enter a message here");
		subContent3L = new JLabel("Please enter a message here");
		usernameL = new JLabel("Username:");
		passwordL = new JLabel("Password:");
		sponsorL = new JLabel("Powered by Java");

		usernameTF = new JTextField(200);
		passwordTF = new JPasswordField(200);
		signInB = new JButton("Sign In");
		signUpB = new JButton("Sign Up");
		
		titleL.setForeground(Color.WHITE);
		usernameL.setForeground(Color.WHITE);
		passwordL.setForeground(Color.WHITE);
		subTitle1L.setForeground(Color.WHITE);
		subTitle2L.setForeground(Color.WHITE);
		subTitle3L.setForeground(Color.WHITE);
		subContent1L.setForeground(Color.WHITE);
		subContent2L.setForeground(Color.WHITE);
		subContent3L.setForeground(Color.WHITE);
		sponsorL.setForeground(Color.WHITE);
		signInB.setForeground(Color.WHITE);
		signUpB.setForeground(Color.WHITE);
		
		signInB.setBackground(Color.decode("#ff8000"));
		signUpB.setBackground(Color.decode("#ff8000"));
		
		titleL.setFont(new Font("Arial", Font.BOLD, 40));
		subTitle1L.setFont(new Font("Arial", Font.BOLD, 20));
		subTitle2L.setFont(new Font("Arial", Font.BOLD, 20));
		subTitle3L.setFont(new Font("Arial", Font.BOLD, 20));
		sponsorL.setFont(new Font("Arial", Font.ITALIC, 10));
		
		signInB.setFocusable(false);
		signUpB.setToolTipText("No account yet?");
		signUpB.setFocusable(false);

		passwordTF.setEchoChar('*');
		
		// Adding Components
		this.add(backgroundL);
		this.getContentPane().add(titleL);
		this.getContentPane().add(usernameL);
		this.getContentPane().add(passwordL);
		this.getContentPane().add(usernameTF);
		this.getContentPane().add(passwordTF);
		this.getContentPane().add(signInB);
		this.getContentPane().add(signUpB);
		this.getContentPane().add(sponsorL);
		
		backgroundL.add(subTitle1L);
		backgroundL.add(subTitle2L);
		backgroundL.add(subTitle3L);
		backgroundL.add(subContent1L);
		backgroundL.add(subContent2L);
		backgroundL.add(subContent3L);
		
		// Setting Up Components Location
		backgroundL.setBounds(0, 70, 1000, 500);
		titleL.setBounds(20, 5, 500, 100);
		usernameL.setBounds(750, 20, 80, 40);
		passwordL.setBounds(750, 50, 80, 40);
		usernameTF.setBounds(820, 30, 150, 20);
		passwordTF.setBounds(820, 60, 150, 20);	
		signInB.setBounds(840, 90, 100, 20);
		signUpB.setBounds(700, 90, 100, 20);
		
		subTitle1L.setBounds(70, 120, 200, 40);
		subTitle2L.setBounds(420, 120, 200, 40);
		subTitle3L.setBounds(760, 120, 200, 40);
		
		subContent1L.setBounds(70, 150, 200, 40);
		subContent2L.setBounds(420, 150, 200, 40);
		subContent3L.setBounds(760, 150, 200, 40);
		
		sponsorL.setBounds(890, 540, 200, 40);
	}
	
	class LoginCredentials implements ActionListener
	{
		JFrame frame;
		public LoginCredentials(JFrame loginUI) 
		{
			frame = loginUI;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			String pwd = new String(passwordTF.getPassword());
			String username = "sa", password = "masterkey";
			String url = "jdbc:sqlserver://CALAGUASFAMILY:2014;databaseName=InventoryDBMS";
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection conn = DriverManager.getConnection(url, username, password);
				userDA = new UserDA(conn);
			
				if(userDA.userValidate(usernameTF.getText(), pwd)) 
				{
					JOptionPane.showMessageDialog(null, "Login Successful", "", JOptionPane.INFORMATION_MESSAGE);
					new HomeUI(conn);
					frame.dispose();
				}else
					JOptionPane.showMessageDialog(null, "Invalid Account", "", JOptionPane.ERROR_MESSAGE);
				
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			}
			
		}
	}
	
    public static void main (String args[]) 
    { 
    	new LoginUI();
    } 
}
