package cab.app.paymentservice.repository;

import cab.app.paymentservice.model.DriverBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverBalanceRepository extends JpaRepository<DriverBalance, Long> {
}
