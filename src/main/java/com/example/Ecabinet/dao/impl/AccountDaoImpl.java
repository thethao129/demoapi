package com.example.Ecabinet.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Ecabinet.dao.AccountDao;
import com.example.Ecabinet.entity.Account;
import com.example.Ecabinet.entity.CurrentUser;
import com.example.Ecabinet.entity.User;

@Repository
@Transactional
public class AccountDaoImpl implements AccountDao, UserDetailsService {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void createAccount(Account account) {
		entityManager.persist(account);
	}

	@Override
	public void deleteAccount(long id) {

		System.out.println("-- delete account by id --");
		Query query = entityManager.createQuery("DELETE FROM Account a WHERE a.accountNumber = :aid ");
		query.setParameter("aid", id);
		int rowsDeleted = query.executeUpdate();
		System.out.println("entities deleted: " + rowsDeleted);

	}

	@Override
	public Account getAccountById(long id) {
		try {
			String jpql = "SELECT NEW Account(a.accountNumber, a.address, a.age, a.balance, a.city, a.email, a.employer, a.firstname, a.gender, a.lastname, a.state)  FROM Account AS a WHERE a.accountNumber LIKE :aid ";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("aid", id);
			return (Account) query.getSingleResult();
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}

	@Override
	public List<Account> getAll() {
		try {
			String jpql = "SELECT NEW Account(a.accountNumber, a.address, a.age, a.balance, a.city, a.email, a.employer, a.firstname, a.gender, a.lastname, a.state) FROM  Account AS a ";
			Query query = entityManager.createQuery(jpql);
			return query.getResultList();
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}

	@Override
	public void editAccount(Account account) {
		String jpql = "update Account a set  a.address = ?1, a.age = ?2, a.balance = ?3, a.city = ?4, a.email = ?5, "
				+ "a.employer= ?6, a.firstname = ?7, a.gender = ?8, a.lastname = ?9, a.state = ?10  where a.accountNumber = ?11";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, account.getAddress());
		query.setParameter(2, account.getAge());
		query.setParameter(3, account.getBalance());
		query.setParameter(4, account.getCity());
		query.setParameter(5, account.getEmail());
		query.setParameter(6, account.getEmployer());
		query.setParameter(7, account.getFirstname());
		query.setParameter(8, account.getGender());
		query.setParameter(9, account.getLastname());
		query.setParameter(10, account.getState());
		query.setParameter(11, account.getAccountNumber());
		query.executeUpdate();

	}

	@Override
	public List<Account> getAllByAccount(Account acc) {
		String jpql = "SELECT NEW Account(a.accountNumber, a.address, a.age, a.balance, a.city, a.email, a.employer, a.firstname, a.gender, a.lastname, a.state, a.role) FROM Account AS a WHERE 1=1 ";
		if (acc.getAddress() != null) {
			jpql = jpql.concat(" AND LOWER(a.address) LIKE :add");
		}
		if (acc.getCity() != null) {
			jpql = jpql.concat(" AND LOWER(a.city) LIKE :acity");
		}
		if (acc.getEmail() != null) {
			jpql = jpql.concat(" AND LOWER(a.email) LIKE :aemail");
		}
		if (acc.getFirstname() != null) {
			jpql = jpql.concat(" AND LOWER(a.firstname) LIKE :afirstname");
		}
		if (acc.getLastname() != null) {
			jpql = jpql.concat(" AND LOWER(a.lastname) LIKE :alastname");
		}

		Query query = entityManager.createQuery(jpql);

		if (acc.getAddress() != null) {
			query.setParameter("add", "%" + acc.getAddress().toLowerCase() + "%");
		}
		if (acc.getCity() != null) {
			query.setParameter("acity", "%" + acc.getCity().toLowerCase() + "%");
		}
		if (acc.getEmail() != null) {
			query.setParameter("aemail", "%" + acc.getEmail().toLowerCase() + "%");
		}
		if (acc.getFirstname() != null) {
			query.setParameter("afirstname", "%" + acc.getFirstname().toLowerCase() + "%");
		}
		if (acc.getLastname() != null) {
			query.setParameter("alastname", "%" + acc.getLastname().toLowerCase() + "%");
		}

		return query.getResultList();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    User usr = getUserLogin(username);
		if (usr == null) {
			throw new UsernameNotFoundException("not found");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(usr.getRole()));
		CurrentUser currentUser = new CurrentUser(username, usr.getPassword(), grantedAuthorities);
//		currentUser.setAccountNumber(acc.getAccountNumber());
		currentUser.setFirstName(usr.getUsername());
//		currentUser.setLastName(acc.getLastname());
		return currentUser;

	}

	@Override
	public Account getAccountByEmail(String email) {
		try {
			String jpql = "SELECT a FROM Account a WHERE a.email LIKE :aemail ";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("aemail", email);
			return (Account) query.getSingleResult();
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
	
	
	@Override
        public User getUserLogin(String username) {
                try {
                        String jpql = "SELECT a FROM User a WHERE a.username LIKE :username ";
                        Query query = entityManager.createQuery(jpql);
                        query.setParameter("username", username);
                        return (User) query.getSingleResult();
                } catch (Exception e) {
                        System.out.print(e);
                        return null;
                }
        }

}
