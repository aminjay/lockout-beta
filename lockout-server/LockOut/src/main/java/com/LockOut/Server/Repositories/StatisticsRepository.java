package com.LockOut.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.Statistic;
import com.LockOut.Server.Models.User;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistic, Long> {

	User findByid(Integer id);
}
