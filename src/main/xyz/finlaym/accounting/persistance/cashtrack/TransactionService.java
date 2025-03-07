package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.List;
import java.util.Optional;

import xyz.finlaym.accounting.persistance.auth.Account;

public interface TransactionService {
	Transaction saveTransaction(Transaction transaction);
	List<Transaction> fetchByUser(Account account);
	List<Transaction> fetchByFrom(CashLocation from);
	List<Transaction> fetchByTo(CashLocation to);
	Optional<Transaction> getTransaction(long id);
}
