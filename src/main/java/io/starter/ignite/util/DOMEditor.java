package io.starter.ignite.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class DOMEditor {

	public static Document parse(String namespace, String filename)
			throws JDOMException, IOException {
		// final Namespace ns = Namespace.getNamespace(namespace);

		// Get the JDOM document
		org.jdom.Document doc = useSAXParser(filename);
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
	private static org.jdom.Document useSAXParser(String fileName)
			throws JDOMException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		return saxBuilder.build(new File(fileName));
	}

}