package it.unipv.payroll.DAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.PaymentMethod;

@Stateless
public class PaymentMethodDAO {
	@PersistenceContext
	private EntityManager em;

	public PaymentMethod find(int id) {
		try {
			return em.find(PaymentMethod.class, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	// Needed when an operation that changes some of the attributes of a payment
	// method is called. It forwards the modifications to the database.
	public void update(PaymentMethod pm) {
		em.merge(pm);
	}

}
