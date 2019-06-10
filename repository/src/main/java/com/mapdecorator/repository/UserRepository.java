package com.mapdecorator.repository;

import com.mapdecorator.repository.db.UserObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserObject,Long>{

    @Query("select u from UserObject u where u.userName = ?1")
    Optional<UserObject> getUserByName(String userName);
}
