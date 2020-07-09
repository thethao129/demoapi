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

@Repository
@Transactional
public class AccountDaoImpl implements AccountDao,UserDetailsService{
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void createAccount(Account account) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		try {
			String jpql = "SELECT NEW Account(a.accountNumber, a.address, a.age, a.balance, a.city, a.email, a.employer, a.firstname, a.gender, a.lastname, a.state)  FROM Account AS a WHERE a.accountNumber LIKE :aid ";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("aid",  id );
			return (Account)query.getSingleResult();
		}catch (Exception e) {
			// TODO: handle exception
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
		}catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return null;
		}
	}

	@Override
	public void editAccount(Account account) {
		// TODO Auto-generated method stub
		entityManager.merge(account);
	}

	@Override
	public List<Account> getAllByAccount(Account acc) {
		String jpql = "SELECT NEW Account(a.accountNumber, a.address, a.age, a.balance, a.city, a.email, a.employer, a.firstname, a.gender, a.lastname, a.state) FROM Account AS a WHERE 1=1 ";
		if(acc.getAddress() !=null) {
			jpql=jpql.concat(" AND LOWER(a.address) LIKE :add");
		}
		if(acc.getCity()!=null){
			jpql=jpql.concat(" AND LOWER(a.city) LIKE :acity");
		}
		if(acc.getEmail()!=null) {
			jpql=jpql.concat(" AND LOWER(a.email) LIKE :aemail");
		}
		if(acc.getFirstname()!=null) {
			jpql=jpql.concat(" AND LOWER(a.firstname) LIKE :afirstname");
		}
		if(acc.getLastname()!=null) {
			jpql=jpql.concat(" AND LOWER(a.lastname) LIKE :alastname");
		}
		
		Query query = entityManager.createQuery(jpql);
		
		if(acc.getAddress() !=null) {
			query.setParameter("add", "%"+acc.getAddress().toLowerCase()+"%");
		}
		if(acc.getCity()!=null){
			query.setParameter("acity", "%"+acc.getCity().toLowerCase()+"%");
		}
		if(acc.getEmail()!=null) {
			query.setParameter("aemail", "%"+acc.getEmail().toLowerCase()+"%");
		}
		if(acc.getFirstname()!=null) {
			query.setParameter("afirstname", "%"+acc.getFirstname().toLowerCase()+"%");
		}
		if(acc.getLastname()!=null) {
			query.setParameter("alastname", "%"+acc.getLastname().toLowerCase()+"%");
		}
		
		return query.getResultList();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account acc = getAllByEmail(username);
		if(acc==null) {
			throw new UsernameNotFoundException("not found");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		 grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        CurrentUser currentUser=new CurrentUser(username, acc.getPassword(), grantedAuthorities);
        currentUser.setAccountNumber(acc.getAccountNumber());
		currentUser.setFirstName(acc.getFirstname());
		currentUser.setLastName(acc.getLastname());
		return  currentUser;
		
	}

	@Override
	public Account getAllByEmail(String email) {
		try {
			String jpql = "SELECT a FROM Account a WHERE a.email LIKE :aemail ";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("aemail",  email );
			return (Account) query.getSingleResult();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return null;
		}
	}

	
	

}