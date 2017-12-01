package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import it.unipv.payroll.DAO.ServiceChargeDAO;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
public class ServiceChargeController implements Serializable {

	@Inject	ServiceChargeDAO ServiceDAO;

	public void add(ServiceCharge charge, Employee e, Union u) {
		charge.setUnion(u);
		charge.setEmployee(e);
		ServiceDAO.add(charge);

	}

	// Called only by the UnionController in order to delete all service charges
	// assessed by the Union.

	public boolean deleteforUnion(Union u) {
		List<ServiceCharge> l = u.getServiceCharges();
		if (l.isEmpty() == true)
			return false;
		for (ServiceCharge iter : l)
			ServiceDAO.delete(iter);
		return true;
	}

	public void delete(ServiceCharge servicecharge) {
		ServiceDAO.delete(servicecharge);
	}
}
