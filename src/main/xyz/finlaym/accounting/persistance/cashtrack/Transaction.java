package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import xyz.finlaym.accounting.persistance.auth.Account;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Account user;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private CashLocation from;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private CashLocation to;
	
	@CreationTimestamp
	private Date createdAt;
	
	@Column
	private int amount;
	
	public Transaction() {
		
	}

	public Transaction(Account user, CashLocation from, CashLocation to, int amount) {
		this.user = user;
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public Account getUser() {
		return user;
	}

	public CashLocation getFrom() {
		return from;
	}

	public CashLocation getTo() {
		return to;
	}

	public int getAmount() {
		return amount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
}
