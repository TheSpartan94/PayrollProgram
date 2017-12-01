package it.unipv.payroll.test;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.controller.UserController;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;

@RunWith(Arquillian.class)
public class LoginTest extends ArquillianTest {

	@Inject	FlatEmployeeController FlatController;
	@Inject	UserController uc;

	PaymentMethod pm = new PaymentMethod("432342", PaymentType.BankAccount);

	@Before
	public void cleanup() throws Exception {
		List<FlatEmployee> l = FlatController.SelectivefindAll();
		if (!l.isEmpty())
			for (FlatEmployee fe : l)
				FlatController.delete(fe);
	}

	@Test
	public void Test() throws Exception {
		FlatEmployee fe = new FlatEmployee("pass", "cfcfcfcfcfcfcfc6", "pinco pallo", 500, pm, "13/12/2016");
		FlatController.add(fe);
		boolean login = uc.login(fe.getId(), "pass");
		Assert.assertTrue(login);
	}
}
