package is.domain;

public class InventoryAdjustmentDetails 
{
	private int id, inventoryadjustmentid, itemid, uomid;
	private double quantity, unitprice, totalCost;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInventoryadjustmentid() {
		return inventoryadjustmentid;
	}
	public void setInventoryadjustmentid(int inventoryadjustmentid) {
		this.inventoryadjustmentid = inventoryadjustmentid;
	}
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}
	public int getUomid() {
		return uomid;
	}
	public void setUomid(int uomid) {
		this.uomid = uomid;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(double unitprice) {
		this.unitprice = unitprice;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	
	

}
