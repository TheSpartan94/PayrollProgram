package it.unipv.payroll.DAO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.User;

public class UserDAO {

	@PersistenceContext
	private EntityManager em;

	// Used for logging in

	public User find(int id) {
		try {
			return em.find(User.class, id);
		} catch (NoResultException e) {
			return null;
		}
	}
}
