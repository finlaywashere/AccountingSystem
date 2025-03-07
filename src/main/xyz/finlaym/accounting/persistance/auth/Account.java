package xyz.finlaym.accounting.persistance.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account implements UserDetails{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String hash;
	
	@CreationTimestamp
	private Date createdAt;
	
	@UpdateTimestamp
	private Date updatedAt;
	
	@Column(nullable = true)
	private String privs;
	
	@Column
	private boolean deleted = false;
	
	public Account() {}
	public Account(String username, String email) {
		this.username = username;
		this.email = email;
		this.privs = "";
	}
	public void addPriv(String s) {
		if(this.privs == null)
			this.privs = "";
		this.privs += ":" + s;
		if(this.privs.startsWith(":"))
			this.privs = this.privs.substring(1);
	}
	public void removePriv(String s) {
		if(this.privs == null)
			return;
		String[] spl = this.privs.split(":");
		String newPrivs = "";
		for(String priv : spl) {
			if(!priv.equals(s)) {
				newPrivs += ":" + priv;
			}
		}
		if(!newPrivs.isEmpty())
			newPrivs = newPrivs.substring(1);
		this.privs = newPrivs;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		if(this.privs == null)
			return List.of();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String s : this.privs.split(":")) {
			authorities.add(new SimpleGrantedAuthority(s));
		}
		return authorities;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	@Override
	public String getPassword() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	@Override
	public boolean isEnabled() {
		return !deleted;
	}
	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}
	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}
}
