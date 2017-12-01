package it.unipv.payroll.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")

/*
 * Commission Employee are employees that are paid with a flat salary
 * (CommissionBase field), but also according to the amount of sales they
 * perform (they earn only a percentage of the sale price, given by
 * CommissionBase in decimal values).
 */
public class CommissionEmployee extends Employee {

	@OneToMany(targetEntity = SalesReceipt.class, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "employee")
	private List<SalesReceipt> salesrecepit;

	private double CommissionAmount, CommissionBase;
	
	

	public CommissionEmployee(String password, String cF, PaymentMethod pm, String fullName, double CommissionAmount,
			double CommissionBase, String datetobepaid) throws Exception {
		super(password, cF, fullName, pm, datetobepaid);
		if (CommissionAmount > 1)
			throw new Exception("Commission Amount can only be a decimal value smaller or equal than 1");
		else
			this.CommissionAmount = CommissionAmount;
		this.CommissionBase = CommissionBase;
		salesrecepit = new ArrayList<>();

	}

	public CommissionEmployee() {
		super();
	}

	// getters and setters

	public double getCommissionAmount() {
		return CommissionAmount;
	}

	public void setCommissionAmount(double commissionAmount) {
		CommissionAmount = commissionAmount;
	}

	public double getCommissionBase() {
		return CommissionBase;
	}

	public void setCommissionBase(double commissionBase) {
		CommissionBase = commissionBase;
	}

	public void PostSaleReceipt(SalesReceipt recepit) {
		salesrecepit.add(recepit);
	}

	public List<SalesReceipt> getSalesRecepit() {
		return salesrecepit;

	}
}
