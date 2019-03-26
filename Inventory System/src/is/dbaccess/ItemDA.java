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

import is.domain.Item;
import is.domain.User;

public class ItemDA 
{
	private List<Item> itemList;
	private Connection itemConn;
	private Statement initializeQuery;
	private int navNum;
	//private User currentUser;
	
	public ItemDA(Connection paramConn) 
	{
		itemConn = paramConn;
		navNum = 0;
		initializeList();
	}
	
	public void initializeList() 
	{
		try 
		{
			itemList = new ArrayList<Item>();
			initializeQuery = itemConn.createStatement();
			ResultSet rs = initializeQuery.executeQuery("SELECT Id, Code, Name, UOMId, Price FROM tblItems");
			
			while(rs.next()) 
			{
				Item itemAccount = new Item();
				itemAccount.setId(rs.getInt(1));
				itemAccount.setCode(rs.getString(2));
				itemAccount.setName(rs.getString(3));
				itemAccount.setUomId(rs.getInt(4));
				itemAccount.setPrice(rs.getDouble(5));
				itemList.add(itemAccount);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addItem(Item item)
	{
		try 
		{
			//Calendar calendar = Calendar.getInstance();
		    //java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

			String query = "INSERT INTO tblItems(Code, Name, UOMId, Price, DateCreated, DateModified)" +
					   " VALUES(?, ?, ?, ?, GETDATE(), GETDATE())";
			PreparedStatement ps = itemConn.prepareStatement(query);
			ps.setString(1, item.getCode());
			ps.setString(2, item.getName());
			ps.setInt(3, item.getUomId());
			ps.setDouble(4, item.getPrice());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Item Added Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}
		catch(SQLException sqlE) 
		{ 
			sqlE.printStackTrace();
		}
	}
	
	public void updateItem(Item item, String code, String name, int uomId, double price) 
	{
		try 
		{
			String query = "UPDATE tblItems SET Code = ?, Name = ?, UomId = ?, Price = ?, DateModified = GETDATE() WHERE Id = ?";
			PreparedStatement ps = itemConn.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, name);
			ps.setInt(3, uomId);
			ps.setDouble(4, price);
			ps.setInt(5, item.getId());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Item Updated Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public void deleteItem(Item item) 
	{
		try 
		{
			String query = "DELETE FROM tblItems WHERE Id = ?";
			PreparedStatement ps = itemConn.prepareStatement(query);
			ps.setInt(1, item.getId());
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Item Deleted Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public DefaultTableModel viewAllItems() 
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
			Statement s = itemConn.createStatement();
			ResultSet rs = s.executeQuery("SELECT i.Id, i.Code, i.Name, u.Name AS Unit, i.Price, i.DateCreated AS [Date Created], i.DateModified AS [Date Modified] FROM tblItems i LEFT OUTER JOIN tblUnitOfMeasures u ON u.Id = i.UOMId");
			ResultSetMetaData rsmd = rs.getMetaData();
		
			Object[] columns = new Object[7];
			
			for(int inc = 1; inc <= rsmd.getColumnCount(); inc++) 
				columns[inc-1] = rsmd.getColumnLabel(inc);
			
			dm.setColumnIdentifiers(columns);
			
			while(rs.next()) 
			{
				Object[] row = new Object[7];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getDouble(5);
				row[5] = rs.getDate(6);
				row[6] = rs.getDate(7);
				dm.addRow(row);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block	
			e1.printStackTrace();
		}
		return dm; 
		
	}
	
	public Item openSelectedItem(String itemId) 
	{
		int id = Integer.parseInt(itemId);
		for(Item u: itemList) 
		{
			if (u.getId() == id)
			{
				return u;
			}
			navNum++;
		}
		return null;
	}
	
	public int[] getItemAndUnit(String name)
	{
		int itemnunit[]= new int[2]; 
		for(Item u: itemList) 
		{
			if (u.getName().equals(name))
			{
				itemnunit[0] = u.getId();
				itemnunit[1] = u.getUomId();
				break;
			}
		}
		return itemnunit;
	}
	
	public Object[] getListOfItems() 
	{
		try 
		{
			int arrSize = 0, inc = 0;
			String dataCountQuery = "SELECT COUNT(Code) FROM tblItems";
			String query = "SELECT Name FROM tblItems";
			
			Statement s = itemConn.createStatement();
			ResultSet rs = s.executeQuery(dataCountQuery);
			rs.next();
			arrSize = rs.getInt(1);
			
			Object items[] = new Object[arrSize];
			
			rs = s.executeQuery(query);
			while(rs.next()) 
			{
				items[inc] = rs.getString(1);
				inc++;
			}
			
			return items;
			
		}catch(SQLException er){
			er.printStackTrace();
		}
		return null;
	}
	
	public Item moveToFirstItem() 
	{
		navNum = 0;
		return itemList.get(navNum); 
	}
	public Item moveToPrevItem() 
	{
		navNum--;
		if(navNum < 0)
			navNum = 0;
		return itemList.get(navNum); 
	}
	public Item moveToNextItem() 
	{	
		navNum++;
		if(navNum == itemList.size())
			navNum = itemList.size()-1;
		return itemList.get(navNum); 
	}
	public Item moveToLastItem()
	{ 
		navNum = itemList.size()-1;
		return itemList.get(navNum); 
	}
}
