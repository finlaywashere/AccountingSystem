package xyz.finlaym.accounting.subsystem.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xyz.finlaym.accounting.persistance.auth.Account;
import xyz.finlaym.accounting.persistance.auth.AccountService;

@RestController
public class AuthController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestParam(value = "username") String username,
						@RequestParam(value = "password") String password) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));
		Account user = accountService.getAccount(username).orElseThrow();
		String jwt = jwtService.generateToken(user);
		Map<String, String> response = new HashMap<String, String>();
		response.put("status", "ok");
		response.put("token", jwt);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestParam(value = "email") String email,
						   @RequestParam(value = "username") String username,
						   @RequestParam(value = "password") String password) {
		Optional<Account> account = accountService.getAccount(username);
		Map<String, String> status = new HashMap<String, String>();
		if(!account.isEmpty()) {
			status.put("status", "already_exists");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern pattern = Pattern.compile(ePattern);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()) {
			status.put("status", "bad_email");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		if(password.length() < 8) {
			status.put("status", "bad_password");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		if(username.length() < 2) {
			status.put("status", "bad_username");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		Account acct = new Account(username, email);
		acct.setHash(passwordEncoder.encode(password));
		accountService.saveAccount(acct);
		status.put("status", "created");
		return new ResponseEntity<>(status, HttpStatus.CREATED);
	}
	
	@GetMapping("/auth/me")
	public ResponseEntity<?> me(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account acct = (Account) authentication.getPrincipal();
		return new ResponseEntity<>(acct, HttpStatus.OK);
	}
}
