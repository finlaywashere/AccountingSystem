package xyz.finlaym.accounting.subsystem.cashtrack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xyz.finlaym.accounting.persistance.auth.Account;
import xyz.finlaym.accounting.persistance.auth.AccountService;
import xyz.finlaym.accounting.persistance.cashtrack.CashLocation;
import xyz.finlaym.accounting.persistance.cashtrack.CashLocationService;
import xyz.finlaym.accounting.persistance.cashtrack.Transaction;
import xyz.finlaym.accounting.persistance.cashtrack.TransactionService;

@Controller
public class CashController {
	@Autowired
	private CashLocationService cashLocService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	@PreAuthorize("hasAuthority('WRITE_CASH')")
	@PutMapping("/cash/location/{name}")
	public ResponseEntity<?> createLocation(@PathVariable("name") String name,
											@RequestParam(value = "location") String location,
											@RequestParam(value = "virtual") Optional<Boolean> virtual){
		Optional<CashLocation> cash = cashLocService.getLocation(name);
		Map<String, String> status = new HashMap<String, String>();
		if(!cash.isEmpty()) {
			status.put("status", "already_exists");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		boolean virt = false;
		if(virtual.isPresent())
			virt = virtual.get();
		CashLocation loc = new CashLocation(name, location, virt);
		cashLocService.saveLocation(loc);
		status.put("status", "created");
		return new ResponseEntity<>(status, HttpStatus.CREATED);
	}
	
	@GetMapping("/cash/location/{name}")
	public ResponseEntity<?> getLocation(@PathVariable("name") String name){
		Optional<CashLocation> cash = cashLocService.getLocation(name);
		Map<String, String> status = new HashMap<String, String>();
		if(cash.isEmpty()) {
			status.put("status", "not_found");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(cash.get(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('WRITE_CASH')")
	@PutMapping("/cash/transaction")
	public ResponseEntity<?> createTransaction(@RequestParam(value = "amount") int amount,
											   @RequestParam(value = "to") String to,
											   @RequestParam(value = "from") String from){
		Account account = accountService.getAccount(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Optional<CashLocation> toLoc = cashLocService.getLocation(to);
		Optional<CashLocation> fromLoc = cashLocService.getLocation(from);
		Map<String, Object> response = new HashMap<String, Object>();
		if(toLoc.isEmpty()) {
			response.put("status", "bad_to");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if(fromLoc.isEmpty()) {
			response.put("status", "bad_from");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if(to.equals(from)) {
			response.put("status", "to_equals_from");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if(amount <= 0) {
			response.put("status", "bad_amount");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		CashLocation toCL = toLoc.get();
		CashLocation fromCL = fromLoc.get();
		if(!fromCL.isVirtual() && fromCL.getAmount() < amount) {
			response.put("status", "insufficient_funds");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		int oldFromBal = fromCL.getAmount();
		int oldToBal = fromCL.getAmount();
		
		fromCL.setAmount(fromCL.getAmount() - amount);
		toCL.setAmount(toCL.getAmount() + amount);
		fromCL = cashLocService.saveLocation(fromCL);
		toCL = cashLocService.saveLocation(toCL);
		Transaction transaction = new Transaction(account, fromCL, toCL, amount);
		transaction = transactionService.saveTransaction(transaction);
		
		response.put("status", "success");
		response.put("id", transaction.getId());
		response.put("old_from", oldFromBal);
		response.put("old_to", oldToBal);
		response.put("new_from", fromCL.getAmount());
		response.put("new_to", toCL.getAmount());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
