package xyz.finlaym.accounting.subsystem.cashtrack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xyz.finlaym.accounting.persistance.cashtrack.CashLocation;
import xyz.finlaym.accounting.persistance.cashtrack.CashLocationService;

@Controller
public class CashController {
	@Autowired
	private CashLocationService cashLocService;
	
	@PreAuthorize("hasAuthority('WRITE_CASH')")
	@PutMapping("/cash/location/{name}")
	public ResponseEntity<?> createLocation(@PathVariable("name") String name,
											@RequestParam(value = "location") String location){
		Optional<CashLocation> cash = cashLocService.getLocation(name);
		Map<String, String> status = new HashMap<String, String>();
		if(!cash.isEmpty()) {
			status.put("status", "already_exists");
			return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
		}
		CashLocation loc = new CashLocation(name, location);
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
}
