package xyz.finlaym.accounting.subsystem.generic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericController {
	@GetMapping("/error")
	public String error() {
		return "An error occurred";
	}
	
	@GetMapping("/")
	public String index() {
		return "Index";
	}
}
