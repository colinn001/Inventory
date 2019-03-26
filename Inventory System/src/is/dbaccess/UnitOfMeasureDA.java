package is.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import is.domain.UnitOfMeasure;

public class UnitOfMeasureDA 
{
	private List<UnitOfMeasure> uomList;
	private Statement initializeQuery;
	private Connection conn;
	private int navNum;
	
	public UnitOfMeasureDA(Connection paramConn) 
	{
		conn = paramConn;
		initializeList();
		navNum = 0;
	}
	
	public void initializeList() 
	{
		try
		{
			uomList = new ArrayList<UnitOfMeasure>();
			initializeQuery = conn.createStatement();
			ResultSet rs = initializeQuery.executeQuery("SELECT Id, Code, Name FROM tblUnitOfMeasures");
			
			while(rs.next()) 
			{
				UnitOfMeasure uom = new UnitOfMeasure();
				uom.setId(rs.getInt(1));
				uom.setCode(rs.getString(2));
				uom.setName(rs.getString(3));
				uomList.add(uom);
			}
			
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	public Object[] getListOfUnits() 
	{
		try 
		{
			int arrSize = 0, inc = 0;
			String dataCountQuery = "SELECT COUNT(Code) FROM tblUnitOfMeasures";
			String query = "SELECT Code FROM tblUnitOfMeasures";
			
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(dataCountQuery);
			rs.next();
			arrSize = rs.getInt(1);
			
			Object units[] = new Object[arrSize];
			
			rs = s.executeQuery(query);
			while(rs.next()) 
			{
				units[inc] = rs.getString(1);
				inc++;
			}
			
			return units;
			
		}catch(SQLException er){
			er.printStackTrace();
		}
		return null;
	}
	
	public UnitOfMeasure getUOMValue(String code) 
	{
		for(UnitOfMeasure u: uomList) 
		{
			if(code.equals(u.getCode())) 
			{
				return u;
			}
		}
		return null;
	}
	
	public UnitOfMeasure getIdValue(int uomId) 
	{
		for(UnitOfMeasure u: uomList) 
		{
			if(uomId == u.getId()) 
			{
				return u;
			}
		}
		return null;
	}

}
