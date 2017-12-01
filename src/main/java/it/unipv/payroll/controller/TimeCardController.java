package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.TimeCardDAO;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;

@SuppressWarnings("serial")
@Stateless
public class TimeCardController implements Serializable {
	@Inject	TimeCardDAO CardDAO;

	public List<TimeCard> findAll() {
		return CardDAO.findAll();
	}

	public void delete(TimeCard tc) {
		CardDAO.delete(tc);
	}

	// The first section just checks if the timecard has been already entered
	// before using the timestamp value.

	public void add(TimeCard tc, TimeEmployee te) throws Exception {
		String timestamp = tc.getTimeStamp();
		for (TimeCard timecard : te.getTimescards())
			if (timestamp.equals(timecard.getTimeStamp()))
				throw new Exception("You've already entered this timecard!");
		CardDAO.add(tc);
		tc.SetEmployee(te);
	}

	// triggered when a time employee gets removed. Removes all his TimeCards
	// too.
	public boolean deleteforTimeEmployee(TimeEmployee te) {
		List<TimeCard> timecard = te.getTimescards();
		for (TimeCard iter : timecard)
			CardDAO.delete(iter);
		return true;
	}

}
