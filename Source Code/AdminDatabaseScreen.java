import javafx.collections.ObservableList;

import javafx.geometry.Insets;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

class AdminDatabaseScreen {
	
	private TableView<Product> table;
	
	private TextField bookField;
	private TextField authorField;
	private TextField publisherField;
	private TextField priceField;
	private TextField isbnField;
	private Spinner<Integer> quantityField;
	
	private boolean isSaved = true;
	
	void display() {
		
		Stage window = new Stage();
		window.setTitle("Admin Database");
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
		
		//Book Field
		bookField = new TextField();
		bookField.setPromptText("Book");
		bookField.setPrefWidth(bookColumn.getWidth() - 10);
		
		//Author Field
		authorField = new TextField();
		authorField.setPromptText("Author");
		authorField.setPrefWidth(authorColumn.getWidth() - 10);
		
		//Publisher Field
		publisherField = new TextField();
		publisherField.setPromptText("Publisher");
		publisherField.setPrefWidth(publisherColumn.getWidth() - 10);
		
		//Price Field
		priceField = new TextField();
		priceField.setPromptText("Price");
		priceField.setPrefWidth(priceColumn.getWidth() - 10);
		
		//ISBN Field
		isbnField = new NumberTextField();
		isbnField.setPromptText("ISBN");
		isbnField.setPrefWidth(isbnColumn.getWidth() - 10);
		
		//Quantity Field
		quantityField = new Spinner<>(0, Integer.MAX_VALUE, 1);
		quantityField.setPrefWidth(quantityColumn.getWidth() - 10);
		quantityField.setEditable(false);
		quantityField.setPromptText("Quantity");
		
		//Add Button
		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			try {
				addButtonClicked();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Delete Button
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> deleteButtonClicked());
		
		//Save Button
		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> {
			try {
				writeToFile();
				isSaved = true;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Addition Layout
		HBox additionLayout = new HBox();
		additionLayout.setPadding(new Insets(10, 10, 10, 10));
		additionLayout.setSpacing(10);
		additionLayout.getChildren().addAll(bookField, authorField, publisherField, priceField, isbnField, quantityField, addButton, deleteButton, saveButton);
		
		//Create Table
		table = new TableView<>();
		table.getColumns().addAll(Arrays.asList(bookColumn, authorColumn, publisherColumn, priceColumn, isbnColumn, quantityColumn));
		try {
			readFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Table and Addition Layout stacked vertically
		VBox layout = new VBox();
		layout.getChildren().addAll(table, additionLayout);
		
		//Fill text fields with data from double clicked row
		table.setRowFactory( tv -> {
			TableRow<Product> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					Product rowData = row.getItem();
					bookField.setText(rowData.getBook());
					authorField.setText(rowData.getAuthor());
					publisherField.setText(rowData.getPublisher());
					priceField.setText(rowData.getPrice()+"");
					isbnField.setText(rowData.getIsbn()+"");
					quantityField.getValueFactory().setValue(rowData.getQuantity());
				}
			});
			return row ;
		});
		
		Scene scene = new Scene(layout);
		
		window.setScene(scene);
		window.setOnCloseRequest(e -> {
			if(!isSaved) {
				boolean save = ConfirmBox.display("Save changes?", "Do you want to save your changes before exit?");
				if(save) {
					try {
						writeToFile();
						isSaved = true;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		window.showAndWait();
		
	}
	
	private void addButtonClicked() {
		
		Product product = new Product();
		
		//Check if Book field is empty
		product.setBook(bookField.getText());
		if(product.getBook().equals("")) {
			AlertBox.display("Error", "Book field is empty");
			return;
		}
		
		//Check if Author field is empty
		product.setAuthor(authorField.getText());
		if(product.getAuthor().equals("")) {
			AlertBox.display("Error", "Author field is empty");
			return;
		}
		
		//Check if Publisher field is empty
		product.setPublisher(publisherField.getText());
		if(product.getPublisher().equals("")) {
			AlertBox.display("Error", "Publisher field is empty");
			return;
		}
		
		//Check if Price field is empty
		if(priceField.getText().equals("")) {
			AlertBox.display("Error", "Price field is empty");
			return;
		}
		//Check if Price is decimal
		try {
			product.setPrice(Double.parseDouble(priceField.getText()));
		} catch (NumberFormatException e) {
			AlertBox.display("Error", "Price should be decimal");
			return;
		}
		
		//Check if ISBN Field is empty
		if(isbnField.getText().equals("")) {
			AlertBox.display("Error", "ISBN field is empty");
			return;
		}
		product.setIsbn(Long.parseLong(isbnField.getText()));
		//Check if ISBN is 13 digits long
		if((product.getIsbn()+"").length() != 13) {
			AlertBox.display("Error", "ISBN should be 13 digits long");
			return;
		}
		//Check if ISBN is valid
		if(!isIsbn(product.getIsbn())) {
			AlertBox.display("Error", "Invalid ISBN");
			return;
		}
		
		try {
			product.setQuantity(quantityField.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Check if the record is already present
		if(allFieldsMatch()) {
			int recordIndex = getRecordIndex(product.getBook());
			product.setQuantity(product.getQuantity());
			table.getItems().set(recordIndex, product);
			isSaved = false;
			return;
		}
		
		//Check if ISBN already exists
		if(hasIsbn(product.getIsbn())) {
			AlertBox.display("Error", "ISBN already exists");
			return;
		}
		
		table.getItems().add(product);
		isSaved = false;
		bookField.clear();
		authorField.clear();
		publisherField.clear();
		priceField.clear();
		isbnField.clear();
		quantityField.getValueFactory().setValue(1);
		
	}
	
	private void deleteButtonClicked() {
		
		ObservableList<Product> selectedProducts, allProducts;
		allProducts = table.getItems();
		selectedProducts = table.getSelectionModel().getSelectedItems();
		selectedProducts.forEach(allProducts::remove);
		isSaved = false;
		
	}
	
	private boolean hasIsbn(long isbn) {
		
		for(Product pro : table.getItems())
			if (pro.getIsbn() == isbn)
				return true;
		return false;
		
	}
	
	private boolean isIsbn(long isbn) {
		
		long part1, part2;
		part1 = Long.parseLong((isbn+"").substring(0, 12));
		part2 = Long.parseLong((isbn+"").substring(12));
		long sum = 0;
		for(int i=1; i <=12; i++) {
			if(i%2==0)
				sum += part1 % 10;
			else
				sum += part1 % 10 * 3;
			part1 /= 10;
		}
		long checkDigit = 10 - sum % 10;
		if(checkDigit == 10)
			checkDigit = 0;
		return checkDigit == part2;
		
	}
	
	private boolean allFieldsMatch() {
		
		String sql = "SELECT * FROM records WHERE book = ? AND author = ? AND publisher = ? AND price = ? AND isbn = ?";
		
		try (
				Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setString(1, bookField.getText());
			pstmt.setString(2, authorField.getText());
			pstmt.setString(3, publisherField.getText());
			pstmt.setDouble(4, Double.parseDouble(priceField.getText()));
			pstmt.setString(5, isbnField.getText());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
		
	}
	
	private int getRecordIndex(String book) {
		
		createNewTable();
		String sql = "SELECT * FROM records";
		
		int index = 0;
		
		try (
				Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs	= stmt.executeQuery(sql)
		) {
			while (rs.next()) {
				
				if (rs.getString("book").equalsIgnoreCase(book) )
					return index;
				rs.getString("author");
				rs.getString("quantity");
				rs.getDouble("price");
				rs.getString("isbn");
				rs.getInt("quantity");
				
				index++;
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return index;
		
	}
	
	public static void deleteTable() {
		
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
	
	public static void createNewTable() {
		
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
	
	public static Connection connect() {
		String url = "jdbc:sqlite:Books.sqlitedb";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	static void writeData(String book, String author, String publisher, double price, String isbn, int quantity) {
		
		createNewTable();
		
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
	
	private void writeToFile() {
		
		deleteTable();
		
		for(Product pro : table.getItems())
			writeData(pro.getBook(), pro.getAuthor(), pro.getPublisher(), pro.getPrice(), pro.getIsbn()+"", pro.getQuantity());
		
	}
	
	private void readFromFile() {
		
		createNewTable();
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