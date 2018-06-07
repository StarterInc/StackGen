package io.starter.ignite.generator.react;

import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ReactEngine {
	private ScriptEngine se;

	// Constructor, sets up React and the Component
	public ReactEngine() throws Throwable {
		ScriptEngineManager sem = new ScriptEngineManager();
		se = sem.getEngineByName("nashorn");
		// React depends on the "global" variable
		se.eval("var global = this");
		// eval react.js
		String fdir = System.getProperty("user.dir");
		se.eval(new FileReader(fdir + "/WebContent/react/reactjs/node_modules/react/dist/react.js"));
		// This would also be an external JS file
		String component = "var MyComponent = React.createClass({" + "	render: function() {"
				+ "		return React.DOM.div(null, this.props.text)" + "	}" + "});";
		se.eval(component);
	}

	// Render the component, which can be called multiple times
	public void render(String text) throws Throwable {
		String render = "React.renderToString(React.createFactory(MyComponent)({" +
		// using JSONObject here would be cleaner obviosuly
				"	text: '" + text + "'" + "}))";
		io.starter.ignite.util.Logger.error(se.eval(render).toString());
	}

	public static void main(String... args) throws Throwable {
		ReactEngine test = new ReactEngine();
		test.render("Hello World");

		String fdir = System.getProperty("user.dir");
		// FileReader fir = new FileReader(fdir
		// + "/WebContent/react/reactjs/node_modules/react/dist/react.js");
		// String fx = new StringReader (fir).

	}
}
