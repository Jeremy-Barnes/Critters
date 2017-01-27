package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jeremy on 8/23/2016.
 */
@Entity
@Table(name="pets")
public class Pet {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int petID;
	private String petName;
<<<<<<< HEAD
	private Boolean sex;
	private int ownerid;
=======
	private String sex;
	private int ownerID;
	private boolean isAbandoned;


>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	@ManyToOne
	@JoinColumn(name="colorid", updatable = false)
	private PetColor petColor;
	@ManyToOne
	@JoinColumn(name="speciesid", updatable = false)
	private PetSpecies petSpecies;


	public Pet() {}

<<<<<<< HEAD
	public Pet(String petName, boolean sex, int ownerid) {
		this.petName = petName;
		this.sex = sex;
		this.ownerid = ownerid;
=======
	public Pet(String petName, String sex, int ownerID, boolean isAbandoned) {
		this.petName = petName;
		this.sex = sex;
		this.ownerID = ownerID;
		this.isAbandoned = isAbandoned;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

	public int getPetID() {
		return petID;
	}

	public void setPetID(int petID) {
		this.petID = petID;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getSex() {
<<<<<<< HEAD
		return sex ? "Male" : sex == null ? "Other" : "Female";
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
=======
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerid) {
		this.ownerID = ownerid;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

	public PetColor getPetColor() {
		return petColor;
	}

	public void setPetColor(PetColor petColor) {
		this.petColor = petColor;
	}

	public PetSpecies getPetSpecies() {
		return petSpecies;
	}

	public void setPetSpecies(PetSpecies petSpecies) {
		this.petSpecies = petSpecies;
	}

<<<<<<< HEAD
=======
	public boolean getIsAbandoned() {
		return isAbandoned;
	}

	public void setIsAbandoned(boolean isAbandoned) {
		this.isAbandoned = isAbandoned;
	}

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	@Entity
	@Table(name = "petcolorconfigs")
	public static class PetColor {
		@Id
		int petColorConfigID;
		String petColorName;

		public PetColor(){}

		public PetColor(int petColorConfigID, String petColorName) {
			this.petColorConfigID = petColorConfigID;
			this.petColorName = petColorName;
		}

		public int getPetColorConfigID() {
			return petColorConfigID;
		}

		public void setPetColorConfigID(int petColorConfigID) {
			this.petColorConfigID = petColorConfigID;
		}

		public String getPetColorName() {
			return petColorName;
		}

		public void setPetColorName(String petColorName) {
			this.petColorName = petColorName;
		}
	}

	@Entity
	@Table(name = "petspeciesconfigs")
	public static class PetSpecies {
		@Id
		int petSpeciesConfigID;
		String petTypeName;

		public PetSpecies(){}

		public PetSpecies(int petSpeciesConfigID, String petTypeName) {
			this.petSpeciesConfigID = petSpeciesConfigID;
			this.petTypeName = petTypeName;
		}

		public int getPetSpeciesConfigID() {
			return petSpeciesConfigID;
		}

		public void setPetSpeciesConfigID(int petSpeciesConfigID) {
			this.petSpeciesConfigID = petSpeciesConfigID;
		}

		public String getPetTypeName() {
			return petTypeName;
		}

		public void setPetTypeName(String petTypeName) {
			this.petTypeName = petTypeName;
		}
	}
}
