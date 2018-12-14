package io.starter.ignite.generator.react;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert JSX in the pages into JavaScript using the JSXCompiler from Facebook
 * 
 * http://www.oracle.com/technetwork/java/filters-137243.html
 * 
 * @author John McMahon (@TechnoCharms)
 * 
 */
public final class JSXTransformationFilter implements Filter {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(JSXTransformationFilter.class);

	private FilterConfig			filterConfig	= null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	private static JSXTransformer transformer;

	// -------------------------- STATIC METHODS
	// --------------------------

	public static String transform(String jsx) {
		return transformer.transform("/**@jsx React.DOM */" + jsx);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (filterConfig == null)
			return;

		// do the tranformation
		transformHTML((HttpServletRequest) request, response, chain);

		/**
		 * TODO: fix session instantiation timing issue
		 * 
		 * try {
		 * SecuritySubSystem.logAndCheckAccess((HttpServletRequest)request,
		 * (HttpServletResponse)response); } catch (Exception e) { // TODO
		 * Auto-generated catch block throw new ServletException(e); }
		 */
		// chain.doFilter(request, response);
	}

	/**
	 * rewrite input text JSX to valid React code
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void transformHTML(HttpServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String contentType;

		CharResponseWrapper responseWrapper = new CharResponseWrapper(
				(HttpServletResponse) response);
		String requ = request.getRequestURI();

		chain.doFilter(request, responseWrapper);

		// Get response from servlet
		String inputText = responseWrapper.toString();

		if (!response.isCommitted()) {
			PrintWriter out = response.getWriter();

			try {

				if (transformer == null) {
					JSXTransformer instance = new JSXTransformer();
					try {

						instance.setModulePaths(Arrays.asList("public"));
						instance.setJsxTransformerJS("/io/starter/reactjs/JSXTransformer-0.13.3.js");

						instance.init();
						transformer = instance;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// OK here we extract any JSX tags -- important not to try
				// to
				// compile non JSX code
				// type="text/jsx"
				int pos = inputText.toLowerCase().indexOf("text/jsx");

				if (pos == -1) { // JSX not embedded in HTML. Look for a JS tag
					if (requ.contains(".js")) {
						logger.warn("Transforming JavaScript File: " + requ);
						inputText = transformer.transform(inputText);
					} else {
						logger.warn("No JSX contained in Parsed File: " + requ);
					}
				} else {
					while (pos > 0) { // iterate
						inputText = extractJSX(inputText, pos);
						pos = inputText.toLowerCase().indexOf("text/jsx");
					}
				}

				response.setContentLength(inputText.length());

				out.write(inputText);

				// Log the resulting string
				out.flush();

			} catch (Exception ex) {
				out.println(ex.toString());
			}
		}
	}

	/**
	 * grab JSX code between script tags -- assumes a well-formed jsx tag:
	 * 
	 * <code>
	 *  <script type="test/jsx">
	 * </code>
	 * 
	 * @param extract
	 * @param pos
	 * @return
	 */
	private String extractJSX(String extract, int pos) {
		String extractedPre = "", extractedPost = "";

		String snippet = snip(extract, pos + 10);

		String js = transformer.transform(snippet);

		extractedPre = extract.substring(0, pos);

		extractedPost = extract.substring(pos + snippet.length() + 10);

		extractedPre += "text/javascript\">";
		extractedPre += js;

		logger.debug("js = " + js);

		return extractedPre + extractedPost;
	}

	// return a snip
	private String snip(String in, int pos) {
		int endJSX = in.toLowerCase().indexOf("</script>", pos); // the next
																	// index of
																	// end
																	// script
																	// after JSX
																	// start
		String ret = in.substring(pos, endJSX);
		return ret;
	}

}

/**
 * from http://www.java2s.com/Tutorial/Java/
 * 0400__Servlet/Filterthatusesaresponsewrappertoconvertalloutputtouppercase.htm
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
class CharResponseWrapper extends HttpServletResponseWrapper {

	protected static final Logger	logger	= LoggerFactory
			.getLogger(CharResponseWrapper.class);

	protected CharArrayWriter		charWriter;

	protected PrintWriter			writer;

	protected boolean				getOutputStreamCalled;

	protected boolean				getWriterCalled;

	/**
	 * reads from a file in the app deployment
	 * 
	 * @param rsc
	 * @return
	 */
	private static final String getResource(String rsc) {
		String val = "";

		try {
			Class cls = Class.forName("ClassLoaderDemo");

			// returns the ClassLoader object associated with this Class
			ClassLoader cLoader = cls.getClassLoader();
			// input stream
			InputStream i = cLoader.getResourceAsStream(rsc);
			BufferedReader r = new BufferedReader(new InputStreamReader(i));

			// reads each line
			String l;
			while ((l = r.readLine()) != null) {
				val = val + l;
			}
			i.close();
		} catch (Exception e) {
			logger.error("Failed to get resource: " + e);
		}
		return val;
	}

	public CharResponseWrapper(HttpServletResponse response) {
		super(response);

		charWriter = new CharArrayWriter();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (getWriterCalled) {
			throw new IllegalStateException("getWriter already called");
		}

		getOutputStreamCalled = true;
		return super.getOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}
		if (getOutputStreamCalled) {
			throw new IllegalStateException("getOutputStream already called");
		}
		getWriterCalled = true;
		writer = new PrintWriter(charWriter);
		return writer;
	}

	@Override
	public String toString() {
		String s = null;

		if (writer != null) {
			s = charWriter.toString();
		}
		return s;
	}
}