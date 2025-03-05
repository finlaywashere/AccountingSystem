package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashLocationServiceImpl implements CashLocationService{
	@Autowired
	private CashLocationRepository repository;

	@Override
	public CashLocation saveLocation(CashLocation location) {
		return repository.save(location);
	}

	@Override
	public List<CashLocation> fetchAccountList() {
		return (List<CashLocation>) repository.findAll();
	}

	@Override
	public Optional<CashLocation> getLocation(String name) {
		return repository.findById(name);
	}

	@Override
	public void deleteLocation(String name) {
		repository.deleteById(name);
	}
	
}
