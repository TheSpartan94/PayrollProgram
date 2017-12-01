package it.unipv.payroll.test;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
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
public class ServiceChargeTest extends ArquillianTest {

	PaymentMethod pm = new PaymentMethod("some-iban-here", PaymentType.BankAccount);

	@Inject	FlatEmployeeController FlatController;
	@Inject	ServiceChargeController ServiceController;
	@Inject	UnionController UnionController;

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
	public void RemoveTest() throws Exception {
		Union u1 = new Union("Sindacate", "pass", 80);
		FlatEmployee fe = new FlatEmployee("pass", "cfcfcfcfcfcfcfc5", "Flat Employee", 1200, pm, "13/12/2016");
		ServiceCharge charge1 = new ServiceCharge(60, "service for employee", "27/11/2016 20:35:00");
		UnionController.add(u1);
		FlatController.add(fe);
		ServiceController.add(charge1, fe, u1);
		ServiceController.delete(charge1);
	}
}
