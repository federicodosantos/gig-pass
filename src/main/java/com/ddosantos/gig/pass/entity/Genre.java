package com.ddosantos.gig.pass.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "genres")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    // relationship
    @OneToMany(mappedBy = "genre",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.REFRESH})
    private List<Artist> artists;
}
