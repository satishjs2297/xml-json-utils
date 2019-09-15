package com.xml.json.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmlUtilHandler {
	private static final Logger LOG = LogManager.getLogger(XmlUtilHandler.class);

	public static void main(String[] args) throws IOException, TransformerException {

		final String INPUT_XSL = "style.xsl";
		final String OUTPUT_FILE = "output-results.json";
		if (args.length == 0) {
			throw new IllegalStateException("Pass xml file path or xml Content");
		}
		String xmlContent = null;
		String inputParam = args[0];
		if (new File(inputParam).isFile()) {
			xmlContent = getXmlContent(inputParam);
		} else {
			xmlContent = inputParam;
		}
		LOG.debug("**************** Input Original XML Content ****************");
		LOG.debug(xmlContent);

		XmlUtilHandler xmlUtilHanderl = new XmlUtilHandler();
		xmlContent = XmlUtils.tranformXmlWithXsltConfig(xmlUtilHanderl.getFileFromResources(INPUT_XSL), xmlContent);
		LOG.debug("**************** Output Transformed XML Content ****************");
		LOG.debug(xmlContent);

		String jsonContent = XmlUtils.convertToJson(xmlContent);
		LOG.info("**************** OutPut Json Content **************");
		LOG.info(jsonContent);

		try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE);) {
			outputStream.write(jsonContent.getBytes());
			outputStream.flush();
		}
		LOG.info("**************** Json file was successfully generated. **************");

	}

	private static String getXmlContent(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> {
				contentBuilder.append(s).append("\n");
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		return contentBuilder.toString();
	}

	private InputStream getFileFromResources(String fileName) {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}
}
