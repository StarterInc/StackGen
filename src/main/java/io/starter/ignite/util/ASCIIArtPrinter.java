package io.starter.ignite.util;

public class ASCIIArtPrinter {

	private static String ln1 = "  _________ __                 __                 .___              .__  __          ";
	private static String ln2 = " /   _____//  |______ ________/  |_  ___________  |   | ____   ____ |__|/  |_  ____  ";
	private static String ln3 = " \\_____  \\\\   __\\__  \\\\_  __ \\   __\\/ __ \\_  __ \\ |   |/ ___\\ /    \\|  \\   __\\/ __ \\ ";
	private static String ln4 = " /        \\|  |  / __ \\|  | \\/|  | \\  ___/|  | \\/ |   / /_/  >   |  \\  ||  | \\  ___/ ";
	private static String ln5 = "/_______  /|__| (____  /__|   |__|  \\___  >__|    |___\\___  /|___|  /__||__|  \\___  >  ";
	private static String ln6 = "        \\/           \\/                 \\/           /_____/      \\/              \\/ ";

	public static void main(String[] args) {
		System.out.print(ASCIIArtPrinter.print());
	}

	public static String print() {
		StringBuffer ret = new StringBuffer();

		ret.append(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		ret.append("Configuration.LINEFEED");
		ret.append(ln1);
		ret.append("Configuration.LINEFEED");
		ret.append(ln2);
		ret.append("Configuration.LINEFEED");
		ret.append(ln3);
		ret.append("Configuration.LINEFEED");
		ret.append(ln4);
		ret.append("Configuration.LINEFEED");
		ret.append(ln5);
		ret.append("Configuration.LINEFEED");
		ret.append(ln6);
		ret.append("Configuration.LINEFEED");

		ret.append("::::::::::::::::: Starter Ignite (v." + SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION + ") - http://starter.io/ignite :::::::::::::::::");
		return ret.toString();
	}
}
