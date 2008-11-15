package main;

public class Console {
	private static boolean DEBUG = true;
	
	public static void debug(String text) {
		if(DEBUG) System.out.println(text);
	}
	
	public static void info(String text) {
		System.out.println(text);
	}
}
