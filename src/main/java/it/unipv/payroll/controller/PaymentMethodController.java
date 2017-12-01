package it.unipv.payroll.controller;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import it.unipv.payroll.DAO.PaymentMethodDAO;
import it.unipv.payroll.model.PaymentMethod;

@SuppressWarnings("serial")
@Stateless
public class PaymentMethodController implements Serializable {
	@Inject	PaymentMethodDAO PaymentMthDAO;

	public void update(PaymentMethod pm) throws Exception {
		if (PaymentMthDAO.find(pm.getId()) != null)
			PaymentMthDAO.update(pm);
		else
			throw new Exception("Payment Method not found");
	}

	public PaymentMethod find(int id) {
		return PaymentMthDAO.find(id);
	}

}
