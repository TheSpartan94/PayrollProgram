package it.unipv.payroll.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.SalesReceiptDAO;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.SalesReceipt;

@SuppressWarnings("serial")
@Stateless
public class SalesReceiptController implements Serializable {

	@Inject	SalesReceiptDAO SalesDAO;

	// The first section just checks if the sales receipt has been already
	// entered before using the timestamp value.

	public void add(SalesReceipt sr, CommissionEmployee ce) throws Exception {
		String timestamp = sr.getTimeStamp();
		for (SalesReceipt salesreceipt : ce.getSalesRecepit())
			if (timestamp.equals(salesreceipt.getTimeStamp()))
				throw new Exception("You've already entered this sales receipt!");
		SalesDAO.add(sr);
		ce.PostSaleReceipt(sr);
	}

	public List<SalesReceipt> findAll() {
		return SalesDAO.findAll();
	}

	public void delete(SalesReceipt sr) {
		SalesDAO.delete(sr);
	}

	// triggered when a commission employee gets removed. Removes all his
	// salerecepit too.
	public boolean deleteforCommissionEmployee(CommissionEmployee ce) {
		if (ce == null)
			return false;
		List<SalesReceipt> l = ce.getSalesRecepit();
		if (l.isEmpty() == true)
			return false;
		for (SalesReceipt iter : l)
			SalesDAO.delete(iter);
		return true;
	}
}
