package com.debski.simplefileserver.repositories;

import com.debski.simplefileserver.entities.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
}
