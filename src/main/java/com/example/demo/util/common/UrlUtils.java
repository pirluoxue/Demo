package com.example.demo.util.common;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlUtils {

	public static String DEFAULT_FORMAT = "utf-8";
	
	public static String encode(String input)
	{
		try {
			return URLEncoder.encode(input, DEFAULT_FORMAT);
		} catch (Exception e) {
			return input;
		}
	}
	
	public static String decode(String input)
	{
		try {
			return URLDecoder.decode(input, DEFAULT_FORMAT);
		} catch (Exception e) {
			return input;
		}
	}
	
}
