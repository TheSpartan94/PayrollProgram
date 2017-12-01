package it.unipv.payroll.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@Entity
@Table

/*
 * TimeCards are records of the hours worked by an employee, and they are used
 * by the application in order to pay Time Employees. They keep as attributes
 * the time the employee has finished working, the minutes he has been working,
 * and the TimeEmployee they are related to.
 */

public class TimeCard implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar TimeStamp;
	private int MinuteWorked;
	@ManyToOne
	@JoinColumn(name = "timeemployee_id")
	TimeEmployee timeemployee;

	public TimeCard() {
		super();
	}

	public TimeCard(String date, int minuteworked) throws Exception {
		super();
		this.TimeStamp = TimeManagUtil.getGCobjectfromString(date);
		if (minuteworked >= 60 * 20)
			throw new Exception("You can't have worked 20 hours!");
		else
			this.MinuteWorked = minuteworked;
	}

	// getters and setters

	public String getTimeStamp() {
		return TimeManagUtil.getStringfromGCobject(TimeStamp);
	}

	public void setTimeStamp(String timestamp) throws ParseException {
		this.TimeStamp = TimeManagUtil.getGCobjectfromString(timestamp);
	}

	public int getMinuteWorked() {
		return MinuteWorked;
	}

	public void setMinuteWorked(int minuteWorked) {
		MinuteWorked = minuteWorked;
	}

	public int getId() {
		return id;
	}

	public void SetEmployee(TimeEmployee te) {
		this.timeemployee = te;
	}

}
