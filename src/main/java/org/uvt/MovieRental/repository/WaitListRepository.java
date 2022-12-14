package org.uvt.MovieRental.repository;


import org.uvt.MovieRental.entity.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaitListRepository extends JpaRepository<WaitList, Long> {
    @Query("select w.id from WaitList w where w.user.id = :idUser and w.movie.id = :idMovie")
    Long findIdByIdUserAndIdMovie(Long idUser, Long idMovie);

    @Query("select w from WaitList w where w.date < current_date ")
    List<WaitList> findAllByDateLessThanCurrentDate();

    List<WaitList> findWaitListByUser_Id(Long userId);
}
