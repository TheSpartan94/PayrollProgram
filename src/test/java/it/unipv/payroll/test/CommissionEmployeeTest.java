package it.unipv.payroll.test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.CommissionEmployeeController;
import it.unipv.payroll.controller.SalesReceiptController;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.SalesReceipt;
import it.unipv.payroll.util.MD5Hash;

@RunWith(Arquillian.class)
public class CommissionEmployeeTest extends ArquillianTest {

	@Inject	CommissionEmployeeController CommissionController;
	@Inject	SalesReceiptController SalesController;

	@Before
	public void cleanup() throws NoSuchAlgorithmException {
		List<CommissionEmployee> l = CommissionController.SelectivefindAll();
		if (!l.isEmpty())
			for (CommissionEmployee ce : l)
				CommissionController.delete(ce);
	}

	@Test
	public void CommissionEmployeeMethodTest() throws Exception {

		PaymentMethod pm = new PaymentMethod("some-iban-here", PaymentType.BankAccount);
		CommissionEmployee ce = new CommissionEmployee(MD5Hash.getmd5("mypass"), "cfcfcfcfcfcfcfc4", pm, "john Doe",
				0.3, 100f, "13/12/2016");
		SalesReceipt sr = new SalesReceipt("25/11/2015 18:37:00", 50f, ce);
		sr.setEmployee(ce);
		sr.setSaleAmount(50f);
		sr.setTimeStamp("25/11/2015 18:37:00");
		CommissionController.add(ce);
		SalesController.add(sr, ce);
		CommissionController.delete(ce);
		Assert.assertTrue("All Sales Receipt are gone!", SalesController.findAll().isEmpty());
	}

}
