package io.starter.ignite.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.generator.Main;

public class IgniteServlet implements Servlet, Configuration {

	private String					message;
	protected static final Logger	logger			= LoggerFactory
			.getLogger(IgniteServlet.class);

	String							inputSpecFile	= "trade_automator.yml",

			pluginSpecFile1 = PLUGIN_SPEC_LOCATION + "ignite/eStore.yml",

			pluginSpecFile2 = PLUGIN_SPEC_LOCATION + "location_services.yml";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {

		// Do required initialization
		message = "Starter Ignite Generator";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + message + "</h1>");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		try {
			out.write("<h1>" + Main.generateEncryptionKey("yo") + "</h1>");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();

		// run that junk
		swaggerPluginMerge();
	}

	public void swaggerPluginMerge() {
		Main.generateApp(inputSpecFile);
	}

}
