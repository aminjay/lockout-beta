package com.LockOut.Server.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.User;

//needed for all tables, put the model in where user is and keep Long 
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	User findByUserName(String UserName);
	List<User> findByPassword(String Password);
	User findByid(Integer id);
	List<User> findAllByOrderByPointsTotalDesc();
	List<User> findAllByOrderBySeasonPointsDesc();
	
}


