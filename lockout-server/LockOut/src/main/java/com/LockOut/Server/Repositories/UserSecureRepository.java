package com.LockOut.Server.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.User;
import com.LockOut.Server.Models.UsersSecure;

@Repository
public interface UserSecureRepository extends CrudRepository<UsersSecure, Long> {
	UsersSecure findByUserName(String UserName);
	UsersSecure findByid(Integer id);
	List<UsersSecure> findAllByOrderByPointsTotalDesc();
	List<UsersSecure> findAllByOrderBySeasonPointsDesc();
}
