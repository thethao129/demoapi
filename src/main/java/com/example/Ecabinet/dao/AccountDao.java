package com.example.Ecabinet.dao;

import java.util.List;

import com.example.Ecabinet.entity.Account;
import com.example.Ecabinet.entity.User;

public interface AccountDao {
	public void createAccount(Account account);
	public void editAccount(Account account);
	public void deleteAccount(long id);
	public Account getAccountById(long id);
	public List<Account> getAll();
	public List<Account> getAllByAccount(Account acc);
	public Account getAccountByEmail(String email);
	public User getUserLogin(String username);
}
