package cab.app.rideservice.repository;

import cab.app.rideservice.model.Ride;
import cab.app.rideservice.model.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Optional<Ride> findByIdAndDeletedFalse(Long id);
    List<Ride> findAllByDeletedFalse();
    List<Ride> findAllByDeletedFalseAndStatus(RideStatus rideStatus);
    List<Ride> findAllByDeletedFalseAndPassengerId(Long driverId);
    List<Ride> findAllByDeletedFalseAndDriverId(Long driverId);
}
