package cab.app.rideservice.repository;

import cab.app.rideservice.model.Ride;
import cab.app.rideservice.model.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Optional<Ride> findByIdAndDeletedFalse(Long id);
    Page<Ride> findAllByDeletedFalse(Pageable pageable);
    Page<Ride> findAllByDeletedFalseAndStatus(RideStatus rideStatus, Pageable pageable);
    Page<Ride> findAllByDeletedFalseAndPassengerId(Long driverId,Pageable pageable);
    Page<Ride> findAllByDeletedFalseAndDriverId(Long driverId, Pageable pageable);
}
