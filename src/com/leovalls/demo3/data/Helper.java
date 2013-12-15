package com.leovalls.demo3.data;

public class Helper {
	public static final String INSTGRAM_API_KEY = "d1fbe779a72546cabd9944ab2da42817";
	public static final String BASE_API_URL = "https://api.instagram.com/v1";
	
	public static String getRecentMediaUrl(String tag){
		return BASE_API_URL + "/tags/" + tag + "/media/recent?client_id=" + INSTGRAM_API_KEY ;
	}
}
