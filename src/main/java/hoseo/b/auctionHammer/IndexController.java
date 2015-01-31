package hoseo.b.auctionHammer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bean.AuctionArticle;
import bean.AuctionAttachFile;
import dao.AuctionArticleDao;
import dao.AuctionAttachFileDao;
import dao.AuctionBidDao;

@Controller
public class IndexController {
	@Autowired
	AuctionArticleDao auctionArticleDao;
	@Autowired
	AuctionBidDao AuctionBidDao;
	@Autowired
	AuctionAttachFileDao auctionAttachFileDao;	

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/index.do")      
    public ModelAndView index() {             
		ModelAndView mav = new ModelAndView("/index");
		
		List<AuctionArticle> articles_up =auctionArticleDao.getAuctionArticleListToUp();
		List<AuctionAttachFile> images_up = new ArrayList<AuctionAttachFile>();
		
		List<AuctionArticle> articles_down =auctionArticleDao.getAuctionArticleListToDown();
		List<AuctionAttachFile> images_down = new ArrayList<AuctionAttachFile>();
		
		List<AuctionArticle> articles_private =auctionArticleDao.getAuctionArticleListToPrivate();
		List<AuctionAttachFile> images_private = new ArrayList<AuctionAttachFile>();
		
		for(AuctionArticle article : articles_up){
			if(AuctionBidDao.getMaximumBid(article.getArticle_id()) != null){
				article.setArticle_startprice(AuctionBidDao.getMaximumBid(article.getArticle_id()).getBid_price());
			}
		}
		for(AuctionArticle article: articles_up){
			images_up.add(auctionAttachFileDao.getAttachFile(article.getArticle_id()));
		}
		
		
		
		for(AuctionArticle article : articles_down){
			if(AuctionBidDao.getMaximumBid(article.getArticle_id()) != null){
				article.setArticle_startprice(AuctionBidDao.getMaximumBid(article.getArticle_id()).getBid_price());
			}
		}
		for(AuctionArticle article: articles_down){
			images_down.add(auctionAttachFileDao.getAttachFile(article.getArticle_id()));
		}
		
		
		
		for(AuctionArticle article : articles_private){
			if(AuctionBidDao.getMaximumBid(article.getArticle_id()) != null){
				article.setArticle_startprice(AuctionBidDao.getMaximumBid(article.getArticle_id()).getBid_price());
			}
		}
		for(AuctionArticle article: articles_private){
			images_private.add(auctionAttachFileDao.getAttachFile(article.getArticle_id()));
		}				
		
		mav.addObject("articleList_up", articles_up);
		mav.addObject("imageList_up", images_up);
		mav.addObject("articleList_down", articles_down);
		mav.addObject("imageList_down", images_down);
		mav.addObject("articleList_private", articles_private);
		mav.addObject("imageList_private", images_private);		
		
		mav.addObject("totalCount", auctionArticleDao.getAuctionArticleTotalCount());

		return mav;       
    }
    
    @RequestMapping("/guide.do")      
    public ModelAndView guide() {             
		ModelAndView mav = new ModelAndView("/guide");
		
		return  mav;
    }
    @RequestMapping("/event.do")      
    public ModelAndView event() {             
		ModelAndView mav = new ModelAndView("/event");
		
		return  mav;
    }
}
