package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.FlatEmployeeDAO;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@Stateless
public class FlatEmployeeController extends EmployeeController implements Serializable {
	@Inject	FlatEmployeeDAO FlatDAO;
	@Inject	ServiceChargeController ChargeController;

	public void delete(FlatEmployee fe) {
		FlatDAO.delete(fe);
	}

	public List<FlatEmployee> SelectivefindAll() {
		return FlatDAO.SelectivefindAll();
	}

	public boolean SetSalary(FlatEmployee e, int salary) {
		FlatEmployee x = (FlatEmployee) EmployeeDAO.find(e.getId());
		if (x == null)
			return false;
		x.setSalary(salary);
		EmployeeDAO.update(e);
		return true;
	}

	public float pay(FlatEmployee fe) {
		List<ServiceCharge> sclist = fe.getServiceCharges();
		float PayAmount = fe.getSalary();
		float uniondue = 0;
		Union union = fe.getUnion();

		// Checks if union exists: if not, the Union due stays 0.
		if (union != null)
			uniondue += union.getUniondue();

		// Union due is subtracted
		PayAmount -= uniondue;

		for (ServiceCharge sc : sclist) {
			PayAmount -= sc.getChargeAmount();
			ChargeController.delete(sc);
		}

		return PayAmount;

	}
}
