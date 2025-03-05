package xyz.finlaym.accounting.persistance.auth;

import java.util.List;
import java.util.Optional;

public interface AccountService {
	Account saveAccount(Account account);
	List<Account> fetchAccountList();
	Optional<Account> getAccount(String username);
	void deleteAccount(String username);
}
