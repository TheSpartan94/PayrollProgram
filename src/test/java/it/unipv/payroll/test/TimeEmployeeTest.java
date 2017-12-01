package it.unipv.payroll.test;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.controller.TimeCardController;
import it.unipv.payroll.controller.TimeEmployeeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.controller.UserController;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.MD5Hash;

@RunWith(Arquillian.class)
public class TimeEmployeeTest extends ArquillianTest {

	@Inject	TimeEmployeeController TimeController;
	@Inject	TimeCardController CardController;
	@Inject	UnionController UnionController;
	@Inject	UserController UserController;

	PaymentMethod pm = new PaymentMethod("some-iban-here", PaymentType.BankAccount);
	TimeEmployee te;
	Union u;

	@Before
	public void cleanup() { // i want to delete all timeemployee in the database
		List<TimeEmployee> l = TimeController.SelectivefindAll();
		if (!l.isEmpty())
			for (TimeEmployee te : l)
				TimeController.delete(te);
		u = UnionController.find("Other Sindacate");
		if (u != null)
			UnionController.delete(u);
	}

	public void TimeEmployeeadd() throws Exception {
		te = new TimeEmployee("pass", "cfcfcfcfcfcfcfc1", "Test Subject", 500, pm, "13/12/2016");
		u = new Union("Other Sindacate", "pass", 80);
		te.setPassword(MD5Hash.getmd5("pass"));
		TimeCard tc1 = new TimeCard("25/11/2016 15:17:00", 120);
		TimeCard tc2 = new TimeCard("26/11/2016 17:08:00", 140);
		TimeCard tc3 = new TimeCard();

		tc3.setMinuteWorked(200);
		tc3.setTimeStamp("26/1/2016 10:08:00");
		TimeController.add(te);
		TimeController.addUnion(te, u);
		UnionController.add(u);
		CardController.add(tc1, te);
		CardController.add(tc2, te);
		CardController.add(tc3, te);
		te = (TimeEmployee) TimeController.find(te.getId());
		for (TimeCard iter : te.getTimescards())
		Assert.assertFalse(te.getTimescards().isEmpty());
		Assert.assertFalse(TimeController.findAll().isEmpty());
		Assert.assertTrue("I'm a Time Employee", UserController.getUserClass(te.getId()).equals("TimeEmployee"));
	}

	@Test
	public void TimeEmployeeremove() throws Exception {
		TimeEmployeeadd();
		TimeController.delete(te);
		Assert.assertTrue("all time employee is gone", TimeController.SelectivefindAll().isEmpty());
		Assert.assertTrue("TimeCards gone !", CardController.findAll().isEmpty());
	}
}
