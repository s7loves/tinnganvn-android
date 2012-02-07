package com.viettelcdc.tinngan.entity;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
	public int pid;
	public int id;
	public String title;
	public Date date;
	public String lead;
	public String image;
	public String content;
}
