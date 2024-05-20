package com.test.game.common.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.test.game.common.view.GameView;

@Repository
public class GameSalesBatchInsertDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void performBatchInsert(List<GameView> gameEntities) {
		String sql = "INSERT INTO game_sales (id, game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				GameView entity = gameEntities.get(i);
				ps.setString(1, String.valueOf(entity.getId()));
				ps.setString(2, String.valueOf(entity.getGameNo()));
				ps.setString(3, entity.getGameName());
				ps.setString(4, entity.getGameCode());
				ps.setString(5, String.valueOf(entity.getType()));
				ps.setString(6, String.valueOf(entity.getCostPrice()));
				ps.setString(7, String.valueOf(entity.getTax()));
				ps.setString(8, String.valueOf(entity.getSalePrice()));
				ps.setString(9, String.valueOf(entity.getDateOfSale()));
			}
			
			@Override
			public int getBatchSize() {
				return gameEntities.size();
			}
		});
	}
}
