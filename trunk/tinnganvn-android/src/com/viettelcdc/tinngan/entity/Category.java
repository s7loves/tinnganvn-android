package com.viettelcdc.tinngan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
	public String name;
	public int id;
	
	private List<Article> articles = new ArrayList<Article>();
	
	public List<Article> getAllArticles() {
		return articles;
	}
}
