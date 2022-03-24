package io.starter.ignite.util;

public class ASCIIArtPrinter {
	
	private static String[] lines = {
			"   ______           __   _____       \n" +
					"  / __/ /____ _____/ /__/ ______ ___ \n" +
					" _\\ \\/ __/ _ `/ __/  '_/ (_ / -_/ _ \\\n" +
					"/___/\\__/\\_,_/\\__/_/\\_\\\\___/\\__/_//_/\n" +
					"                                     "
	};
	
	private static String[] prolines = {
			" ______  ______ ______  ______  __  __  ______  ______  __   __       ______ ______  ______    \n" +
					"/\\  ___\\/\\__  _/\\  __ \\/\\  ___\\/\\ \\/ / /\\  ___\\/\\  ___\\/\\ \"-.\\ \\     /\\  == /\\  == \\/\\  __ \\   \n" +
					"\\ \\___  \\/_/\\ \\\\ \\  __ \\ \\ \\___\\ \\  _\"-\\ \\ \\__ \\ \\  __\\\\ \\ \\-.  \\    \\ \\  _-\\ \\  __<\\ \\ \\/\\ \\  \n" +
					" \\/\\_____\\ \\ \\_\\\\ \\_\\ \\_\\ \\_____\\ \\_\\ \\_\\ \\_____\\ \\_____\\ \\_\\\\\"\\_\\    \\ \\_\\  \\ \\_\\ \\_\\ \\_____\\ \n" +
					"  \\/_____/  \\/_/ \\/_/\\/_/\\/_____/\\/_/\\/_/\\/_____/\\/_____/\\/_/ \\/_/     \\/_/   \\/_/ /_/\\/_____/ \n" +
					"                                                                                               "
	};
	
	public static void main(String[] args) {
		System.out.print(ASCIIArtPrinter.print());
	}

	public static String print(){
		return printLines(lines);
	}

	public static String printLines(String[] lin) {
		StringBuffer ret = new StringBuffer();
		ret.append("\r\n");
		ret.append(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		for(String ln : lin) {
			ret.append("\r\n");
			ret.append(ln);
		}
		ret.append("\r\n");
		ret.append(":::::::::::::::::::::: Starter StackGen (v."
				+ SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION
				+ ") - https://stackgen.io ::::::::::::::::::::::");
		ret.append("\r\n");
		ret.append("\r\n");
		return ret.toString();
	}
}
