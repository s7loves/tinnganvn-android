package com.viettelcdc.tinngan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
	private transient static List<Category> categories = new ArrayList<Category>();
	
	public static List<Category> getAll() {
		return categories;
	}
	
	public static Category getById(int id) {
		for(Category category: categories) {
			if(id == category.id)
				return category;
		}
		return null;
	}
	
	public String name;
	public int id;
	
	private List<Article> articles = new ArrayList<Article>();
	
	public List<Article> getAllArticles() {
		return articles;
	}
}
