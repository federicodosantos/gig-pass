package com.ddosantos.gig.pass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "concerts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(name="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate updatedAt;

    // relationship
    @OneToMany(mappedBy = "concert",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH})
    private List<Ticket> tickets;
}
