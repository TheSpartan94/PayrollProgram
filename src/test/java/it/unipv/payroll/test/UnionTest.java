package it.unipv.payroll.test;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.controller.ServiceChargeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;

@RunWith(Arquillian.class)
public class UnionTest extends ArquillianTest {
	@Inject	FlatEmployeeController FlatController;
	@Inject	ServiceChargeController ServiceController;
	@Inject	UnionController UnionController;

	PaymentMethod pm = new PaymentMethod("some-iban-here", PaymentType.BankAccount);

	@Before
	public void cleanup() {
		List<FlatEmployee> l = FlatController.SelectivefindAll();
		if (!l.isEmpty())
			for (FlatEmployee fe : l)
				FlatController.delete(fe);
		List<Union> lu = UnionController.findall();
		if (!lu.isEmpty())
			for (Union u : lu)
				UnionController.delete(u);
	}

	@Test
	public void Test() throws Exception {
		Union u1 = new Union("Sindacate", "pass", 80);
		FlatEmployee fe = new FlatEmployee("pass", "cfcfcfcfcfcfcfc9", "Flat Employee", 1200, pm, "13/12/2016");
		fe.setCF("FISCALCODE");
		fe.setFullName("Flat Employee");
		Union u2 = new Union("Other Union", "pass", 80);
		UnionController.add(u1);
		Assert.assertTrue(u1.getId() > 0);
		UnionController.add(u2);
		FlatController.add(fe);
		FlatController.addUnion(fe, u1);
		ServiceCharge charge1 = new ServiceCharge(60, "service for employee", "27/11/2016 20:35:00");
		charge1.setChargeAmount(60);
		charge1.setEmployee(fe);
		charge1.setService("service for employee");
		charge1.setTimeStamp("27/11/2016 20:35:00");
		ServiceController.add(charge1, fe, u1);
		Assert.assertTrue(fe.getUnion() != null);
		ServiceController.delete(charge1);
		FlatController.addUnion(fe, u2);
		FlatController.update(fe);

		UnionController.update(u1);
		Union tmp = UnionController.find(u1.getId()); /* for test find method */
		Assert.assertFalse(tmp.equals(null));
		tmp = UnionController.find(u1.getName());
		Assert.assertFalse(tmp.equals(null));

	}
}
