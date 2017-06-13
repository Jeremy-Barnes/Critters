package com.critters.dal.dto;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created by Jeremy on 6/6/2017.
 */
public class DTO {
	public Class type;

	public DTO() {
		type = this.getClass();
	}

	public String toString(){
		try {
			JAXBContext jc = JAXBContext.newInstance(type);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty("eclipselink.media-type", "application/json");
			marshaller.setProperty("eclipselink.json.include-root", true);
			StringWriter sw = new StringWriter();
			marshaller.marshal(this, sw);
			return sw.toString();
		} catch (Exception e) {
			return "failed to serialize!";
		}
	}
}
