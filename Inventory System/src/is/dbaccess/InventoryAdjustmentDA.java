package is.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import is.domain.InventoryAdjustment;
import is.domain.InventoryAdjustmentDetails;

public class InventoryAdjustmentDA 
{
	private List<InventoryAdjustment> invAdjList;
	private Connection conn;
	private int navNum;
	
	public InventoryAdjustmentDA(Connection paramConn)
	{
		conn = paramConn;
		navNum = 0;
		initializeList();
	}
	
	public void initializeList() 
	{
		try
		{
			invAdjList = new ArrayList<InventoryAdjustment>();
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Id, TransactionNo, TransactionDate, TotalCost FROM tblInventoryAdjustments");
			while(rs.next()) 
			{
				InventoryAdjustment invAdj = new InventoryAdjustment();
				invAdj.setId(rs.getInt(1));
				invAdj.setTransactionNo(rs.getString(2));
				invAdj.setTransactionDate(rs.getDate(3));
				invAdj.setTotalCost(rs.getDouble(4));
				invAdj.setInvadjdetailList(initializeDetailList(rs.getInt(1)));
				invAdjList.add(invAdj);
			}
			
		}catch(Exception e0) { e0.printStackTrace();}
	}
	
	public List<InventoryAdjustmentDetails> initializeDetailList(int inventoryAdjustmentId) 
	{
		List<InventoryAdjustmentDetails> invAdjDetailList = new ArrayList<InventoryAdjustmentDetails>();
		try
		{
			//invAdjDetailList = new ArrayList<InventoryAdjustmentDetails>();
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Id, InventoryAdjustmentId, ItemId, UOMId, Quantity, UnitPrice, TotalCost FROM tblInventoryAdjustmentDetails WHERE InventoryAdjustmentId = " + inventoryAdjustmentId);
			while(rs.next()) 
			{
				InventoryAdjustmentDetails invAdjDetail = new InventoryAdjustmentDetails();
				invAdjDetail.setId(rs.getInt(1));
				invAdjDetail.setInventoryadjustmentid(rs.getInt(2));
				invAdjDetail.setItemid(rs.getInt(3));
				invAdjDetail.setUomid(rs.getInt(4));
				invAdjDetail.setQuantity(rs.getDouble(5));
				invAdjDetail.setUnitprice(rs.getDouble(6));
				invAdjDetail.setTotalCost(rs.getDouble(7));
				invAdjDetailList.add(invAdjDetail);
			}
			
		}catch(Exception e0) { e0.printStackTrace();}
		
		return invAdjDetailList;
	}
	
	public DefaultTableModel viewAllInventoryAdjustments() 
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
		
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Id, TransactionNo, TransactionDate, TotalCost, DateCreated AS [Date Created], DateModified AS [Date Modified] FROM tblInventoryAdjustments");
			ResultSetMetaData rsmd = rs.getMetaData();
		
			Object[] columns = new Object[6];
			
			for(int inc = 1; inc <= rsmd.getColumnCount(); inc++) 
				columns[inc-1] = rsmd.getColumnLabel(inc);
			
			dm.setColumnIdentifiers(columns);
			
			while(rs.next()) 
			{
				Object[] row = new Object[6];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getDate(3);
				row[3] = rs.getDouble(4);
				row[4] = rs.getDate(5);
				row[5] = rs.getDate(6);
				dm.addRow(row);
			}
			
		}catch(Exception e1) { e1.printStackTrace();}
		
		return dm;
	}
	
	public DefaultTableModel viewDetailTable(boolean isNew, InventoryAdjustment invAdj) 
	{
		//Set Model Not Editable
		DefaultTableModel dm  = new DefaultTableModel(){

	        @Override
	        public boolean isCellEditable(int row, int column)
	        {
	            // make read only fields except column 2, 4, 5
	            return column == 2 || column == 4 || column == 5;
	        }
	    };
		
		try
		{
			ItemDA itemDA = new ItemDA(conn);
			UnitOfMeasureDA uomDA = new UnitOfMeasureDA(conn);
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT Id, InventoryAdjustmentId, ItemId AS Item, UOMId AS Unit, Quantity, UnitPrice AS [Unit Price], TotalCost AS [Total Cost] FROM tblInventoryAdjustmentDetails");
			ResultSetMetaData rsmd = rs.getMetaData();
		
			Object[] columns = new Object[7];
			
			for(int inc = 1; inc <= rsmd.getColumnCount(); inc++) 
				columns[inc-1] = rsmd.getColumnLabel(inc);
			
			dm.setColumnIdentifiers(columns);
			
			if (!isNew && invAdj != null) {
				for(InventoryAdjustmentDetails iad: invAdj.getInadjdetailList()) 
				{
					Object[] row = new Object[7];
					row[0] = iad.getId();
					row[1] = iad.getInventoryadjustmentid();
					
					row[2] = itemDA.openSelectedItem(String.valueOf(iad.getItemid())).getName();
					row[3] = uomDA.getIdValue(iad.getUomid()).getCode();
					
					row[4] = iad.getQuantity();
					row[5] = iad.getUnitprice();
					row[6] = iad.getTotalCost();
					dm.addRow(row);
				}	
			}else if (isNew){
				dm.addRow(new Object[7]);
			}
			
		}catch(Exception e1) { e1.printStackTrace();}
		
		return dm;
	}
	
	public String getTransactionNoSeries() 
	{
		StringBuffer transNo = new StringBuffer("IA-");
		String numberSeries = "";
		
		try 
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT SeriesNumber FROM tblTransactionNoSeries WHERE Code = 'IA'");
			rs.next();
			
			numberSeries = String.valueOf(rs.getInt(1));
			switch(numberSeries.length()) 
			{
				case 1: transNo.append("00000"); break;
				case 2: transNo.append("0000"); break;
				case 3: transNo.append("000"); break;
				case 4: transNo.append("00"); break;
				case 5: transNo.append("0"); break;
				case 6: transNo.append(""); break;
			}
			transNo.append(numberSeries);
			
		}catch(SQLException e) { e.printStackTrace(); }
		
		return transNo.toString();
	}
	
	public void addInventoryAdjustment(InventoryAdjustment ia)
	{
		try 
		{
			String queryMaster = "INSERT INTO tblInventoryAdjustments(TransactionNo, TransactionDate, TotalCost, DateCreated, DateModified)\r\n" + 
						         "VALUES(?, ?, ?, GETDATE(), GETDATE())";
			String queryDetail = "INSERT INTO tblInventoryAdjustmentDetails (InventoryAdjustmentId, ItemId, UOMId, Quantity, UnitPrice, TotalCost, DateCreated, DateModified)\r\n" + 
							     "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
			int masterId = 0;
			
			//Insert into Master
			PreparedStatement ps = conn.prepareStatement(queryMaster);
			ps.setString(1, ia.getTransactionNo());
			ps.setTimestamp(2, new Timestamp(ia.getTransactionDate().getTime()));
			ps.setDouble(3, ia.getTotalCost());
			ps.execute();
			
			//Get the Latest Id
			Statement s1 = conn.createStatement();
			//ResultSet rs1 = s1.executeQuery("SELECT DISTINCT SCOPE_IDENTITY() AS LatestId FROM tblInventoryAdjustments");
			s1.execute("SELECT MAX(Id) FROM tblInventoryAdjustments");    
			//ResultSet rs2 = s2.getResultSet(); // 
			//ResultSet rs1 = s1.executeQuery("SELEC T MAX(Id) FROM tblInventoryAdjustments");
			ResultSet rs1 = s1.getResultSet();
			if (rs1.next()) {
				masterId = rs1.getInt(1);
			}
			//rs1.next();
			 
			
			//Insert into Details
			PreparedStatement ps1 = conn.prepareStatement(queryDetail);
			for(InventoryAdjustmentDetails invadjdetail: ia.getInadjdetailList()) 
			{
				ps1.setInt(1, masterId);// Inventory Adjustment Id
				ps1.setInt(2, invadjdetail.getItemid());// Item Id
				ps1.setInt(3, invadjdetail.getUomid());// UOMId 
				ps1.setDouble(4, invadjdetail.getQuantity());// Quantity
				ps1.setDouble(5, invadjdetail.getUnitprice());// Unit Price
				ps1.setDouble(6, invadjdetail.getTotalCost());// Total Cost
				ps1.execute();
			}
			
			//Increment the Transaction No
			PreparedStatement ps2 = conn.prepareStatement("UPDATE tblTransactionNoSeries SET SeriesNumber = SeriesNumber + 1 WHERE Code = 'IA'");
			ps2.executeUpdate();
			
			JOptionPane.showMessageDialog(null, "Inventory Adjustment Added Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
			initializeList();
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public void updateInventoryAdjustment(InventoryAdjustment ia) 
	{
		try 
		{
			String updateMaster = "UPDATE tblInventoryAdjustments \r\n" + 
								  "	SET TransactionNo = ?, \r\n" + 
								  "		TransactionDate = ?,\r\n" + 
								  "		TotalCost = ?,\r\n" + 
								  "		DateModified = GETDATE()\r\n" + 
								  "WHERE Id = ?";
			
			String updateDetail = "UPDATE tblInventoryAdjustmentDetails\r\n" + 
								  "	SET InventoryAdjustmentId = ?,\r\n" + 
								  "		ItemId = ?,\r\n" + 
								  "		UOMId = ?,\r\n" +  
								  "		Quantity = ?,\r\n" + 
								  "		UnitPrice = ?,\r\n" + 
								  "		TotalCost = ?,\r\n" + 
								  "		DateModified = GETDATE()\r\n" + 
								  "WHERE Id = ?";
			
			String insertqueryDetail = "INSERT INTO tblInventoryAdjustmentDetails (InventoryAdjustmentId, ItemId, UOMId, Quantity, UnitPrice, TotalCost, DateCreated, DateModified)\r\n" + 
				     			 "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
			
			PreparedStatement psMaster = conn.prepareStatement(updateMaster);
			psMaster.setString(1, ia.getTransactionNo());
			psMaster.setTimestamp(2, new Timestamp(ia.getTransactionDate().getTime()));
			psMaster.setDouble(3, ia.getTotalCost());
			psMaster.setInt(4, ia.getId());
			psMaster.execute();
			System.out.println("Updated Master");
			
			PreparedStatement psDetail = conn.prepareStatement(updateDetail);
			PreparedStatement psInsertDetail = conn.prepareStatement(insertqueryDetail);
			
			for(InventoryAdjustmentDetails iad:ia.getInadjdetailList()) 
			{
				if(iad.getId() == 0) 
				{	
					System.out.println("Insert");
					psInsertDetail.setInt(1, ia.getId());// Inventory Adjustment Id
					psInsertDetail.setInt(2, iad.getItemid());// Item Id
					psInsertDetail.setInt(3, iad.getUomid());// UOMId 
					psInsertDetail.setDouble(4, iad.getQuantity());// Quantity
					psInsertDetail.setDouble(5, iad.getUnitprice());// Unit Price
					psInsertDetail.setDouble(6, iad.getTotalCost());// Total Cost
					psInsertDetail.execute();
				}else 
				{
					System.out.println("Update");
					psDetail.setInt(1, iad.getId());// Inventory Adjustment Id
					psDetail.setInt(2, iad.getItemid());// Item Id
					psDetail.setInt(3, iad.getUomid());// UOMId 
					psDetail.setDouble(4, iad.getQuantity());// Quantity
					psDetail.setDouble(5, iad.getUnitprice());// Unit Price
					psDetail.setDouble(6, iad.getTotalCost());// Total Cost
					psDetail.setInt(7, iad.getId());// Id
					psDetail.execute();
				}
			}
			initializeList();
			JOptionPane.showMessageDialog(null, "Inventory Adjustment Updated Successfully", "", JOptionPane.INFORMATION_MESSAGE);
		}catch(SQLException sqle) {sqle.printStackTrace();}
		
		
	}
	
	public void deleteInventoryAdjustments(InventoryAdjustment ia) 
	{
		try 
		{
			String deleteQuery = "DELETE FROM tblInventoryAdjustments WHERE Id = ?";
			String deleteDetails = "DELETE FROM tblInventoryAdjustmentDetails WHERE InventoryAdjustmentId = ?";
			
			PreparedStatement ps = conn.prepareStatement(deleteQuery);
			ps.setInt(1, ia.getId());
			ps.execute();
			
			PreparedStatement ps1 = conn.prepareStatement(deleteDetails);
			ps1.setInt(1, ia.getId());
			ps1.execute();
			
			initializeList();
			JOptionPane.showMessageDialog(null, "Inventory Adjustment Deleted Successfully", "", JOptionPane.INFORMATION_MESSAGE);
			
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	public void deleteInventoryAdjustmentDetails(List<InventoryAdjustmentDetails> iad) 
	{
		try 
		{
			String deleteQuery = "DELETE FROM tblInventoryAdjustmentDetails WHERE Id = ?";
			PreparedStatement ps = conn.prepareStatement(deleteQuery);
			
			for(InventoryAdjustmentDetails deleteIad: iad) 
			{
				ps.setInt(1, deleteIad.getId());
				ps.execute();
			}
			System.out.println("Delete Details");
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	public InventoryAdjustment openSelectedIA(String iaId) 
	{
		int id = Integer.parseInt(iaId);
		for(InventoryAdjustment ia: invAdjList) 
		{
			if (ia.getId() == id)
			{
				return ia;
			}
			navNum++;
		}
		return null;
	}
	
	public InventoryAdjustment moveToFirstIA() 
	{
		navNum = 0;
		return invAdjList.get(navNum); 
	}
	public InventoryAdjustment moveToPrevIA() 
	{
		navNum--;
		if(navNum < 0)
			navNum = 0;
		return invAdjList.get(navNum); 
	}
	public InventoryAdjustment moveToNextIA() 
	{	
		navNum++;
		if(navNum == invAdjList.size())
			navNum = invAdjList.size()-1;
		return invAdjList.get(navNum); 
	}
	public InventoryAdjustment moveToLastIA()
	{ 
		navNum = invAdjList.size()-1;
		return invAdjList.get(navNum); 
	}
}
