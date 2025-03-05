package xyz.finlaym.accounting.persistance.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public List<Account> fetchAccountList() {
		return (List<Account>) accountRepository.findAll();
	}

	@Override
	public Optional<Account> getAccount(String username) {
		return accountRepository.findById(username);
	}

	@Override
	public void deleteAccount(String username) {
		accountRepository.deleteById(username);
	}
	
}
