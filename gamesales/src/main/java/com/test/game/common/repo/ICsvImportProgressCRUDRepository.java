package com.test.game.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.game.common.view.CsvImportProgress;

@Repository
public interface ICsvImportProgressCRUDRepository extends JpaRepository<CsvImportProgress, Integer>{

}
