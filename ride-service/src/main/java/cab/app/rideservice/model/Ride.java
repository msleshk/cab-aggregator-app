package cab.app.rideservice.model;

import cab.app.rideservice.model.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_seq")
    @SequenceGenerator(name = "ride_seq", sequenceName = "rides_seq", allocationSize = 1)
    private Long id;
    @Column(name = "driver_id")
    private Long driverId;
    @Column(name = "passenger_id")
    private Long passengerId;
    @Column(name = "departure_address")
    private String departureAddress;
    @Column(name = "arrival_address")
    private String arrivalAddress;
    @Column(name = "distance")
    private BigDecimal distance;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RideStatus status;
    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
