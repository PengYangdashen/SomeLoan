package com.example.bookmarket;

public class Book {

	private String bookName;
	private String author;
	private long bookId;
	private int cover;
	private String introduction;
	private String price;

	public Book() {
		super();
	}

	public Book(String bookName, String author, long bookId, int cover,
			String introduction, String price) {
		super();
		this.bookName = bookName;
		this.author = author;
		this.bookId = bookId;
		this.cover = cover;
		this.introduction = introduction;
		this.price = price;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return bookName;
	}

	public void setName(String name) {
		this.bookName = name;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public int getCover() {
		return cover;
	}

	public void setCover(int cover) {
		this.cover = cover;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Book [bookName=" + bookName + ", author=" + author
				+ ", bookId=" + bookId + ", cover=" + cover + ", introduction="
				+ introduction + ", price=" + price + "]";
	}

}
