package com.ddosantos.gig.pass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tickets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "concert_id")
    private  Concert concert;

    @Column(name = "price")
    private int price;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "stock")
    private int stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate updatedAt;

    // relationship
    @OneToMany(mappedBy = "ticket",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH})
    private List<Order> orders;
}
