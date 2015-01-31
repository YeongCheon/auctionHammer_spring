package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bean.LandnumZipcode;


/*
 * author : YeongCheon Kim
 * email : kyc1682@gmail.com
 * 
 * */
public class LandnumZipcodeDao{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<LandnumZipcode> getZipcodeList(String keyword) {
		List<LandnumZipcode> result = null;

		String query = "SELECT * FROM ah_zipcode_landnum WHERE CONCAT(gugun, ' ', dong, ' ', bunji) LIKE CONCAT('%', ? , '%') ORDER BY zipcode";
		result = jdbcTemplate.query(query, new Object[]{keyword},
				new RowMapper<LandnumZipcode>() {
					@Override
					public LandnumZipcode mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						LandnumZipcode result = new LandnumZipcode();
						result.setZipcode(rs.getString("zipcode"));
						result.setSido(rs.getString("sido"));
						result.setGugun(rs.getString("gugun"));
						result.setDong(rs.getString("dong"));
						result.setBunji(rs.getString("bunji"));
						result.setSeq(rs.getString("seq"));

						return result;
					}
				});

		return result;
	}

}
