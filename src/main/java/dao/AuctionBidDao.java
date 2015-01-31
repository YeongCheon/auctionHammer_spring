package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bean.AuctionBid;

/*
 * 입찰 DAO
 * 
 * */

public class AuctionBidDao {
	JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void deleteAll() {
		String query = "DELETE FROM ah_goods_article";
		jdbcTemplate.update(query);
	}

	public int insertBid(AuctionBid bid) {
		String query = "INSERT INTO ah_goods_bid(bid_article_id, bid_user, bid_price, bid_date) VALUES(?, ?, ?, NOW())";
		return jdbcTemplate.update(query, bid.getBid_article_id(),
				bid.getBid_user(), bid.getBid_price());
	}

	public AuctionBid getMaximumBid(int article_id) {
		String query = "SELECT bid_id, bid_article_id, bid_user, bid_price, bid_date"
				+ " FROM ah_goods_bid WHERE bid_article_id = ? AND "
				+ "bid_price = (SELECT MAX(bid_price) FROM ah_goods_bid WHERE bid_article_id = ?)";
		try {
			return jdbcTemplate.queryForObject(query, new Object[] {
					article_id, article_id }, new RowMapper<AuctionBid>() {

				@Override
				public AuctionBid mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					// TODO Auto-generated method stub
					AuctionBid bid = new AuctionBid();
					bid.setBid_id(rs.getInt("bid_id"));
					bid.setBid_article_id(rs.getInt("bid_article_id"));
					bid.setBid_price(rs.getInt("bid_price"));
					bid.setBid_user(rs.getString("bid_user"));
					bid.setBid_date(rs.getTimestamp("bid_date"));

					return bid;
				}

			});
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public AuctionBid getMinimumBid(int article_id) {
		String query = "SELECT bid_id, bid_article_id, bid_user, bid_price, bid_date"
				+ " FROM ah_goods_bid WHERE bid_article_id = ? AND "
				+ "bid_price = (SELECT MIN(bid_price) FROM ah_goods_bid WHERE bid_article_id = ?)";
		return jdbcTemplate.queryForObject(query, new Object[] { article_id,
				article_id }, new RowMapper<AuctionBid>() {

			@Override
			public AuctionBid mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				// TODO Auto-generated method stub
				AuctionBid bid = new AuctionBid();
				bid.setBid_id(rs.getInt("bid_id"));
				bid.setBid_article_id(rs.getInt("bid_article_id"));
				bid.setBid_price(rs.getInt("bid_price"));
				bid.setBid_user(rs.getString("bid_user"));
				bid.setBid_date(rs.getTimestamp("bid_date"));

				return bid;
			}

		});
	}
}
