package it.unipv.payroll.test;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.AdministratorController;
import it.unipv.payroll.controller.SalesReceiptController;
import it.unipv.payroll.controller.ServiceChargeController;
import it.unipv.payroll.controller.TimeCardController;
import it.unipv.payroll.model.Administrator;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.SalesReceipt;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;

@RunWith(Arquillian.class)
public class AdministratorTest extends ArquillianTest {
	Administrator a;
	@Inject	SalesReceiptController SalesController;
	@Inject	AdministratorController AdminController;
	@Inject	TimeCardController CardController;
	@Inject	ServiceChargeController ServiceController;

	@Before
	public void cleanup() throws Exception { 
// i want to delete all administrators and employees in the database
		List<Administrator> la = AdminController.findAll();
		List<Employee> le = AdminController.findEmployees();
		List<Union> lu = AdminController.findUnions();
		if (!la.isEmpty())
			for (Administrator a : la)
				AdminController.delete(a, a.getPassword());
		if (!lu.isEmpty())
			for (Union u : lu)
				AdminController.deleteUnion(u);
		if (!le.isEmpty())
			for (Employee e : le)
				AdminController.deleteEmployee(e);

	}

	@Test
	public void AdministratorAdd() throws Exception {
		a = new Administrator("pass");
		AdminController.add(a);
		Assert.assertFalse("There is an administrator", AdminController.findAll().isEmpty());
		// Assert.assertTrue("And now he has been removed",
		// AdminController.delete(a, "password"));
	}

	@Test
	public void RunPayrollTest() throws Exception {
		PaymentMethod pm = new PaymentMethod("iban", PaymentType.BankAccount);
		PaymentMethod pm2 = new PaymentMethod("iban2", PaymentType.BankAccount);
		PaymentMethod pm3 = new PaymentMethod("iban3", PaymentType.BankAccount);

		FlatEmployee fe = new FlatEmployee("pass", "cfcfcfcfcfcfcfc7", "Flat Test", 1200, pm, "12/12/2016");
		CommissionEmployee ce = new CommissionEmployee("pass", "cfcfcfcfcfcfcf00", pm2, "Commission Test", 0.2, 100,
				"12/12/2016");
		TimeEmployee te = new TimeEmployee("pass", "cfcfcfcfcfcfcfc8", "Time Test", 50, pm3, "12/12/2016");
		SalesReceipt sr = new SalesReceipt("05/12/2016 10:53:00", 50, ce);
		TimeCard tc = new TimeCard("20/01/2016 18:32:00", 60);
		Union u = new Union("Service Union", "pass", 200);
		ServiceCharge charge1 = new ServiceCharge(60, "service for employee", "27/11/2016 20:35:00");
		AdminController.addFlatEmployee(fe);
		AdminController.setSalary(fe.getId(), 600);
		AdminController.addCommissionEmployee(ce);
		AdminController.setCommissionAmount(ce.getId(), 0.3);
		AdminController.setCommissionBase(ce.getId(), 1000);
		AdminController.addTimeEmployee(te);
		AdminController.setHourRate(te.getId(), 60);
		CardController.add(tc, te);
		SalesController.add(sr, ce);
		AdminController.addUnion(u);
		ServiceController.add(charge1, fe, u);
		List<String> list = AdminController.RunPayroll();

		if (!list.isEmpty())
			System.out.println(list);
		

	}
}
