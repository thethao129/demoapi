package com.example.Ecabinet.restfulApi;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Ecabinet.dao.AccountDao;
import com.example.Ecabinet.dto.JwtResponse;
import com.example.Ecabinet.entity.Account;
import com.example.Ecabinet.entity.CurrentUser;
import com.example.Ecabinet.entity.User;
import com.example.Ecabinet.jwt.JWTutil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@Api(value = "onlinestore", description = "Operations pertaining to account in Online Store")
public class AccountApi {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AccountDao accountDao;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JWTutil jwtUtil;

	@ApiOperation(value = "View a list of available products", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully create"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })

	// @ApiOperation(value = "Search a account with an ID",response = Account.class)
	@GetMapping(value = "/api/get/account/{id}", produces = "application/json")
	public ResponseEntity<?> getAccountById(@PathVariable(value = "id") long id) {
		Account account;
		account = accountDao.getAccountById(id);
		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	@PostMapping(value = "/api/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtil.generateToken((CurrentUser) authentication.getPrincipal());
		return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
	}

	@PostMapping(value = "/api/create/account", produces = "application/json")
	public ResponseEntity<Object> createAccount(@RequestBody Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountDao.createAccount(account);
		// Create resource location
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(account.getAccountNumber()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping(value = "/api/getAll/account", produces = "application/json")
	public ResponseEntity<?> getAccountAll() {

		List<Account> account = accountDao.getAll();
		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a product")
	@RequestMapping(value = "/api/delete/account/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable long id) {
		accountDao.deleteAccount(id);
		;
		return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "Update a product")
	@RequestMapping(value = "/api/update/account/{id}", method = RequestMethod.POST, produces = "application/json")
	public Account updateProduct(@PathVariable long id, @RequestBody Account acc) {
		acc.setAccountNumber(id);
		accountDao.editAccount(acc);
		return accountDao.getAccountById(id);
	}

	@PostMapping(value = "/api/getAllByObject/account", produces = "application/json")
	public ResponseEntity<?> getAccountAllByAccount(@RequestBody Account account) {

		List<Account> listAcc = accountDao.getAllByAccount(account);
		return new ResponseEntity<>(listAcc, HttpStatus.OK);
	}

}
