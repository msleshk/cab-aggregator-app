package cab.app.paymentservice.repository;

import cab.app.paymentservice.model.PassengerBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerBalanceRepository extends JpaRepository<PassengerBalance, Long> {
}
