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
public class MovieInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String genre;


    @OneToMany(mappedBy = "info")
    @JsonIgnore
    private List<Movie> moviesList;

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", genre='" + getGenre() + '\'' +
                '}';
    }
}
