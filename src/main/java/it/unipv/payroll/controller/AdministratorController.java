package it.unipv.payroll.controller;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.AdministratorDAO;
import it.unipv.payroll.model.Administrator;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.MD5Hash;
import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@Stateless
public class AdministratorController implements Serializable {

	@Inject	TimeEmployeeController TimeController;
	@Inject	FlatEmployeeController FlatController;
	@Inject	CommissionEmployeeController CommissionController;
	@Inject	UnionController UnionController;
	@Inject	AdministratorDAO AdminDAO;

	/* Methods that add and refresh on calling */

	public List<Employee> addTimeEmployee(TimeEmployee te) throws Exception {
		return TimeController.add(te);
	}

	public List<Employee> addFlatEmployee(FlatEmployee fe) throws Exception {
		return FlatController.add(fe);
	}

	public List<Employee> addCommissionEmployee(CommissionEmployee ce) throws Exception {
		return CommissionController.add(ce);
	}

	public void deleteUnion(Union u) {
		UnionController.delete(u);
	}

	/****************************************/

	// Deletes an employee calling his controller according to the type.

	public void deleteEmployee(Employee e) throws Exception {
		switch (e.getClass().getSimpleName()) {
		case "TimeEmployee":
			TimeController.delete((TimeEmployee) e);
			break;
		case "FlatEmployee":
			FlatController.delete((FlatEmployee) e);
			break;
		case "CommissionEmployee":
			CommissionController.delete((CommissionEmployee) e);
			break;
		default:
			throw new Exception("Employee Class not recognized");
		}

	}

	public void addUnion(Union u) throws Exception {
		UnionController.add(u);
	}

	public void AssignUnion(Employee e, Union u) throws Exception {
		FlatController.addUnion(e, u);
	}

	public void add(Administrator a) {
		AdminDAO.add(a);
	}

	/*
	 * Methods that update fields of the employees and automatically forward the
	 * modifications to the database.
	 */

	public boolean setSalary(int id, int salary) {
		FlatEmployee e = (FlatEmployee) FlatController.find(id);
		boolean result = FlatController.SetSalary(e, salary);
		FlatController.update(e);
		return result;
	}

	public boolean setHourRate(int id, float rate) {
		TimeEmployee e = (TimeEmployee) TimeController.find(id);
		boolean result = TimeController.SetHourRate(e, rate);
		TimeController.update(e);
		return result;

	}

	public boolean setCommissionBase(int id, float base) {
		CommissionEmployee e = (CommissionEmployee) CommissionController.find(id);
		boolean result = CommissionController.Setbase(e, base);
		CommissionController.update(e);
		return result;
	}

	public boolean setCommissionAmount(int id, double amount) {
		CommissionEmployee e = (CommissionEmployee) CommissionController.find(id);
		boolean result = CommissionController.Setamount(e, amount);
		CommissionController.update(e);
		return result;

	}

	// *******************************************************

	/*
	 * Delete an Administrator from the control panel. Needs the password of the
	 * admin being removed
	 */

	public boolean delete(Administrator a, String Adminpassword) throws NoSuchAlgorithmException {

		String truepass = a.getPassword();
		String pass = MD5Hash.getmd5(Adminpassword);

		if (a != null && truepass.equals(pass)) {
			AdminDAO.delete(a);
			return true;
		} else
			return false;
	}

	/* Refreshing utilities */

	public List<Administrator> findAll() {
		return AdminDAO.findAll();
	}

	public List<Employee> findEmployees() {
		return FlatController.findAll();
	}

	public List<Union> findUnions() {
		return UnionController.findall();
	}

	/*
	 * Core method of the whole project. It checks the current day and gets the
	 * employees according to their type if they were meant to be paid on the
	 * current day. Then, changes the payday according to the type, and prints a
	 * record of those employees with the amount of money they must be paid. See
	 * each Controller for information on how the computation of the salary is
	 * performed.
	 */

	public List<String> RunPayroll() throws Exception {
		List<Employee> Employees = findEmployees();
		List<String> output = new ArrayList<>();
		double PayAmount;
		for (Employee e : Employees) {
			String payday = e.getDayToBePaid();
			if (TimeManagUtil.ComparetoNow(payday)) {
				switch (e.getClass().getSimpleName()) {
				case "TimeEmployee":
					payday = TimeManagUtil.getStringfromDayGCobject(TimeManagUtil.sumdaytoDayGCobjet(payday, 7));
					PayAmount = TimeController.pay((TimeEmployee) e);
					break;
				case "FlatEmployee":
					payday = TimeManagUtil.getStringfromDayGCobject(TimeManagUtil.summonthtoDayGCobjet(payday, 1));
					PayAmount = FlatController.pay((FlatEmployee) e);
					break;
				case "CommissionEmployee":
					payday = TimeManagUtil.getStringfromDayGCobject(TimeManagUtil.sumdaytoDayGCobjet(payday, 7));
					PayAmount = CommissionController.pay((CommissionEmployee) e);
					break;
				default:
					throw new Exception("Employee Class not recognized");
				}
				e.setDayToBePaid(payday);
				if (PayAmount > 0)
					output.add("Employee with id " + e.getId() + " has to be paid: " + PayAmount
							+ " by payment method: " + e.getPaymentmethod().getType());
				else
					output.add("Employee with id " + e.getId() + " has a debt with the company of: " + PayAmount);
			}

		}
		if (output.isEmpty())
			output.add("There's no one to pay today.");
		return output;
	}
}
