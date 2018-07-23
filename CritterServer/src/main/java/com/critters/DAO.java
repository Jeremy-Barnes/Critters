package com.critters;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Jeremy on 6/6/2017.
 */
public abstract class DAO extends DTO {

	@XmlTransient
	public abstract Integer getID();
	public abstract void setID();

}
