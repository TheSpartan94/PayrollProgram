package it.unipv.payroll.model;

import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)

/*
 * Administrator class is very poor, it only needs to be distinguished by the
 * abstract User class.
 */
public class Administrator extends User {

	public Administrator(String password) throws NoSuchAlgorithmException {
		super(password);
	}

	public Administrator() {
	}
}
