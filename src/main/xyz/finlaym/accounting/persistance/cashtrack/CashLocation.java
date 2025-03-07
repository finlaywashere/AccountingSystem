package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CashLocation {
	@Id
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String location;
	
	@Column
	private int amount;
	
	@CreationTimestamp
	private Date createdAt;
	
	@UpdateTimestamp
	private Date updatedAt;
	
	@Column
	private boolean deleted = false;
	
	@Column
	private boolean virtual = false;
	
	public CashLocation() {
		
	}
	public CashLocation(String name, String location, boolean virtual) {
		this.name = name;
		this.location = location;
		this.virtual = virtual;
		this.amount = 0;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	public int getAmount() {
		return amount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public boolean isDeleted() {
		return deleted;
	}
	
	public boolean setDeleted(boolean deleted) {
		if(this.amount == 0) {
			this.deleted = deleted;
			return true;
		}
		return false;
	}
	public boolean isVirtual() {
		return virtual;
	}
	
}
