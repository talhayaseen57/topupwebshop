package topupwebshop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Transaction {
	private int transacID;
	private int purchasedProductID;
	private String productType;
	private String transacType;
			
	
	public Transaction() {
		super();
	}
	
	public Transaction(int transacID, int purchasedProductID, String productType, String transacType) {
		super();
		this.transacID = transacID;
		this.purchasedProductID = purchasedProductID;
		this.productType = productType;
		this.transacType = transacType;
	}

	public int getTransacID() {
		return transacID;
	}
	public void setTransacID(int transacID) {
		this.transacID = transacID;
	}
	public String getTransacType() {
		return transacType;
	}
	public void setTransacType(String transacType) {
		this.transacType = transacType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getPurchasedProductID() {
		return purchasedProductID;
	}
	public void setPurchasedProductID(int purchasedProductID) {
		this.purchasedProductID = purchasedProductID;
	}
	

	public void topUp(Customer customer,ArrayList<Product> products, int productID){
		productID = productID-1;
		String filepath = "src/topupwebshop/transactions.txt";
		File transacFile = new File(filepath);
		Product purchasedProduct = products.get(productID);
		String status = "PAID";
				
		double exchangeRate = currencyExchangeRate(customer.getCurrency(), purchasedProduct.getCurrency());
		int exchangedProductPrice = (int) (purchasedProduct.getPrice() / exchangeRate);
		
		//checking the balance of customer
		if(customer.getBalance() < exchangedProductPrice) {
			System.out.println("Not enough balance avaiable.");
			return;
		}
		
		//checking if the product is available i.e. Quantity > 0
		if(purchasedProduct.getQuantity() <= 0) {
			System.out.println("Sorry! The selected product is not available at the moment.");
			return;
		}
		
		//getting the recent transction_id
		int lastTransacID = getRecentTranscID(transacFile);
		
		//top-up the customer's purchased products and update transaction history
		customer.getPurchasedProducts().add(purchasedProduct);
		
		Transaction newTransaction = new Transaction(lastTransacID, productID+1, purchasedProduct.getType(), status);
		customer.getTransactionHistory().add(newTransaction);
		
		customer.setBalance(customer.getBalance() - exchangedProductPrice);
		
		//update the transaction history file
		updateTransacHistoryFile(transacFile, lastTransacID, purchasedProduct, status);
		
		//decrement the quantity of locally stored products
		int newQuantity = products.get(productID).getQuantity()-1;
		products.get(productID).setQuantity(newQuantity);
		
	}
	
	public String refund(Customer customer, int transacID, ArrayList<Product> products) {
		String filepath = "src/topupwebshop/transactions.txt";
		File transacFile = new File(filepath);
		int refundProductID = 0;
		
		for (Transaction t: customer.getTransactionHistory()) {
			if(t.getTransacID()==transacID) {
				refundProductID = t.getPurchasedProductID();
				break;
			}
		}
		
		int refundProductPrice = 0;
		Product removedProduct = new Product();
		String refundProductCurrency = null;
		String status = "REFUNDED";
		
		//initiating the required attributes
		int i = 0;
		for(Product p: customer.getPurchasedProducts()) {
			if(p.getProductID()==refundProductID) {
				removedProduct = p;
				refundProductPrice = p.getPrice();
				refundProductCurrency = p.getCurrency();
				break;
			}
			i++;
		}
		
		//getting the recent transction_id
		int lastTransacID = getRecentTranscID(transacFile);
		
		double exchangeRate = currencyExchangeRate(refundProductCurrency, customer.getCurrency());
		int exchangededRefund = (int) (refundProductPrice / exchangeRate);
		
		//remove the customer's purchased products and update transaction history
		customer.getPurchasedProducts().remove(i);
		customer.setBalance(customer.getBalance() + exchangededRefund);
		
		Transaction newTransaction = new Transaction();
		newTransaction.setTransacID(lastTransacID);
		newTransaction.setTransacType(status);
		customer.getTransactionHistory().add(newTransaction);
		
		//update the transaction history file
		updateTransacHistoryFile(transacFile, lastTransacID, removedProduct, status);
		
		//increment the quantity of locally stored products
		int newQuantity = products.get(refundProductID-1).getQuantity()+1;
		products.get(refundProductID-1).setQuantity(newQuantity);
		
		return removedProduct.getName();
	}
	
	
	private void updateTransacHistoryFile(File transacFile, int lastTransacID, Product purchasedProduct, String status) {
		LocalDateTime purchaseTime = LocalDateTime.now();
		try {
			FileWriter fwriter = new FileWriter(transacFile, true);
			
			String paddedTranscID = String.format("%07d", (lastTransacID));
			fwriter.write(paddedTranscID + ";" + purchasedProduct.getName() + ";" + purchasedProduct.getPrice()
					+ ";" + purchasedProduct.getCurrency() + ";" + purchasedProduct.getType() + ";" + 
					purchaseTime + ";" + status);
			
			if(lastTransacID!=1) {
				fwriter.write("\n");
			}
			
			fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double currencyExchangeRate(String customerCurrency, String productCurrency) {
		double rate = 1;
		
		if(!customerCurrency.contentEquals(productCurrency)) {
			//EUR - to - other currencies
			if(customerCurrency.contentEquals("EUR")) {
				switch (productCurrency) {
				case "HUF":	rate = 353;	break;
				case "GBP":	rate = 0.84; break;
				case "USD":	rate = 1.14; break;
				default: break;
				}
			}
			//USD - to - other currencies
			if(customerCurrency.contentEquals("USD")) {
				switch (productCurrency) {
				case "HUF":	rate = 310;	break;
				case "GBP":	rate = 0.74; break;
				case "EUR":	rate = 0.88; break;
				default: break;
				}
			}
			//GBP - to - other currencies
			if(customerCurrency.contentEquals("GBP")) {
				switch (productCurrency) {
				case "HUF":	rate = 420;	break;
				case "USD":	rate = 1.35; break;
				case "EUR":	rate = 1.19; break;
				default: break;
				}
			}
			//HUF - to - other currencies
			if(customerCurrency.contentEquals("HUF")) {
				switch (productCurrency) {
				case "GBP":	rate = 0.0024;	break;
				case "USD":	rate = 0.0032; break;
				case "EUR":	rate = 0.0028; break;
				default: break;
				}
			}
		}
		
		return rate;
	}

	//method to copy current transaction ID from the file
	private int getRecentTranscID(File transacFile) {
		int transacID = 0;
		try {
			Scanner scFile = new Scanner(transacFile);
			String last = "";
			while(scFile.hasNextLine()) {
				last = scFile.nextLine();
			}
			Scanner scLine = new Scanner(last);
			scLine.useDelimiter(";");
			transacID = scLine.nextInt()+1;	
			
			scFile.close();
			scLine.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transacID;
	}
	
	@Override
	public String toString() {
		return String.format("%07d", (transacID)) + ": " + " Product ID: " + purchasedProductID + ", " + productType + ", " + transacType;
	}

}
