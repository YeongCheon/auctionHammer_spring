package hoseo.b.auctionHammer;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import util.FileUpload;
import bean.AuctionArticle;
import bean.AuctionAttachFile;
import bean.AuctionBid;
import bean.Buy;
import bean.User;

import com.oreilly.servlet.MultipartRequest;

import dao.AuctionArticleDao;
import dao.AuctionAttachFileDao;
import dao.AuctionBidDao;
import dao.UserDao;

@Controller
public class AuctionController {
	@Autowired
	AuctionArticleDao auctionArticleDao;
	@Autowired
	AuctionBidDao AuctionBidDao;
	@Autowired
	AuctionAttachFileDao auctionAttachFileDao;
	@Autowired
	UserDao userDao;
	
	private final int ADJUST = 100; 

	@RequestMapping("/auction/insertAuctionForm.do")
	public ModelAndView insertAuctionForm() {
		return new ModelAndView("/auction/insertAuctionForm");
	}
	
	@RequestMapping("/auction/insertAuction.do")
	public ModelAndView insertAuction(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileUpload fileUpload = new FileUpload(request);
		MultipartRequest multiRequest = fileUpload.getMultipartRequest();
		
		Timestamp startDate = (Timestamp) Timestamp.valueOf(multiRequest.getParameter("article_startdate") + ":00");
		Timestamp endDate = (Timestamp) Timestamp.valueOf(multiRequest.getParameter("article_enddate") + ":00");
		
		String article_seller = (String)request.getSession().getAttribute("userID");
		String article_title = multiRequest.getParameter("article_title");
		int article_type = Integer.parseInt(multiRequest.getParameter("article_type"));
		Timestamp article_startdate = startDate;
		Timestamp article_enddate = endDate;
		int article_marketprice = Integer.parseInt(multiRequest.getParameter("article_marketprice"));
		int article_startprice = Integer.parseInt(multiRequest.getParameter("article_startprice"));
		String article_content =  multiRequest.getParameter("article_content");
		String article_description = multiRequest.getParameter("article_description");
		
		AuctionArticle article = new AuctionArticle();
		article.setArticle_seller(article_seller);
		article.setArticle_title(article_title);
		article.setArticle_type(article_type);
		article.setArticle_startdate(article_startdate);
		article.setArticle_enddate(article_enddate);
		article.setArticle_marketprice(article_marketprice);
		article.setArticle_startprice(article_startprice);
		article.setArticle_content(article_content);
		article.setArticle_description(article_description);
		
		boolean inserted = false;
		if(auctionArticleDao.insertArticle(article) >= 1){
			inserted = true;
		}

		String[] categorys = multiRequest.getParameterValues("product_kind");
		auctionArticleDao.insertAuctionCategory(auctionArticleDao.getRecentAuctionArticleId(article_seller), categorys); //category 추가
		
		try {
			String thumnail = fileUpload.upload();
			auctionAttachFileDao.insertAttachFile(auctionArticleDao.getRecentAuctionArticleId(article_seller), thumnail); //image 경로 추가
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ModelAndView("/auction/insertAuction", "inserted", inserted);
	}

	@RequestMapping("/auction/auctionArticleList.do")
	public ModelAndView auctionArticleList(HttpServletRequest request, HttpServletResponse response) {
		int article_type = AuctionArticleDao.ARTICLE_TYPE_UP;
		if(request.getParameter("article_type") != null){
			article_type = Integer.parseInt(request.getParameter("article_type"));
		}

		ModelAndView mav = new ModelAndView("/auction/auctionArticleList");
		
		List<AuctionArticle> articles = null;
		List<AuctionAttachFile> images = new ArrayList<AuctionAttachFile>();
		String search_type = request.getParameter("search_type");
		String keyword = request.getParameter("searchBox");
		
		if(search_type != null && search_type.equals("keyword")){
			try {
				keyword = new String(request.getParameter("searchBox").getBytes(
						"8859_1"), "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			articles = auctionArticleDao.getAuctionArticleListToKeyword(keyword);
			System.out.println("toKeyword");
		} else if(search_type != null && search_type.equals("category")){
			int category = Integer.parseInt(request.getParameter("category"));
			articles = auctionArticleDao.getAuctionArticleListToCategory(category);
			System.out.println("toCategory");
		} else{
			articles =auctionArticleDao.getAuctionArticleList(article_type);
			System.out.println("default");
		}
		
		for(AuctionArticle article : articles){
			if(AuctionBidDao.getMaximumBid(article.getArticle_id()) != null){
				article.setArticle_startprice(AuctionBidDao.getMaximumBid(article.getArticle_id()).getBid_price());
			}
		}
		

		
		
		for(AuctionArticle article: articles){
			images.add(auctionAttachFileDao.getAttachFile(article.getArticle_id()));
		}
		
		mav.addObject("keyword", keyword);
		mav.addObject("articleList", articles);
		mav.addObject("imageList", images);
		mav.addObject("article_type", article_type);
		mav.addObject("totalCount", auctionArticleDao.getAuctionArticleTotalCount());

		return mav;
	}
	
	@RequestMapping("/auction/auctionArticleDetail.do")
	public ModelAndView auctionArticleDetail(HttpServletRequest request, HttpServletResponse response){
		int article_id = Integer.parseInt(request.getParameter("article_id"));
		AuctionArticle article = auctionArticleDao.getAuctionArticle(article_id);
		AuctionAttachFile thumnail = auctionAttachFileDao.getAttachFile(article_id);
		
		AuctionBid bid_maximum = AuctionBidDao.getMaximumBid(article_id);
		if(bid_maximum != null){
			article.setArticle_startprice(bid_maximum.getBid_price());
		}
		
		
		ModelAndView mav = new ModelAndView("/auction/auctionArticleDetail", "article", article);
		mav.addObject("thumnail", thumnail);
		return mav;
	}
	
	@RequestMapping("/auction/insertBid.do")
	public ModelAndView insertBid(HttpServletRequest request, HttpServletResponse response){
		boolean inserted = false;
		String user_id = (String)request.getSession().getAttribute("userID");
		
		if(user_id == null){ // session 정보 없을 시 fail;
			return new ModelAndView("/auction/insertBid", "inserted", false); 
		}
		
		int article_id = Integer.parseInt(request.getParameter("article_id"));
		
		AuctionBid bid_maximum = AuctionBidDao.getMaximumBid(article_id);
		int insertPrice = 0;
		if(bid_maximum == null){
			insertPrice = auctionArticleDao.getAuctionArticle(article_id).getArticle_startprice();
		} else{
			insertPrice = bid_maximum.getBid_price();
		}
		
		insertPrice += ADJUST;
		
		
		if(AuctionBidDao.insertBid(new AuctionBid(article_id, user_id, insertPrice)) >= 1){
			inserted = true;
		}
		
		ModelAndView mav = new ModelAndView("/auction/insertBid", "inserted", inserted);
		mav.addObject("price", insertPrice);
		
		return mav;
	}
	
	@RequestMapping("/auction/myBidList.do")
	public ModelAndView myBidList(HttpServletRequest request, HttpServletResponse response){
		String user_id = (String)request.getSession().getAttribute("userID");
		return new ModelAndView("/auction/myBidList", "myBidList", auctionArticleDao.getMyBidList(user_id));
	}
	
	@RequestMapping("/auction/myBuyList.do")
	public ModelAndView myBuyList(HttpServletRequest request, HttpServletResponse response){
		String user_id = (String)request.getSession().getAttribute("userID");
		return new ModelAndView("/auction/myBuyList", "myBuyList", auctionArticleDao.getMyBuyList(user_id));
	}	
	
	@RequestMapping("/auction/uploadContentImage.do")
	public ModelAndView uploadContentImage(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("/auction/uploadContentImage");
		FileUpload fileupload = new FileUpload(request);
		String src = null;
		
		try {
			src = fileupload.upload();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.addObject("src", src);
		
		return mav;
	}
	
	@RequestMapping("/auction/insertBuyForm.do")
	public ModelAndView insertBuyForm(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("/auction/insertBuyForm");
		int article_id = Integer.parseInt(request.getParameter("article_id"));
		AuctionArticle article = auctionArticleDao.getAuctionArticle(article_id);
		AuctionAttachFile thumnail = auctionAttachFileDao.getAttachFile(article_id);
		
		AuctionBid bid_maximum = AuctionBidDao.getMaximumBid(article_id);
		if(bid_maximum != null){
			article.setArticle_startprice(bid_maximum.getBid_price());
		}
		User user = userDao.userGet(AuctionBidDao.getMaximumBid(article_id).getBid_user());
		
		mav.addObject("thumnail", thumnail);
		mav.addObject("article",article);
		mav.addObject("user", user);
		return mav;
	}
	@RequestMapping("/auction/insertBuy.do")
	public ModelAndView insertBuy(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView("/auction/insertBuy");
		Buy buy = new Buy();
		boolean inserted = false;
		
		
		buy.setBuy_article_id(Integer.parseInt(request.getParameter("article_id")));
		buy.setUser_id((String)request.getSession().getAttribute("userID"));
		buy.setBuy_zipcode(request.getParameter("user_zipcode01") + "user_zipcode02");
		buy.setAddr1(request.getParameter("user_addr01"));
		buy.setAddr2(request.getParameter("user_addr02"));
		
		if(auctionArticleDao.insertBuy(buy) >= 1){
			inserted = true;
		} else{
			inserted = false;
		}
		
		
		mav.addObject("inserted", inserted);
		
		return mav;
	}
	
}
