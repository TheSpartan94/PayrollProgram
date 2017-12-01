package it.unipv.payroll.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")

/*
 * FlatEmployee are those who get paid every month with a Flat Salary. It
 * extends abstract Employee class, extended by the User class on which are
 * based the Administrator and Union classes. It needs only a Salary
 * specification.
 * 
 */

public class FlatEmployee extends Employee {

	int Salary;

	public FlatEmployee() {
		super();
	}

	public FlatEmployee(String password, String cF, String fullName, int salary, PaymentMethod pm, String datetobepaid)
			throws Exception {
		super(password, cF, fullName, pm, datetobepaid);
		Salary = salary;
	}

	// getters and setters

	public int getSalary() {
		return Salary;
	}

	public void setSalary(int salary) {
		Salary = salary;
	}

}
