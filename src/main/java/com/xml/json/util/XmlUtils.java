package com.xml.json.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;
import org.json.XML;

public class XmlUtils {

	public static int PRETTY_PRINT_INDENT_FACTOR = 4;

	private XmlUtils() {
		throw new IllegalStateException("Should not create object for XmlUtils class");
	}

	public static String convertToJson(String xmlString) {
		String jsonString = null;
		JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
		jsonString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		return jsonString;
	}

	public static String tranformXmlWithXsltConfig(InputStream xsltFileStream, String originXmlContent)
			throws FileNotFoundException, TransformerException {
		ByteArrayInputStream bIs = new ByteArrayInputStream(originXmlContent.getBytes());
		ByteArrayOutputStream bOs = new ByteArrayOutputStream(1023);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		// load xsl in memory
		Transformer transformer = tFactory.newTransformer(new StreamSource(xsltFileStream));

		// transform xml file
		transformer.transform(new StreamSource(bIs), new StreamResult(bOs));

		return new String(bOs.toByteArray(), StandardCharsets.UTF_8);

	}
}
