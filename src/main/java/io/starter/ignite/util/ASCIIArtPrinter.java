package io.starter.ignite.util;

public class ASCIIArtPrinter {
	
	private static final String[] prolines = {
			"	   _________ __                 __     ________                ____________________ ________    ",
			"	  /   _____//  |______    ____ |  | __/  _____/  ____   ____   \\______   \\______   \\\\_____  \\   ",
			"	  \\_____  \\\\   __\\__  \\ _/ ___\\|  |/ /   \\  ____/ __ \\ /    \\   |     ___/|       _/ /   |   \\  ",
			"   /        \\|  |  / __ \\\\  \\___|    <\\    \\_\\  \\  ___/|   |  \\  |    |    |    |   \\/    |    \\ ",
			"	 /_______  /|__| (____  /\\___  >__|_ \\\\______  /\\___  >___|  /  |____|    |____|_  /\\_______  / ",
			"	         \\/           \\/     \\/     \\/       \\/     \\/     \\/                    \\/         \\/  "			
	};
	
	private static final String[] lines = {
			"	   _________ __                 __     ________                			",
			"	  /   _____//  |______    ____ |  | __/  _____/  ____   ____   			",
			"	  \\_____  \\\\   __\\__  \\ _/ ___\\|  |/ /   \\  ____/ __ \\ /    \\  ",
			"     	  /        \\|  |  / __ \\\\  \\___|    <\\    \\_\\  \\  ___/|   |  \\ 	",
			"	 /_______  /|__| (____  /\\___  >__|_ \\\\______  /\\___  >___|  / 		",
			"	         \\/           \\/     \\/     \\/       \\/     \\/     \\/  	"
	};
	
	public static void main(String[] args) {
		System.out.print(ASCIIArtPrinter.print());
	}

	public static String print(){
		return printLines(lines);
	}

	public static String printLines(String[] lin) {
		StringBuffer ret = new StringBuffer();

		ret.append(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		
		for(String ln : lin) {
			ret.append("\r\n");
			ret.append(ln);
		}
		ret.append("\r\n");
		ret.append("\r\n");
		ret.append("::::::::::::::::: Starter StackGen (v."
				+ SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION
				+ ") - https://stackgen.io :::::::::::::::::");
		return ret.toString();
	}
}
