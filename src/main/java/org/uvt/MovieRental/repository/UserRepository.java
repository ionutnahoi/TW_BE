package org.uvt.MovieRental.repository;

import org.uvt.MovieRental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByNameOrEmail(String name, String email);

    User findUserByEmail(String email);
}
