package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * Created by Jeremy on 6/4/2018.
 */
@Entity
@Table(name="userQuestInstances")
public class QuestInstance extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int userQuestInstanceID;
	private int questUserID;
	@ManyToOne
	@JoinColumn(name="currentQuestStepID", updatable = false)
	private StoryQuestStep currentStep;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;
	private String questText;
	private String questTitle;
	private String questImage;
	private Integer randomQuestGiverRedeemer;
	private String randomQuestObjectivesJSONObject;

	public QuestInstance(int questUserID, StoryQuestStep currentStep, Date startedDate, Date completedDate, String questText, String questTitle, String questImage,
						 Integer randomQuestGiverRedeemer, String randomQuestObjectivesJSONObject) {
		this.questUserID = questUserID;
		this.currentStep = currentStep;
		this.startedDate = startedDate;
		this.completedDate = completedDate;
		this.questText = questText;
		this.questTitle = questTitle;
		this.questImage = questImage;
		this.randomQuestGiverRedeemer = randomQuestGiverRedeemer;
		this.randomQuestObjectivesJSONObject = randomQuestObjectivesJSONObject;
	}

	public QuestInstance(){	}

	public int getUserQuestInstanceID() {
		return userQuestInstanceID;
	}

	public void setUserQuestInstanceID(int userQuestInstanceID) {
		this.userQuestInstanceID = userQuestInstanceID;
	}

	public int getQuestUserID() {
		return questUserID;
	}

	public void setQuestUserID(int questUserID) {
		this.questUserID = questUserID;
	}

	public StoryQuestStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(StoryQuestStep currentStep) {
		this.currentStep = currentStep;
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getQuestText() {
		return questText;
	}

	public void setQuestText(String questText) {
		this.questText = questText;
	}

	public String getQuestTitle() {
		return questTitle;
	}

	public void setQuestTitle(String questTitle) {
		this.questTitle = questTitle;
	}

	public String getQuestImage() {
		return questImage;
	}

	public void setQuestImage(String questImage) {
		this.questImage = questImage;
	}

	public String getRandomQuestObjectivesJSONObject() {
		return randomQuestObjectivesJSONObject;
	}

	public void setRandomQuestObjectivesJSONObject(String randomQuestObjectivesJSONObject) {
		this.randomQuestObjectivesJSONObject = randomQuestObjectivesJSONObject;
	}

	public int getRandomQuestGiverRedeemer() {
		return randomQuestGiverRedeemer;
	}

	public void setRandomQuestGiverRedeemer (int randomQuestGiverRedeemer) {
		this.randomQuestGiverRedeemer = randomQuestGiverRedeemer;
	}


	@Entity
	@Table(name = "storyQuestStepConfigs")
	public static class StoryQuestStep extends DTO {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
		private int storyQuestStepID;

		@ManyToOne
		@JoinColumn(name = "rootStepID",  updatable = false)
		private StoryQuestStep rootStep;

		@ManyToOne
		@JoinColumn(name = "previousStepID",  updatable = false)
		private StoryQuestStep previousStep;

		@ManyToOne
		@JoinColumn(name = "nextStepID",  updatable = false)
		private StoryQuestStep nextStep;

		@ManyToOne
		@JoinColumn(name = "giverNPCID",  updatable = false)
		private NPC giverNPC;

		@ManyToOne
		@JoinColumn(name = "redeemerNPCID",  updatable = false)
		private NPC redeemerNPC;
		private String questTitle;
		private boolean canRandom;
		private boolean canVolunteer;
		private boolean isActive;
		private Integer timeLimitSeconds;
		@XmlTransient
		private String actionHandlerScript;

		public StoryQuestStep(StoryQuestStep previousStep, StoryQuestStep nextStep,StoryQuestStep rootStep, NPC giverNPC, String questTitle, boolean canRandom,
							  boolean canVolunteer, boolean isActive, Integer timeLimitSeconds, String actionHandlerScript) {
			this.previousStep = previousStep;
			this.nextStep = nextStep;
			this.rootStep = rootStep;
			this.giverNPC = giverNPC;
			this.questTitle = questTitle;
			this.canRandom = canRandom;
			this.canVolunteer = canVolunteer;
			this.isActive = isActive;
			this.timeLimitSeconds = timeLimitSeconds;
			this.actionHandlerScript = actionHandlerScript;
		}

		public StoryQuestStep() {}

		public int getStoryQuestStepID() {
			return storyQuestStepID;
		}

		public void setStoryQuestStepID(int storyQuestStepID) {
			this.storyQuestStepID = storyQuestStepID;
		}

		public StoryQuestStep getRootStep() {
			return rootStep;
		}

		public void setRootStepID(StoryQuestStep rootStepID) {
			this.rootStep = rootStepID;
		}

		public StoryQuestStep getPreviousStep() {
			return previousStep;
		}

		public void setPreviousStep(StoryQuestStep previousStepID) {
			this.previousStep = previousStepID;
		}

		public StoryQuestStep getNextStep() {
			return nextStep;
		}

		public void setNextStep(StoryQuestStep nextStep) {
			this.nextStep = nextStep;
		}

		public NPC getGiverNPC() {
			return giverNPC;
		}

		public void setGiverNPC(NPC giverNPC) {
			this.giverNPC = giverNPC;
		}

		public NPC getRedeemerNPC() {
			return redeemerNPC;
		}

		public void setRedeemerNPC(NPC redeemerNPC) {
			this.redeemerNPC = redeemerNPC;
		}

		public String getQuestTitle() {
			return questTitle;
		}

		public void setQuestTitle(String questTitle) {
			this.questTitle = questTitle;
		}

		public boolean isCanRandom() {
			return canRandom;
		}

		public void setCanRandom(boolean canRandom) {
			this.canRandom = canRandom;
		}

		public boolean isCanVolunteer() {
			return canVolunteer;
		}

		public void setCanVolunteer(boolean canVolunteer) {
			this.canVolunteer = canVolunteer;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setIsActive(boolean isActive) {
			this.isActive = isActive;
		}

		public Integer getTimeLimitSeconds() {
			return timeLimitSeconds;
		}

		public void setTimeLimitSeconds(Integer timeLimitSeconds) {
			this.timeLimitSeconds = timeLimitSeconds;
		}

		public String getActionHandlerScript() {
			return actionHandlerScript;
		}

		public void setActionHandlerScript(String actionHandlerScript) {
			this.actionHandlerScript = actionHandlerScript;
		}
	}
}
