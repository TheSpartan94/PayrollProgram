package it.unipv.payroll.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table
/*
 * Payment Methods define a OneToOne relationship with each Employee so his
 * payment address is unique. They can be only of three type: Check, Transfer,
 * and BankAccount. They are identified just by their own id number.
 */

public class PaymentMethod implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	public static enum PaymentType {
		Check, Transfer, BankAccount
	};

	String Address; /* IBAN or other */
	PaymentType type;

	public PaymentMethod() {
	}

	public PaymentMethod(String PaymentDeliveryAddress, PaymentType PaymentType) {
		this.Address = PaymentDeliveryAddress;
		this.type = PaymentType;
	}

	// getters and setters

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

}
