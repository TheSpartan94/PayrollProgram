package it.unipv.payroll.test;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unipv.payroll.util.MD5Hash;
import it.unipv.payroll.util.TimeManagUtil;

@RunWith(Arquillian.class)
public class TestUtil extends ArquillianTest {


	@Test
	public void testutilmd5() throws NoSuchAlgorithmException {
		String s = "my string";
		String realhash = "2ba81a47c5512d9e23c435c1f29373cb";
		Assert.assertTrue(realhash.toString().equals(MD5Hash.getmd5(s).toString()));
	}

	@Test
	public void testutilcalendar() throws ParseException {
		Calendar g = TimeManagUtil.getGCobjectfromString("22/04/2012 12:12:45");
		String timestamp = TimeManagUtil.getStringfromGCobject(g);
		Assert.assertTrue(timestamp.equals("22/04/2012 12:12:45"));

	}

	@Test
	public void testFuture() throws ParseException {
		boolean ok = TimeManagUtil.CheckFuture("01/01/2016");
		boolean notok = TimeManagUtil.CheckFuture("01/01/2018");
		Assert.assertTrue(ok);
		Assert.assertFalse(notok);
	}

}
