package it.unipv.payroll.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")

/*
 * Employees that are paid according to the hours spent working, they submit
 * TimeCards which record the hours spent and the day they've been working. The
 * class defines a bidirectional OneToMany relationship with the TimeCard class.
 */

public class TimeEmployee extends Employee {

	@OneToMany(targetEntity = TimeCard.class, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "timeemployee")
	private List<TimeCard> timecards;

	private float HourRate;

	public TimeEmployee(String password, String cF, String fullName, float HourRate, PaymentMethod pm,
			String datetobepaid) throws Exception {
		super(password, cF, fullName, pm, datetobepaid);
		this.paymentmethod = pm;
		this.HourRate = HourRate;
		timecards = new ArrayList<>();
	}

	public TimeEmployee() {
		super();
	}

	// getters and setters

	public float getHourRate() {
		return HourRate;
	}

	public void setHourRate(float hourRate) {
		HourRate = hourRate;
	}

	public void PostTimeCard(TimeCard timecard) {
		timecards.add(timecard);
	}

	public List<TimeCard> getTimescards() {
		return timecards;
	}

}
