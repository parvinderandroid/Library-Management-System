import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Arrays;

class StudentDatabaseScreen {
	
	private TableView<Product> table;
	
	void display(String username) {
		
		Stage window = new Stage();
		window.setTitle("Books Database");
		window.initModality(Modality.APPLICATION_MODAL);
		
		//Book Column
		TableColumn<Product, String> bookColumn = new TableColumn<>("Book");
		bookColumn.setMinWidth(200);
		bookColumn.setCellValueFactory(new PropertyValueFactory<>("book"));
		
		//Author Column
		TableColumn<Product, String> authorColumn = new TableColumn<>("Author");
		authorColumn.setMinWidth(200);
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		
		//Publisher Column
		TableColumn<Product, String> publisherColumn = new TableColumn<>("Publisher");
		publisherColumn.setMinWidth(200);
		publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		
		//Price Column
		TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setMinWidth(100);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		//ISBN Column
		TableColumn<Product, Long> isbnColumn = new TableColumn<>("ISBN");
		isbnColumn.setMinWidth(200);
		isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		
		//Quantity Column
		TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setMinWidth(100);
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		//Issue Button
		Button issueButton = new Button("Issue");
		issueButton.setOnAction(e -> {
			try {
				issueButtonClicked(username);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Return Button
		Button returnButton = new Button("Return");
		returnButton.setOnAction(e -> {
			try {
				returnButtonClicked(username);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Buy Button
		Button buyButton = new Button("Buy");
		buyButton.setOnAction(e -> buyButtonClicked());
		
		//Horizontal stacking of all buttons
		HBox buttonLayout = new HBox();
		buttonLayout.setPadding(new Insets(10, 10, 10, 10));
		buttonLayout.setSpacing(10);
		buttonLayout.getChildren().addAll(issueButton, returnButton, buyButton);
		
		//Create Table
		table = new TableView<>();
		table.getColumns().addAll(Arrays.asList(bookColumn, authorColumn, publisherColumn, priceColumn, isbnColumn, quantityColumn));
		try {
			readFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Vertical stacking of table and buttonLayout
		VBox layout = new VBox();
		layout.getChildren().addAll(table, buttonLayout);
		
		Scene scene = new Scene(layout);
		
		window.setScene(scene);
		window.setOnCloseRequest(e -> {
			try {
				writeToFile();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		window.showAndWait();
		
	}
	
	private static void createNewTable() {
		
		String url = "jdbc:sqlite:Books.sqlitedb";
		
		String sql = "CREATE TABLE IF NOT EXISTS issues (\n"
				+ "	username text,\n"
				+ "	isbn text\n"
				+ ");";
		
		try (
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement()
			) {
				stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	
	}
	
	private static void deleteTable2() {
		
		String url = "jdbc:sqlite:Books.sqlitedb";
		
		String sql = "DROP TABLE IF EXISTS records";
		
		try (
				Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()
		) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private static void createNewTable2() {
		
		String url = "jdbc:sqlite:Books.sqlitedb";
		
		String sql = "CREATE TABLE IF NOT EXISTS records (\n"
				+ "	username text,\n"
				+ "	book text,\n"
				+ "	author text,\n"
				+ "	publisher text,\n"
				+ "	price real,\n"
				+ "	isbn text,\n"
				+ "	quantity integer\n"
				+ ");";
		
		try (
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement()
			) {
				stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	
	}
	
	private static Connection connect() {
		String url = "jdbc:sqlite:Books.sqlitedb";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	private static void writeData(String username, String isbn) {
		
		createNewTable();
		
		String sql = "INSERT INTO issues(username,isbn) VALUES(?,?)";
		try (
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			) {
			pstmt.setString(1, username);
			pstmt.setString(2, isbn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private static void writeData2(String book, String author, String publisher, double price, String isbn, int quantity) {
		
		createNewTable2();
		
		String sql = "INSERT INTO records(book,author,publisher,price,isbn,quantity) VALUES(?,?,?,?,?,?)";
		try (
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			) {
			pstmt.setString(1, book);
			pstmt.setString(2, author);
			pstmt.setString(3, publisher);
			pstmt.setDouble(4, price);
			pstmt.setString(5, isbn);
			pstmt.setInt(6, quantity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private static void deleteData(String username, String isbn) {
		
		createNewTable();
		
		String sql = "DELETE FROM issues WHERE username = ? and isbn = ?";
		try (
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			) {
			pstmt.setString(1, username);
			pstmt.setString(2, isbn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private void issueButtonClicked(String username) {
		
		Product selectedProduct = table.getSelectionModel().getSelectedItem();
		int quantity = selectedProduct.getQuantity();
		long isbn = selectedProduct.getIsbn();
		if(quantity == 0)
			AlertBox.display("Error", "No more books in store, please come back later!");
		else if(hasIsbn(username, isbn+""))
			AlertBox.display("Error", "You already have this book");
		else {
			selectedProduct.setQuantity(selectedProduct.getQuantity() - 1);
			table.getItems().set(table.getSelectionModel().getFocusedIndex(), selectedProduct);			
			writeData(username, isbn+"");
		}
		
	}
	
	private void returnButtonClicked(String username) {
		
		Product selectedProduct = table.getSelectionModel().getSelectedItem();
		long isbn = selectedProduct.getIsbn();
		if(!hasIsbn(username, isbn+""))
			AlertBox.display("Error", "You don't have this book");
		else {
			selectedProduct.setQuantity(selectedProduct.getQuantity() + 1);
			table.getItems().set(table.getSelectionModel().getFocusedIndex(), selectedProduct);
			deleteData(username, isbn+"");
		}
		
	}
	
	private void buyButtonClicked() {
		
		Product selectedProduct = table.getSelectionModel().getSelectedItem();
		int quantity = selectedProduct.getQuantity();
		if(quantity <= 0)
			AlertBox.display("Error", "No more books in store, please come back later!");
		else {
			selectedProduct.setQuantity(selectedProduct.getQuantity() - 1);
			table.getItems().set(table.getSelectionModel().getFocusedIndex(), selectedProduct);
		}
		
	}
	
	private boolean hasIsbn(String username, String isbn) {
		
		createNewTable();
		
		username = "'" + username + "'";
		isbn = "'" + isbn + "'";
		String sql = "SELECT * FROM issues WHERE username = " + username + " AND isbn = " + isbn;
		
		try (
			Connection conn = connect();
			Statement stmt  = conn.createStatement();
			ResultSet rs	= stmt.executeQuery(sql)
			) {
				while (rs.next()) {
					return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
		
	}
	
	private void writeToFile() {
		
		deleteTable2();
		
		for(Product pro : table.getItems())
			writeData2(pro.getBook(), pro.getAuthor(), pro.getPublisher(), pro.getPrice(), pro.getIsbn()+"", pro.getQuantity());
		
	}
	
	private void readFromFile() {
		
		String sql = "SELECT * FROM records";
		
		try (
			Connection conn = connect();
			Statement stmt  = conn.createStatement();
			ResultSet rs	= stmt.executeQuery(sql)
			) {
				while (rs.next()) {
					
					Product product = new Product();
					product.setBook(rs.getString("book"));
					product.setAuthor(rs.getString("author"));
					product.setPublisher(rs.getString("publisher"));
					product.setPrice(rs.getDouble("price"));
					product.setIsbn(Long.parseLong(rs.getString("isbn")));
					product.setQuantity(rs.getInt("quantity"));
					table.getItems().add(product);
					
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}