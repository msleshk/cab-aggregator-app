package cab.app.paymentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "passenger_balance")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerBalance {
    @Id
    @Column(name = "passenger_id")
    private Long passengerId;
    @Column(name = "balance")
    private BigDecimal balance;
}
