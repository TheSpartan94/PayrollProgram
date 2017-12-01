package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.CommissionEmployeeDAO;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.SalesReceipt;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@Stateless
public class CommissionEmployeeController extends EmployeeController implements Serializable {

	@Inject	CommissionEmployeeDAO commissionDAO;
	@Inject	SalesReceiptController salescontroller;
	@Inject	ServiceChargeController ChargeController;

	// Before deleting the commissionemployee, delete his sales receipts first

	public void delete(CommissionEmployee ce) {
		salescontroller.deleteforCommissionEmployee(ce);
		commissionDAO.delete(ce);
	}

	public List<CommissionEmployee> SelectivefindAll() {
		return commissionDAO.SelectivefindAll();
	}

	public boolean Setbase(CommissionEmployee e, float base) {
		CommissionEmployee x = (CommissionEmployee) EmployeeDAO.find(e.getId());
		if (x == null)
			return false;
		x.setCommissionBase(base);
		EmployeeDAO.update(e);
		return true;
	}

	public boolean Setamount(CommissionEmployee e, double amount) {
		CommissionEmployee x = (CommissionEmployee) EmployeeDAO.find(e.getId());
		if (x == null)
			return false;
		x.setCommissionAmount(amount);
		EmployeeDAO.update(e);
		return true;
	}

	public double pay(CommissionEmployee ce) {
		List<SalesReceipt> srlist = ce.getSalesRecepit();
		List<ServiceCharge> sclist = ce.getServiceCharges();
		Union union = ce.getUnion();
		float uniondue = 0;
		double PayAmount = ce.getCommissionBase();

		// Checks if union exists: if not, the Union due stays 0.
		if (union != null)
			uniondue += union.getUniondue();

		for (SalesReceipt sr : srlist) {
			PayAmount += sr.getSaleAmount() * ce.getCommissionAmount();
			salescontroller.delete(sr);
		}

		for (ServiceCharge sc : sclist) {
			PayAmount -= sc.getChargeAmount();
			ChargeController.delete(sc);
		}

		PayAmount -= uniondue;
		return PayAmount;
	}
}
