package org.uvt.MovieRental.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    private User owner;

    @ManyToOne(optional = false)
    private MovieInfo info;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Rent> rentedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<WaitList> waitedBy;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + getId() +
                ", owner=" + getOwner() +
                ", info=" + getInfo() +
                '}';
    }
}
