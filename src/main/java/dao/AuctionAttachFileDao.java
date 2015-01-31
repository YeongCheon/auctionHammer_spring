package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bean.AuctionAttachFile;

public class AuctionAttachFileDao {
	JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int insertAttachFile(int article_id, String src) {
		String query = "INSERT INTO ah_goods_attachfile(article_id, attachfile_src) VALUES(?, ?) ";
		return jdbcTemplate.update(query, article_id, src);
	}
	
	public AuctionAttachFile getAttachFile(int article_id){
		String query = "SELECT attachfile_id, attachfile_src, article_id FROM ah_goods_attachfile WHERE article_id = ?";
		try{
		return jdbcTemplate.queryForObject(query, new Object[]{article_id}, new RowMapper<AuctionAttachFile>(){

			@Override
			public AuctionAttachFile mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				AuctionAttachFile attachfile = new AuctionAttachFile();
				
				attachfile.setAttachfile_id(rs.getInt("attachfile_id"));
				attachfile.setArticle_id(rs.getInt("attachfile_id"));
					attachfile.setAttachfile_src(rs.getString("attachfile_src"));
				
				return attachfile;
			}
		});
		} catch(Exception ex){
			return null;
		}
	}
}
