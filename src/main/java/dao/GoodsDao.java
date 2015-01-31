package dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import bean.Goods;

public class GoodsDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int insertGoods(Goods goods, int[] categorys) {
		String query_goods = "INSERT ah_goods_article("
				+ "article_seller, article_startdate, article_enddate, "
				+ "article_marketprice, article_realprice, article_title, "
				+ "article_content, article_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		return jdbcTemplate.update(query_goods, goods.getArticle_seller(),
				goods.getArticle_startdate().toString(), goods.getArticle_enddate().toString(),
				Integer.toString(goods.getArticle_marketprice()), Integer.toString(goods.getArticle_realprice()),
				goods.getArticle_title(), goods.getArticle_content(),
				Integer.toString((goods.getArticle_type())));
	}
	
	
}
