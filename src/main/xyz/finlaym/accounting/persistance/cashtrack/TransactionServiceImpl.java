package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.finlaym.accounting.persistance.auth.Account;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionRepository repository;
	
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		return repository.save(transaction);
	}

	@Override
	public List<Transaction> fetchByUser(Account account) {
		return (List<Transaction>) repository.findByUser(account);
	}

	@Override
	public List<Transaction> fetchByFrom(CashLocation from) {
		return (List<Transaction>) repository.findByFrom(from);
	}

	@Override
	public List<Transaction> fetchByTo(CashLocation to) {
		return (List<Transaction>) repository.findByFrom(to);
	}

	@Override
	public Optional<Transaction> getTransaction(long id) {
		return repository.findById(id);
	}

}
