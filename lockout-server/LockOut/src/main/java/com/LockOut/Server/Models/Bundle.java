package com.LockOut.Server.Models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bundle {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	@OneToOne(cascade= CascadeType.ALL)
	private Reward item;
	
	private int amount;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Reward getItem() {
		return item;
	}

	public void setItem(Reward item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
