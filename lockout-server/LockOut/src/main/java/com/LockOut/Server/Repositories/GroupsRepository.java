package com.LockOut.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.Groups;

//Author: Josh
@Repository
public interface GroupsRepository extends CrudRepository<Groups, Long> {
	public Groups findByGroupName(String GroupName);
}
