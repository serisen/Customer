package com.digital.customer.entity;

import com.digital.customer.util.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_number_gen")
    @SequenceGenerator(
            name = "customer_number_gen",
            sequenceName = "customer_number_seq",
            allocationSize = 1
    )
    private Long customerNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String nationality;

    @Column
    private String residentialAddress;

    @Column
    private String socialSecurityNumber;

    @Column
    private String idPhotoKey;

    @Column
    private String photoKey;

    @Column
    private String onboardingStatus;

}
