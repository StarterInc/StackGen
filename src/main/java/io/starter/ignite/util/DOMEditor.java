package io.starter.ignite.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DOMEditor {

	public static Document parse(String namespace, String filename)
			throws JDOMException, IOException {
		// Get the JDOM document
		org.jdom2.Document doc = useSAXParser(filename);
		return doc;
	}

	public static void write(Document doc, String output)
			throws IOException {
		// document is processed and edited successfully, lets save it in new
		// file
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		// output xml to console for debugging
		// xmlOutputter.output(doc, System.out);
		xmlOutputter.output(doc, new FileOutputStream(output));
	}

	// Get JDOM document from SAX Parser
	private static org.jdom2.Document useSAXParser(String fileName)
			throws JDOMException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		return saxBuilder.build(new File(fileName));
	}
}