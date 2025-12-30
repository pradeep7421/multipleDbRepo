package com.multipledb.transactional;

import com.multipledb.bookModel.Book;
import com.multipledb.userModel.Users;

public class OverallTransactionalModel {
	private Users user;
	private Book book;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public OverallTransactionalModel(Users user, Book book) {
		super();
		this.user = user;
		this.book = book;
	}
	public OverallTransactionalModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OverallTransactionalModel [user=");
		builder.append(user);
		builder.append(", book=");
		builder.append(book);
		builder.append("]");
		return builder.toString();
	}

}
