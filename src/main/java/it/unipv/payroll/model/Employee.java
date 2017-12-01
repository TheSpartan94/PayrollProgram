package it.unipv.payroll.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import it.unipv.payroll.util.TimeManagUtil;

@Entity
@Table
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Inheritance(strategy = InheritanceType.JOINED)

/*
 * Abstraction for all below Employee types. It has only the common attributes
 * and methods that are simultanously necessary to the Flat Salaried, Time and
 * Commission Employees.
 */

public abstract class Employee extends User {
	@Column(unique = true)
	String CF;
	String FullName;
	@OneToOne(cascade = { CascadeType.ALL })	@NotNull
	PaymentMethod paymentmethod;

	@OneToMany(targetEntity = ServiceCharge.class, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "employee")
	List<ServiceCharge> servicecharges; // bidirectional relationship, see servicecharge
										
	@ManyToOne	@JoinColumn(name = "union_id")
	Union union;

	@Temporal(TemporalType.TIMESTAMP)
	Calendar DayToBePaid;
	
	

	public Union getUnion() {
		return union;
	}

	protected Employee(String password, String cF, String fullName, PaymentMethod pm, String datetobepaid)
			throws Exception {
		super(password);
		if (cF.length() == 16)
			this.CF = cF;
		else
			throw new Exception("You typed a wrong number of characters for the Fiscal Code. \n They MUST be 16.");
		this.FullName = fullName;
		this.paymentmethod = pm;
		this.DayToBePaid = TimeManagUtil.getDayGCobjectfromString(datetobepaid);
		this.servicecharges = new ArrayList<>();
	}

	public Employee() {
		super();
	}

	// getters and setters

	public String getCF() {
		return CF;
	}

	public void setCF(String cF) {
		CF = cF.toUpperCase();
	}

	public String getDayToBePaid() {
		return TimeManagUtil.getStringfromDayGCobject(DayToBePaid);
	}

	public void setDayToBePaid(String dayToBePaid) throws ParseException {
		this.DayToBePaid = TimeManagUtil.getDayGCobjectfromString(dayToBePaid);
	}

	public String getFullName() {
		return FullName;
	}

	public PaymentMethod getPaymentmethod() {
		return paymentmethod;
	}

	public void setPaymentmethod(PaymentMethod paymentmethod) {
		this.paymentmethod = paymentmethod;
	}

	public void setUnion(Union union) {
		this.union = union;
	}
	
	public void unsetUnion(){
		this.union = null;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public List<ServiceCharge> getServiceCharges() {
		return servicecharges;
	}

	public void addServiceCharge(ServiceCharge sc) {
		servicecharges.add(sc);
	}

	public void removeServiceCharge(ServiceCharge sc) {
		servicecharges.remove(sc);
	}

}
