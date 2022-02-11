package topupwebshop;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		//instance for a customer
		Customer customer = new Customer("Bhatti", 15000, "HUF");
		Transaction transaction = new Transaction();
		ArrayList<Product> scannedFixedProducts = null;
		ArrayList<Product> scannedVariableProducts = null;
		String transactionID = "#######";
		int selectedID;
		
		while(true) {
			printOptions();
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			int input = scanner.nextInt();
			
			switch (input) {
			case 1:
				//Fix-priced products top-up
				if(scannedFixedProducts == null) {
					scannedFixedProducts = getProducts("src/topupwebshop/fix_priced_products.txt");
				}
				for(Product p: scannedFixedProducts) {
					System.out.println(p);
				}
				System.out.print("Enter the productID of the product: ");
				selectedID = scanner.nextInt();
				transaction.topUp(customer, scannedFixedProducts, selectedID);
				transactionID = String.format("%07d", (customer.getTransactionHistory().get(customer.getTransactionHistory().size()-1).getTransacID()));
				System.out.println("Product " + scannedFixedProducts.get(selectedID-1).getName() + "is purchased. " + 
						"The transaction ID is: " + transactionID);
				System.out.println("Your current balance is: " + customer.getBalance());
				break;
				
			case 2:
				//Variable-priced products top-up
				if(scannedVariableProducts == null) {
					scannedVariableProducts = getProducts("src/topupwebshop/variable_priced_products.txt");
				}
				for(Product p: scannedVariableProducts) {
					System.out.println(p);
				}
				System.out.print("Enter the productID of the product: ");
				selectedID = scanner.nextInt();
				transaction.topUp(customer, scannedVariableProducts, selectedID);
				transactionID = String.format("%07d", (customer.getTransactionHistory().get(customer.getTransactionHistory().size()-1).getTransacID()));
				System.out.println("Product " + scannedVariableProducts.get(selectedID-1).getName() + "is purchased. " + 
						"The transaction ID is: " + transactionID);
				System.out.println("Your current balance is: " + customer.getBalance());
				break;
				
			case 3:
				//refund the product
				System.out.println("Your transaction history is following:");
				for(Transaction t: customer.getTransactionHistory()) {
					System.out.println(t);
				}
				System.out.print("Select a transaction ID from the above: ");
				selectedID = scanner.nextInt();
				
				String productType = null;
				String productName = null;
				for(Transaction t: customer.getTransactionHistory()) {
					if(t.getTransacID() == selectedID) {
						productType = t.getProductType();
						break;
					}
					else {
						System.err.print("Entered transaction ID is not valid!");
					}
				}
				
				if(productType.contentEquals("FIX_PRICED")) {
					productName = transaction.refund(customer, selectedID, scannedFixedProducts);
				} else {
					productName = transaction.refund(customer, selectedID, scannedVariableProducts);
				}
				System.out.println("Product " + productName + " is refunded. Your current balance is: " + customer.getBalance());
				break;
				
			case 4:
				System.out.println("Closing website window..." + "\n\n" + "Dear " + customer.getName() + 
						"!\n" + "Your remaining balance is: " +  customer.getCurrency() + customer.getBalance()
						+ "\n" +	"Your purchased products are following:");
				if(customer.getPurchasedProducts().isEmpty()) {
					System.out.println("null");
				}
				else {
					for(Product p: customer.getPurchasedProducts()) {
						System.out.println(p);
					}
				}
				return;
				
			default:
				System.out.println("Invalid input!\n");
				break;
			}
		}
	};
	
	private static void printOptions() {
		System.out.println("(1) Buy a fix priced product.");
		System.out.println("(2) Buy a variable priced product.");
		System.out.println("(3) Refund an order.");
		System.out.println("(4) Close the webshop window.");
		System.out.print("Select an option: ");
	}
	
	private static ArrayList<Product> getProducts(String filename) {
		ArrayList<Product> products = new ArrayList<Product>();
		File file = new File(filename);
		int productID = 0;
		Scanner scFile = null;
		Scanner scLine = null;
		
		try {
			scFile = new Scanner(file);
			while(scFile.hasNextLine()) {
				String line = scFile.nextLine();
				scLine = new Scanner(line);
				scLine.useDelimiter(";");
				
				String name = scLine.next();
				int price = 0;
				if(filename.contains("fix")) {
					String stPrice = scLine.next();
					if (stPrice.contentEquals("")) continue;		//skip the products which have missing attributes
					price = Integer.parseInt(stPrice);
				}
				else if(filename.contains("variable")) {
					String stMinPrice = scLine.next();
					if (stMinPrice.contentEquals("")) continue;		//skip the products which have missing attributes
					int minPrice = Integer.parseInt(stMinPrice);
					String stMaxPrice = scLine.next();
					if (stMaxPrice.contentEquals("")) continue;		//skip the products which have missing attributes
					int maxPrice = Integer.parseInt(stMaxPrice);
					price = (int) (Math.random() * (maxPrice - minPrice)) + minPrice;
				}
				
				String currency = scLine.next();
				String stQuantity = scLine.next();
				if (stQuantity.contentEquals("") || Integer.parseInt(stQuantity)<=0) continue;		//skip the products which have missing attributes
				int quantity = Integer.parseInt(stQuantity);
				String stDateTime = scLine.next();
				if (stDateTime.contentEquals("")) continue;		//skip the products which have missing attributes
				LocalDateTime creationDateTime = LocalDateTime.parse(stDateTime);
				String type = scLine.next();
				
				productID++;
				Product scProduct = new Product(productID, name, currency, price, quantity, creationDateTime, type);
				
				products.add(scProduct);
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		}
		scFile.close();
		scLine.close();
		
		return products;
	}
	
}
