package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.Worker;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {

	List<Worker> findAll();
}
