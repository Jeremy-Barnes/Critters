package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

	@Entity
	@Table(name = "questStepConfigs")
	public static class QuestStep extends DTO {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
		private int questStepID;

		@ManyToOne
		@JoinColumn(name = "previousStepID")
		private QuestStep previousStepID;

		@ManyToOne
		@JoinColumn(name = "nextStepID")
		private QuestStep nextStepID;

		@ManyToOne
		@JoinColumn(name = "giverNPC")
		private QuestStep giverNPC;
	}
}

