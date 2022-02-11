package topupwebshop;

import java.util.ArrayList;

public class Customer {
	private String name;
	private int balance;
    private String currency;
    private ArrayList<Product> purchasedProducts = new ArrayList<Product>();
    private ArrayList<Transaction> transactionHistory = new ArrayList<Transaction>();
    
    
    public Customer() {
		super();
	}
    
    public Customer(String name, int balance, String currency) {
		super();
		this.name = name;
		this.balance = balance;
		this.currency = currency;
	}

	public Customer(String name, int balance, String currency, ArrayList<Product> purchasedProducts,
			ArrayList<Transaction> transactionHistory) {
		super();
		this.name = name;
		this.balance = balance;
		this.currency = currency;
		this.purchasedProducts = purchasedProducts;
		this.transactionHistory = transactionHistory;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public ArrayList<Product> getPurchasedProducts() {
		return purchasedProducts;
	}
	public void setPurchasedProducts(ArrayList<Product> purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}
	public ArrayList<Transaction> getTransactionHistory() {
		return transactionHistory;
	}
	public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}
    
}
