package com.example.driverservice.model;

import com.example.driverservice.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drivers_seq_generator")
    @SequenceGenerator(name = "drivers_seq_generator", sequenceName = "drivers_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "average_rating")
    private Double averageRating;
    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;
    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
