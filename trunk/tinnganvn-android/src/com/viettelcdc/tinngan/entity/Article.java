package com.viettelcdc.tinngan.entity;

import java.util.Date;

public class Article {
	public String title;
	public Date date;
	public String lead;
	public String image;
	public String content;
	public Type type;
	
	public static enum Type {
		TEXT, AUDIO, VIDEO
	}
}
