package org.uvt.MovieRental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.uvt.MovieRental.entity.MovieInfo;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private MovieInfo info;
    private LocalDate returnDate;
    private String userName;
    private Boolean available;
}
