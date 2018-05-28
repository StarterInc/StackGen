package io.starter.ignite.generator.react;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

/**
 *
 * Depends on Mozilla Rhino. Does not work with the JRE built-in version
 * 
 * <dependency> <groupId>org.mozilla</groupId> <artifactId>rhino</artifactId>
 * <version>1.7R4</version> </dependency>
 */
public class JSXTransformer {
	// ------------------------------ FIELDS ------------------------------

	private List<String> modulePaths;
	private String jsxTransformerJS;
	private Context ctx;
	private Scriptable exports;
	private Scriptable topLevelScope;
	private Function transform;

	// --------------------------- main() method ---------------------------

	public static void main(String args[]) throws URISyntaxException {
		JSXTransformer jsxTransformer = new JSXTransformer();
		// Using the CDN does not work
		// jsxTransformer.setModulePaths(Arrays.asList("http://fb.me/"));
		jsxTransformer.setModulePaths(Arrays.asList("public"));
		jsxTransformer
				.setJsxTransformerJS("${MYBATIS_MAIN}main/java/io/starter/reactjs/JSXTransformer-0.13.3.js");
		jsxTransformer.init();
		String js = jsxTransformer
				.transform("/** @jsx React.DOM */ React.renderComponent(<h1>Hello, world!</h1>,document.getElementById('example'));");
		System.out.println("js = " + js);
	}

	public void setModulePaths(List<String> modulePaths) {
		this.modulePaths = modulePaths;
	}

	public void setJsxTransformerJS(String jsxTransformerJS) {
		this.jsxTransformerJS = jsxTransformerJS;
	}

	public void init() throws URISyntaxException {
		ctx = Context.enter();
		try {
			RequireBuilder builder = new RequireBuilder();
			builder.setModuleScriptProvider(new SoftCachingModuleScriptProvider(
					new UrlModuleSourceProvider(buildModulePaths(), null)));

			topLevelScope = ctx.initStandardObjects();
			builder.setSandboxed(false); // allows this to load scripts
			Require require = builder.createRequire(ctx, topLevelScope);

			exports = require.requireMain(ctx, jsxTransformerJS);
			transform = (Function) exports.get("transform", topLevelScope);
		} finally {
			Context.exit();
		}
	}

	// mostly copied from
	// org.mozilla.javascript.tools.shell.Global.installRequire()
	private List<URI> buildModulePaths() throws URISyntaxException {

		if (modulePaths == null) {
			return null;
		}
		List<URI> uris = new ArrayList<URI>(modulePaths.size());
		for (String path : modulePaths) {
			try {
				URI uri = new URI(path);
				if (!uri.isAbsolute()) {
					// call resolve("") to canonify the path
					uri = new File(path).toURI().resolve("");
				}
				if (!uri.toString().endsWith("/")) {
					// make sure URI always terminates with slash to
					// avoid loading from unintended locations
					uri = new URI(uri + "/");
				}
				uris.add(uri);
			} catch (URISyntaxException usx) {
				throw new RuntimeException(usx);
			}
		}
		return uris;
	}

	public String transform(String jsx) {
		Context.enter();
		try {
			NativeObject result = (NativeObject) transform.call(ctx,
					topLevelScope, exports, new String[] { jsx });
			return result.get("code").toString();
		} finally {
			Context.exit();
		}
	}
}