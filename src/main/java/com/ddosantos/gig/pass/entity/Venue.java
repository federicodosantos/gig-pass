package com.ddosantos.gig.pass.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "venues")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Venue {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private int capacity;

    // relationship
    @OneToMany(mappedBy = "venue",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH})
    private List<Concert> concerts;
}
