package it.unipv.payroll.test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.controller.PaymentMethodController;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.util.MD5Hash;

@RunWith(Arquillian.class)
public class FlatEmployeeTest extends ArquillianTest {

	@Inject	FlatEmployeeController FlatController;
	@Inject	PaymentMethodController paycontroller;

	@Before
	public void cleanup() throws NoSuchAlgorithmException {
		List<FlatEmployee> l = FlatController.SelectivefindAll();
		if (!l.isEmpty())
			for (FlatEmployee fe : l)
				FlatController.delete(fe);
	}

	@Test
	public void FlatEmployeeremove() throws Exception {
		PaymentMethod pm = new PaymentMethod("432342", PaymentType.BankAccount);

		FlatEmployee fe = new FlatEmployee(MD5Hash.getmd5("pass"), "cfcfcfcfcfcfcfc2", "pinco pallo", 500, pm,
				"13/12/2016");
		FlatController.add(fe);

		fe = (FlatEmployee) FlatController
				.findCF("cfcfcfcfcfcfcfc2"); /* test of the method findCF */
		Assert.assertTrue(!fe.equals(null));

		Assert.assertTrue("List has something", paycontroller.find(fe.getPaymentmethod().getId()) != null);
		FlatController.delete(fe);
		Assert.assertTrue("List is now empty", paycontroller.find(fe.getPaymentmethod().getId()) == null);

	}

	@Test
	public void ModifyPayment() throws Exception {
		PaymentMethod pm = new PaymentMethod("432342", PaymentType.BankAccount);
		FlatEmployee fe = new FlatEmployee(MD5Hash.getmd5("pass"), "cfcfcfcfcfcfcfc3", "prova pallo", 500, pm,
				"1/12/2016");
		FlatController.add(fe);
		pm.setAddress("3245382");
		pm.setType(PaymentType.Transfer);
		paycontroller.update(pm);
	}

}
