package it.unipv.payroll.model;

import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import it.unipv.payroll.util.MD5Hash;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)

/*
 * Archetype for all interacting users possible : Administrators, Unions, and
 * Employees. It needs only an Auto-generated id and a password that is hashed
 * by the constructor and stored in the attribute. All comparisons and checks on
 * the password are performed using hashes, so they are not passed in cleartext.
 */

public abstract class User {
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String password;

	public User(String password) throws NoSuchAlgorithmException {
		this.password = MD5Hash.getmd5(password);
	}

	public User() {
	}

	// getters and setters

	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = MD5Hash.getmd5(password);
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

}
