package com.test.game.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.game.common.view.GameView;

@Repository
public interface IGameRepository extends JpaRepository<GameView, Integer>{
	
}
