package it.unipv.payroll.controller;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Named;

import it.unipv.payroll.DAO.UserDAO;
import it.unipv.payroll.model.User;
import it.unipv.payroll.util.MD5Hash;

@SuppressWarnings("serial")
@Named
public class UserController implements Serializable {
	@Inject	UserDAO UserDAO;

	/*
	 * Needs an id and a password. First makes sure the user with the given id
	 * exists, then it compares the hashes of the passwords. If they are the
	 * same, the method return true. The return type is false in any other case.
	 */

	public boolean login(int id, String password) throws NoSuchAlgorithmException {
		User user = UserDAO.find(id);
		if (password.isEmpty())
			return false;
		else {
			String pass = MD5Hash.getmd5(password);
			if (user != null) {
				String truepass = user.getPassword();
				if (truepass.equals(pass))
					return true;
				else
					return false;

			} else
				return false;
		}
	}

	public String getUserClass(int id) {
		return find(id).getClass().getSimpleName();
	}

	public User find(int id) {
		return UserDAO.find(id);
	}

}
