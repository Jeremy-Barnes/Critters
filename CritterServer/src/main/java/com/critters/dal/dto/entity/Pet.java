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
	private String sex;
	private int ownerID;
	private boolean isAbandoned;



	@ManyToOne
	@JoinColumn(name="colorid", updatable = false)
	private PetColor petColor;
	@ManyToOne
	@JoinColumn(name="speciesid", updatable = false)
	private PetSpecies petSpecies;


	public Pet() {}

	public Pet(String petName, String sex, int ownerID, boolean isAbandoned) {
		this.petName = petName;
		this.sex = sex;
		this.ownerID = ownerID;
		this.isAbandoned = isAbandoned;
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

	public boolean getIsAbandoned() {
		return isAbandoned;
	}

	public void setIsAbandoned(boolean isAbandoned) {
		this.isAbandoned = isAbandoned;
	}

	@Entity
	@Table(name = "petcolorconfigs")
	public static class PetColor {
		@Id
		int petColorConfigID;
		String petColorName;
		String patternPath;

		public PetColor(){}

		public PetColor(int petColorConfigID, String petColorName, String patternPath) {
			this.petColorConfigID = petColorConfigID;
			this.petColorName = petColorName;
			this.patternPath = patternPath;
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
		
		public String getPatternPath() {
			return patternPath;
		}

		public void setPatternPath(String patternPath) {
			this.patternPath = patternPath;
		}
	}

	@Entity
	@Table(name = "petspeciesconfigs")
	public static class PetSpecies {
		@Id
		int petSpeciesConfigID;
		String petTypeName;
		String imagePathWithoutModifiers;
		String speciesDescription;


		public PetSpecies(){}

		public PetSpecies(int petSpeciesConfigID, String petTypeName, String imagePathWithoutModifiers, String speciesDescription) {
			this.petSpeciesConfigID = petSpeciesConfigID;
			this.petTypeName = petTypeName;
			this.speciesDescription = speciesDescription;
			this.imagePathWithoutModifiers = imagePathWithoutModifiers;
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
		
		public String getImagePathWithoutModifiers() {
			return imagePathWithoutModifiers;
		}

		public void setImagePathWithoutModifiers(String imagePathWithoutModifiers) {
			this.imagePathWithoutModifiers = imagePathWithoutModifiers;
		}
		
		public String getSpeciesDescription() {
			return speciesDescription;
		}

		public void setSpeciesDescription(String speciesDescription) {
			this.speciesDescription = speciesDescription;
		}
	}
}
