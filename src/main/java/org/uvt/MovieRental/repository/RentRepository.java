package org.uvt.MovieRental.repository;

import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("select r from Rent r where r.user.id = :userId and r.movie.id = :movieId")
    Rent findRentByUserIdAndMovieId(Long userId, Long movieId);

    @Query("select r from Rent r where r.movie.owner.id = :id and r.endDate >= current_date ")
    List<Rent> findMoviesReturnToOwner(Long id);

    @Query("select r.movie from Rent r where r.user.id = :userId and r.endDate >= current_date")
    List<Movie> findMoviesRentedByUserId(Long userId);

    @Query("select case when (count(r) > 0)  then true else false end from Rent r where r.movie.id = :id and r.endDate >= current_date")
    Boolean findIfMovieIsRented(Long id);

    @Query("select r from Rent r where r.endDate >= current_date ")
    List<Rent> findActiveRents();

    @Query("select r from Rent r where r.user.id = :userId and r.endDate >= current_date ")
    List<Rent> findActiveRentsByUserId(Long userId);

    @Query("select max(r.endDate) from Rent r where r.movie.id= :movieId")
    LocalDate findLastEndDateMovieWasRented(Long movieId);
}
