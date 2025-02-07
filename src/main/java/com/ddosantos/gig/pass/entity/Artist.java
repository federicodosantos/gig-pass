package com.ddosantos.gig.pass.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "artists")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Artist {
    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                         CascadeType.REFRESH})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // relationship
    @OneToMany(mappedBy = "artist",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.REFRESH})
    private List<Concert> concerts;
}
