package com.digital.customer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_number_gen")
    @SequenceGenerator(
            name = "account_number_gen",
            sequenceName = "account_number_seq",
            allocationSize = 1
    )
    private Long accountNumber;

    private Long customerNumber;
}
