package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.TimeCard;

@Stateless
public class TimeCardDAO {

	@PersistenceContext
	private EntityManager em;

	public void add(TimeCard tc) {
		em.persist(tc);
	}

	@SuppressWarnings(value = "unchecked")
	public List<TimeCard> findAll() {
		List<TimeCard> tc = (List<TimeCard>) em.createQuery("select p from " + TimeCard.class.getName() + " p")
				.getResultList();
		return tc;
	}

	// This Method makes sure the Card exists before deleting it.
	public boolean delete(TimeCard timecard) {
		TimeCard x = em.find(TimeCard.class, timecard.getId());
		if (x == null) {
			return false;
		} else {
			em.remove(em.contains(timecard) ? timecard : em.merge(timecard));
			return true;
		}
	}

}
