package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * Created by Jeremy on 6/4/2018.
 */
public class Quest extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int userQuestInstanceID;
	private int questUserID;
	@ManyToOne
	@JoinColumn(name="currentQuestStepID", updatable = false)
	private QuestStep currentStep;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;

	public Quest(int questUserID, QuestStep currentStep, Date startedDate, Date completedDate) {
		this.questUserID = questUserID;
		this.currentStep = currentStep;
		this.startedDate = startedDate;
		this.completedDate = completedDate;
	}

	public Quest(){	}

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

	public QuestStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(QuestStep currentStep) {
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

	@Entity
	@Table(name = "questStepConfigs")
	public static class QuestStep extends DTO {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
		private int questStepID;

		@ManyToOne
		@JoinColumn(name = "previousStepID",  updatable = false)
		private QuestStep previousStepID;

		@ManyToOne
		@JoinColumn(name = "nextStepID",  updatable = false)
		private QuestStep nextStepID;

		@ManyToOne
		@JoinColumn(name = "giverNPC",  updatable = false)
		private NPC giverNPC;

		private boolean canRandom;
		private boolean canVolunteer;
		private boolean isActive;
		private Integer timeLimitSeconds;
		@XmlTransient
		private String actionHandlerScript;

		public QuestStep(QuestStep previousStepID, QuestStep nextStepID, NPC giverNPC, boolean canRandom, boolean canVolunteer, boolean isActive, Integer timeLimitSeconds, String actionHandlerScript) {
			this.previousStepID = previousStepID;
			this.nextStepID = nextStepID;
			this.giverNPC = giverNPC;
			this.canRandom = canRandom;
			this.canVolunteer = canVolunteer;
			this.isActive = isActive;
			this.timeLimitSeconds = timeLimitSeconds;
			this.actionHandlerScript = actionHandlerScript;
		}

		public QuestStep() {}

		public int getQuestStepID() {
			return questStepID;
		}

		public void setQuestStepID(int questStepID) {
			this.questStepID = questStepID;
		}

		public QuestStep getPreviousStepID() {
			return previousStepID;
		}

		public void setPreviousStepID(QuestStep previousStepID) {
			this.previousStepID = previousStepID;
		}

		public QuestStep getNextStepID() {
			return nextStepID;
		}

		public void setNextStepID(QuestStep nextStepID) {
			this.nextStepID = nextStepID;
		}

		public NPC getGiverNPC() {
			return giverNPC;
		}

		public void setGiverNPC(NPC giverNPC) {
			this.giverNPC = giverNPC;
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

