package is.domain;

import java.util.Date;
import java.util.List;

public class InventoryAdjustment 
{
	private int id;
	private String transactionNo;
	private Date transactionDate;
	private double totalCost;
	private List<InventoryAdjustmentDetails> invdjdetailList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public List<InventoryAdjustmentDetails> getInadjdetailList() {
		return invdjdetailList;
	}
	public void setInvadjdetailList(List<InventoryAdjustmentDetails> invadjdetailList) {
		this.invdjdetailList = invadjdetailList;
	}
	

}
