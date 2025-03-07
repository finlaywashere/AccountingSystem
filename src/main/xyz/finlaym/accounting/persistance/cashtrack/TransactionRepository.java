package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.finlaym.accounting.persistance.auth.Account;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>{
	List<Transaction> findByUser(Account account);
	List<Transaction> findByTo(CashLocation loc);
	List<Transaction> findByFrom(CashLocation loc);
	
}
