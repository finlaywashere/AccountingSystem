package xyz.finlaym.accounting.persistance.cashtrack;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashLocationRepository extends CrudRepository<CashLocation, String>{

}
