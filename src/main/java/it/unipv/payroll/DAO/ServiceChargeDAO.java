package it.unipv.payroll.DAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.ServiceCharge;

@Stateless
public class ServiceChargeDAO {

	@PersistenceContext
	private EntityManager em;

	public void add(ServiceCharge charge) {
		em.persist(charge);
	}

	public ServiceCharge find(int id) {
		return em.find(ServiceCharge.class, id);
	}

	// Needed when an operation that changes some of the attributes of an
	// employee is called. It forwards the modifications to the database.
	public boolean delete(ServiceCharge servicecharge) {
		ServiceCharge x = find(servicecharge.getChargeID());
		if (x == null) {
			return false;
		} else {
			em.remove(em.contains(x) ? x : em.merge(x));
			return true;
		}
	}
}
