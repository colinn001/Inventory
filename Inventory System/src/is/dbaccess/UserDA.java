package is.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import is.domain.User;

public class UserDA 
{
	private List<User> userList;
	private Connection userConn;
	private Statement initializeQuery;
	private int navNum;
	//private User currentUser;
	
	public UserDA(Connection paramConn) 
	{
		userConn = paramConn;
		navNum = 0;
		initializeList();
	}
	
	public void initializeList() 
	{
		try 
		{
			userList = new ArrayList<User>();
			initializeQuery = userConn.createStatement();
			ResultSet rs = initializeQuery.executeQuery("SELECT Id, Name, Pass, EmailAddress FROM tblUsers");
			
			while(rs.next()) 
			{
				User userAccount = new User();
				userAccount.setId(rs.getInt(1));
				userAccount.setFullName(rs.getString(2));
				userAccount.setPassword(rs.getString(3));
				userAccount.setEmail(rs.getString(4));
				userList.add(userAccount);
			}
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean userValidate(String username, String password) 
	{
		boolean valid = false;
		for(User u: userList)
		{
			if ((u.getFullName().equals(username)) && (u.getPassword().equals(password))) 
			{
				valid = true;
				break;
			}
		}
		return valid;
	}
	
	public void addUser(User user)
	{
		try 
		{
			//Calendar calendar = Calendar.getInstance();
		    //java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

			String query = "INSERT INTO tblUsers(Name, Pass, EmailAddress, DateCreated, DateModified)" +
					   " VALUES(?, ?, ?, GETDATE(), GETDATE())";
			PreparedStatement ps = userConn.prepareStatement(query);
			ps.setString(1, user.getFullName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "User Added Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
			//currentUser = userList.get(userList.size()-1);
		}
		catch(SQLException sqlE) 
		{ 
			sqlE.printStackTrace();
		}
	}
	
	public void updateUser(User userProfile, String name, String pass, String email) 
	{
		try 
		{
			String query = "UPDATE tblUsers SET Name = ?, Pass = ?, EmailAddress = ?, DateModified = GETDATE() WHERE Id = ?";
			PreparedStatement ps = userConn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, pass);
			ps.setString(3, email);
			ps.setInt(4, userProfile.getId());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "User Edited Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public void deleteUser(User userProfile) 
	{
		try 
		{
			String query = "DELETE FROM tblUsers WHERE Id = ?";
			PreparedStatement ps = userConn.prepareStatement(query);
			ps.setInt(1, userProfile.getId());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "User Deleted Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public DefaultTableModel viewAllUsers() 
	{
		//Set Model Not Editable
		DefaultTableModel dm  = new DefaultTableModel() 
		{
			 @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		try {
			Statement s = userConn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Id, Name, EmailAddress AS Email, DateCreated AS [Date Created], DateModified AS [Date Modified] FROM tblUsers");
			ResultSetMetaData rsmd = rs.getMetaData();
		
			Object[] columns = new Object[5];
			
			for(int inc = 1; inc <= rsmd.getColumnCount(); inc++) 
				columns[inc-1] = rsmd.getColumnLabel(inc);
			
			dm.setColumnIdentifiers(columns);
			
			while(rs.next()) 
			{
				Object[] row = new Object[5];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getDate(4);
				row[4] = rs.getDate(5);
				dm.addRow(row);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block	
			e1.printStackTrace();
		}
		return dm; 
		
	}
	
	public User openSelectedUser(String userId) 
	{
		int id = Integer.parseInt(userId);
		for(User u: userList) 
		{
			if (u.getId() == id)
			{
				return u;
			}
			navNum++;
		}
		return null;
	}
	
	public User moveToFirstUser() 
	{
		navNum = 0;
		return userList.get(navNum); 
	}
	public User moveToPrevUser() 
	{
		navNum--;
		if(navNum < 0)
			navNum = 0;
		return userList.get(navNum); 
	}
	public User moveToNextUser() 
	{	
		navNum++;
		if(navNum == userList.size())
			navNum = userList.size()-1;
		return userList.get(navNum); 
	}
	public User moveToLastUser()
	{ 
		navNum = userList.size()-1;
		return userList.get(navNum); 
	}
	
}
