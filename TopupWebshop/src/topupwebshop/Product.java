package topupwebshop;

import java.time.LocalDateTime;

public class Product {
	private int productID;
	private String name;
	private String currency;
	private int price;
	private int quantity;
	private LocalDateTime creationDateTime;
	private String type;
	
	public Product() {
		super();
	}

	public Product(int producatID, String name, String currency, int price, int quantity, LocalDateTime creationDateTime, String type) {
		super();
		this.setProductID(producatID);
		this.name = name;
		this.currency = currency;
		this.price = price;
		this.quantity = quantity;
		this.creationDateTime = creationDateTime;
		this.type = type;
	}

	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}
	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return  productID + ": " + name + ", " + price + " " + currency + ", " + quantity + "pcs, " + creationDateTime + ", " + type;
	}

}
