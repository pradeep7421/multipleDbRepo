package com.multipledb.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multipledb.userModel.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

}
