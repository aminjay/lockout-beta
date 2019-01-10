package com.LockOut.Server.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.LockOut.Server.Models.Reward;

public interface StoreRepository extends CrudRepository<Reward, Long> {
	Reward findByItemName(String itemName);
	Iterable <Reward> findByGroupID(int GroupID);
	Reward findByItemNameAndGroupID(String itemName, int GroupID);
	Reward findById(int id);
}
