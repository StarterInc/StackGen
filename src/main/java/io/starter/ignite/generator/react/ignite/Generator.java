/**
 * this file licensed under Apache-style FOSS license
 * 
 * Copyright 2016, Starter Inc.
 */
package io.starter.ignite.generator.react.ignite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import io.starter.OpenXLS.CellRange;
import io.starter.OpenXLS.NameHandle;
import io.starter.OpenXLS.WorkBookHandle;
import io.starter.OpenXLS.WorkSheetHandle;
import io.starter.toolkit.StringTool;

/**
 * ## EXPERIMENTAL FEATURE ##
 * 
 * Generate React Ignite
 * 
 * @author john
 */
public class Generator {

	public static String indent = "    ";

	public static String componentInitCodeNative = "export default function () {\n" + indent + "return (\n" + indent
			+ indent + "${content}" + indent + ");" + "\n}";

	public static String styleInitCodeNative = "var styles = StyleSheet.create({\n" + indent + "${styles}" + "});";

	public static String componentInitCodeWeb = "/** @jsx React.DOM */ React.renderComponent(" + indent + indent
			+ "${component.content}" + ");";

	private static Map<String, String> styleMap = new HashMap<String, String>();
	private static Map<String, String> componentMapNative = new HashMap<String, String>();
	private static Map<String, String> componentMapWeb = new HashMap<String, String>();

	// the app-specfic mappings
	private static Map<String, String> appComponentMap = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {

		String appWorkBookPath = System.getProperty("user.dir") + "/src/main/resources/templates/IgniteReact.xlsx";
		WorkBookHandle appWorkBook = new WorkBookHandle(appWorkBookPath);
		Generator.initFromSpreadsheet(appWorkBook);
	}

	/**
	 * read the generation config from a Spreadsheet
	 * 
	 * @param book
	 * @throws Exception
	 */
	public static void initFromSpreadsheet(WorkBookHandle book) throws Exception {

		// iterate the sheets
		WorkSheetHandle[] sheets = book.getWorkSheets();

		for (WorkSheetHandle sheet : sheets) {

			// each sheet corresponds to a screen in the app
			NameHandle components = sheet.getNamedRangeInScope("components");
			CellRange[] componentdef = components.getCellRanges();

			// each screen has components defined top-to-bottom
			for (CellRange component : componentdef) {
				System.out.print(component);

				appComponentMap.put("name", "view");
			}

			// each sheet corresponds to a style in the app
			NameHandle styles = sheet.getNamedRangeInScope("styles");
			CellRange[] styledef = styles.getCellRanges();

			// each screen has components defined top-to-bottom
			for (CellRange style : styledef) {
				System.out.print(style);

				styleMap.put("color", "'#222222'");
				styleMap.put("backgroundColor", "'#FF9900'");

			}

			// execute logic from formulas to determine what is shown

			//

		}

	}

	public JSONObject getJSON() {
		JSONObject dtx = new JSONObject();

		dtx.put("name", "Object 2");
		dtx.put("description", "Test Object Number Two");

		// set a url
		String urx = "/table_view.jsp?tablename=User&query=id:";
		dtx.put("url", urx);
		return dtx;
	}

	/**
	 * generate jsx from a JSON object
	 */
	public void generateReactJSXFromJSON() {

		// TODO: init map from spreadsheet
		initMaps();

		JSONObject dtx = getJSON();
		String jsx = getJSXFromJSON(dtx);
		io.starter.ignite.util.Logger.error(jsx);
	}

	/**
	 * generate jsx from a JSON object
	 */
	public void generateReactNativeJSXFromJSON() {
		// TODO: init map from spreadsheet
		initMaps();
		JSONObject dtx = getJSON();
		String jsx = getJSXFromJSON(dtx);
		io.starter.ignite.util.Logger.error(jsx);
	}

	/**
	 * TODO: init from spreadsheet
	 */
	public static void initMaps() {
		styleMap.put("color", "'#222222'");
		styleMap.put("backgroundColor", "'#FF9900'");

		componentMapNative.put("view", "<View style=${styles.component}>${content.component}</View>");

		componentMapWeb.put("view", "<Div style='${styles.component}'>${content.component}</Div>");

		appComponentMap.put("name", "view");
		appComponentMap.put("description", "view");
		appComponentMap.put("url", "view");
	}

	public static String getJSXFromJSON(JSONObject job) {
		StringBuffer sb = new StringBuffer();

		sb.append("// component jsx");

		sb.append(indent);
		sb.append("\n");

		String componentName = formatString(job.get("name").toString());
		Iterator<String> names = job.keys();
		while (names.hasNext()) {
			// lookup the component mapping
			String name = names.next();
			String compName = appComponentMap.get(name);
			String componentJSX = componentMapNative.get(compName);
			if (componentJSX != null) {
				sb.append(indent);
				sb.append(indent);
				// look up the component and style for the object
				componentJSX = StringTool.replaceText(componentJSX, "${styles.component}",
						"{styles." + componentName + "}");

				sb.append(componentJSX);
				sb.append("\n");
			}
		}

		// create the components
		String componentString = sb.toString();

		// replace ${content}
		String componentCode = StringTool.replaceText(componentInitCodeNative, "${content}", componentString);

		sb = new StringBuffer();

		sb.append("// component styles");
		sb.append("\n");
		// create the styles

		sb.append(indent);
		sb.append(componentName + ": {");
		sb.append("\n");
		names = job.keys();
		while (names.hasNext()) {
			String name = names.next();

			String styleName = styleMap.get(name);
			if (styleName != null) {
				sb.append(indent);
				sb.append(indent);
				sb.append(name);
				sb.append(":");
				sb.append(styleName);
				sb.append(",");
				sb.append("\n");
			}
		}
		sb.append(indent);
		sb.append("},"); // end component styles
		sb.append("\n");

		String styleString = sb.toString();

		// replace ${styles}
		String styleCode = StringTool.replaceText(styleInitCodeNative, "${styles}", styleString);

		sb = new StringBuffer(componentCode);
		sb.append("\n");
		sb.append(styleCode);
		sb.append("\n");
		return sb.toString();
	}

	public static String formatString(String input) {
		input = input.toLowerCase();
		input = StringTool.replaceChars(" ", input, "_");
		return input;
	}
}
