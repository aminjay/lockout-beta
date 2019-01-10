package com.LockOut.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.LockOut.Server.Models.Bundle;

@Repository
public interface BundleRepository extends  CrudRepository<Bundle, Long> {

}
