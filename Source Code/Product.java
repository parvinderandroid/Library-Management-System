public class Product {
	
	private String book;
	private String author;
	private String publisher;
	private double price;
	private long isbn;
	private int quantity;
	
	public Product() {
		book = "";
		author = "";
		publisher = "";
		price = 0D;
		isbn = 0L;
		quantity = 0;
	}
	
	public String getBook() {
		return book;
	}
	
	public void setBook(String book) {
		this.book = book;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public long getIsbn() {
		return isbn;
	}
	
	public void setIsbn(long isbn) {
		this.isbn= isbn;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}