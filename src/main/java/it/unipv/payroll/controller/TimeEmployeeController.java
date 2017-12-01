package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.TimeEmployeeDAO;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@Stateless
public class TimeEmployeeController extends EmployeeController implements Serializable {
	@Inject	TimeEmployeeDAO TimeDAO;
	@Inject	TimeCardController CardController;
	@Inject	ServiceChargeController ChargeController;

	// Before deleting the Time Employee, deletes its timecards first.

	public boolean delete(TimeEmployee te) {
		CardController.deleteforTimeEmployee(te);
		TimeDAO.delete(te);
		return true;
	}

	public boolean SetHourRate(TimeEmployee e, float hourrate) {
		TimeEmployee x = (TimeEmployee) EmployeeDAO.find(e.getId());
		if (x == null)
			return false;
		x.setHourRate(hourrate);
		EmployeeDAO.update(e);
		return true;
	}

	public List<TimeEmployee> SelectivefindAll() {
		return TimeDAO.SelectivefindAll();
	}

	/*
	 * public TimeEmployee find(int id){ return TimeDAO.find(id);
	 * 
	 * }
	 */

	public float pay(TimeEmployee te) {
		List<TimeCard> tclist = te.getTimescards();
		List<ServiceCharge> sclist = te.getServiceCharges();
		Union union = te.getUnion();
		float uniondue = 0;
		float HourRate = te.getHourRate();
		float PayAmount = 0;
		float HoursWorked;

		// Checks if union exists: if not, the Union due stays 0.
		if (union != null)
			uniondue += union.getUniondue();
		for (TimeCard tc : tclist) {
			HoursWorked = tc.getMinuteWorked() / 60;
			if (HoursWorked > 8)
				PayAmount += HoursWorked * HourRate * 1.5;
			else
				PayAmount += HoursWorked * HourRate;
			CardController.delete(tc);
		}

		for (ServiceCharge sc : sclist) {
			PayAmount -= sc.getChargeAmount();
			ChargeController.delete(sc);
		}

		// The union due is subtracted
		PayAmount -= uniondue;
		return PayAmount;
	}
}
