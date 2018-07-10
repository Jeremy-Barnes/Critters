package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Jeremy on 7/10/2018.
 */
@Entity
@Table(name="npcItemQuestPreferenceConfigs")
public class NPCItemQuestPreferenceConfig extends DTO {
	
  @Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Integer npcItemQuestPreferenceConfigID;
	private int wanterNPC;
  
  @ManyToOne
	@JoinColumn(name="itemID", updatable = false)
	private Item.ItemDescription item;
	private int critterBuxxValuePerItem;
}
