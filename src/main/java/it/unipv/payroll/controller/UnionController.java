package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import it.unipv.payroll.DAO.UnionDAO;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.Union;
import javax.inject.Named;

@SuppressWarnings("serial")
public class UnionController implements Serializable {

	@Inject	UnionDAO UnionDAO;
	@Inject	ServiceChargeController ServiceController;
	@Inject	@Named	EmployeeController employeeController;

	public void add(Union union) throws Exception {
		if (union != null)
			UnionDAO.add(union);
		else
			throw new Exception("Empty Union");
	}

	// Before deleting the Union, deletes also all its Service Charges

	public void delete(Union union) {
		ServiceController.deleteforUnion(union);
		for (Employee e: union.getEmployee())
			try {
				employeeController.deleteUnion(e);
				employeeController.update(e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		union.getEmployee().clear();
		update(union);
		UnionDAO.delete(union);
	}

	public List<Union> findall() {
		return UnionDAO.findall();
	}

	public Union find(int id) {
		return UnionDAO.find(id);

	}

	public Union find(String name) {
		return UnionDAO.find(name);

	}

	public void update(Union u) {
		UnionDAO.update(u);
	}
}
