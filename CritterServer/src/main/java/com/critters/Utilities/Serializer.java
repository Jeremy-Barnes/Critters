package com.critters.Utilities;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class Serializer {

	public static String toJSON(boolean prettyPrint, Object object, Class type){
		try {
			JAXBContext jc = JAXBContext.newInstance(type);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
			marshaller.setProperty("eclipselink.media-type", "application/json");
			marshaller.setProperty("eclipselink.json.include-root", true);
			StringWriter sw = new StringWriter();
			marshaller.marshal(object, sw);
			return sw.toString();
		} catch(Exception e){
			return "failed to serialize!" + e.getMessage();
		}
	}

	public static <T> T fromJSON(String json, Class objType){
		try {
			JAXBContext jc = JAXBContext.newInstance(objType);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty("eclipselink.json.include-root", false);
			unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
			T copiedObject = (T) unmarshaller.unmarshal(new StreamSource(new StringReader(json)), objType).getValue();
			return copiedObject;
		} catch(JAXBException jaxb) {
			return null;
		}
	}

	public static JSONObject dictionaryFromJSON(String json){
		return new JSONObject(json);
	}
}
