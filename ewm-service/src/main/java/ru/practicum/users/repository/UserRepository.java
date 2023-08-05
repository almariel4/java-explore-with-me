package ru.practicum.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //    @Query("select u from User u " +
//            "where u.id in ?1 ")
//    List<User> getUsersByIds(int[] ids, Pageable pageable);
    List<User> getAllByIdIn(int[] ids, Pageable pageable);


}
