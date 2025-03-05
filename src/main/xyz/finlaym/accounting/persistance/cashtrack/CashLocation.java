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
	
	public CashLocation() {
		
	}
	public CashLocation(String name, String location) {
		this.name = name;
		this.location = location;
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
}
