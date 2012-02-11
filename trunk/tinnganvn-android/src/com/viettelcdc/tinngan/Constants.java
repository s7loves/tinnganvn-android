package com.viettelcdc.tinngan;

public interface Constants {
	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String WSDL = "http://viettelvas.vn/tinnganAPIAPP/tinngan.asmx?WSDL";
	public static final String GET_BY_CATEGORY = "getNewsInCategory";
	public static final String GET_ALL_CATEGORIES = "getListAllCategories";
	public static final String DO_SEARCH = "getSearchNews ";
	public static final String USERNAME_PARAM = "user";
	public static final String PASSWORD_PARAM = "password";
	public static final String SEARCH_TEXT_PARAM = "text";
	public static final String CID_PARAM = "cid";
	public static final String START_PARAM = "start";
	public static final String END_PARAM = "end";
	public static final String USERNAME = "tinngan";
	public static final String PASSWORD = "tinngan123$%^";
	public static final int LOAD_LIMIT = 10;
	public static final String BASE_URl = "http://tinngan.vn";
	
	static interface ArticleJsonField {
		static String ID = "ID";
		static String TITLE = "Title";
		static String PID = "PID";
		static String LEAD = "Lead";
		static String IMAGE = "Image";
		static String CONTENT = "Content";
		static String DATE_PUB = "DatePub";
	}
}
