package com.LockOut.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.FriendRequest;


@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
	FriendRequest findById(int id);
}
