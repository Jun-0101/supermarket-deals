package com.example.supermarket_deals.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supermarkets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supermarket {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
