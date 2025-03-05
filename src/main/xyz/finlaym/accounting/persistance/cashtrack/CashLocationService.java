package xyz.finlaym.accounting.persistance.cashtrack;

import java.util.List;
import java.util.Optional;

public interface CashLocationService {
	CashLocation saveLocation(CashLocation location);
	List<CashLocation> fetchAccountList();
	Optional<CashLocation> getLocation(String name);
	void deleteLocation(String name);
}
