package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import it.unipv.payroll.DAO.EmployeeDAO;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@Named
@Stateless

// Parent class for controllers of all kinds of employee.

public class EmployeeController implements Serializable {

	@Inject	@Named("employeeDAO")	EmployeeDAO EmployeeDAO;

	public List<Employee> add(Employee e) throws Exception {

		if (EmployeeDAO.findCF(e.getCF()) == null) {
			EmployeeDAO.add(e);
			return null;
		} else
			throw new Exception("Fiscal Code error.");
	}

	public List<Employee> findAll() {
		return EmployeeDAO.findAll();
	}

	public Employee findCF(String CF) {
		return EmployeeDAO.findCF(CF);

	}

	public Employee find(int id) {
		return EmployeeDAO.find(id);
	}

	public void update(Employee e) {
		EmployeeDAO.update(e);
	}

	// Assigns the Employee to a specific Union

	public void addUnion(Employee e, Union u) throws Exception {
		if (e.getServiceCharges() == null || e.getServiceCharges().isEmpty())
			e.setUnion(u);
		else
			throw new Exception("Service Charges are not empty");
	}
	
	public void deleteUnion(Employee e) throws Exception {
		if (e.getServiceCharges() == null || e.getServiceCharges().isEmpty())
			e.unsetUnion();
		else
			throw new Exception("Service Charges are not empty");
	}
}
