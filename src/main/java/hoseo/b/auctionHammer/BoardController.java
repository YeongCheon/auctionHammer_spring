package hoseo.b.auctionHammer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import util.FileUpload;
import bean.BoardArticle;
import bean.BoardComment;
import bean.User;
import dao.BoardArticleDao;
import dao.BoardCommentDao;
import dao.UserDao;

@Controller
public class BoardController {
	@Autowired
	BoardArticleDao boardArticleDao;
	@Autowired
	BoardCommentDao commentDao;
	@Autowired
	UserDao userDao;
	
    @RequestMapping("/Board/articleList.do")      
    public ModelAndView articleList(HttpServletRequest request, HttpServletResponse response) throws IOException {     
    	ModelAndView mav =  new ModelAndView("Board/articleList");
    	
     	util.PageNumber pageNumber = new util.PageNumber();

     	String keyword = request.getParameter("keyword");
     	
     	if(keyword != null){
     		keyword = new String(request.getParameter("keyword").getBytes(
    				"8859_1"), "UTF-8");
     	}
     	int search_type = BoardArticleDao.SEARCH_TYPE_DEFAULT;
     	
     	if(request.getParameter("search_type") != null){
     		search_type = Integer.parseInt(request.getParameter("search_type"));
     	}
     	

     	int showRecordCnt = 20;
     	int pageNum = 1;
     	if (request.getParameter("pageNum") != null) {
     		pageNum = Integer.parseInt(request.getParameter("pageNum"));
     	}

     	String pages[] = pageNumber.createPageNumber(10, pageNum, boardArticleDao.getArticleCount(1, search_type, keyword), showRecordCnt,	"articleList.do");

     	List<BoardArticle> articles = boardArticleDao.getArticleList(1, search_type, keyword, pageNum, 	showRecordCnt);
     	ArrayList<Integer> replyCount = new ArrayList<Integer>();
     	
     	for(BoardArticle article : articles){
        	replyCount.add(commentDao.getreplyCount(article.getId()));
        	if(keyword != null && (search_type == BoardArticleDao.SEARCH_TYPE_TITLE || search_type == BoardArticleDao.SEARCH_TYPE_TITLE_AND_CONTENT)){
        		String keywordForReg = ESAPI.encoder().encodeForHTML(keyword);
        		article.setTitle(article.getTitle().replaceAll(keywordForReg, "<span style='background:yellow;margin:0;padding:0'>"+keyword+"</span>"));
        	}
     	}
    	
     	mav.addObject("articleList", articles);
     	mav.addObject("replyCount", replyCount);
     	mav.addObject("pages", pages);
     	mav.addObject("keyword", keyword);
    	
       return mav;       
    }
    
    @RequestMapping("/Board/articleDetail.do")
    public ModelAndView articleDetail(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav =  new ModelAndView("Board/articleDetail");
    	
    	int articleId = Integer
    			.parseInt(request.getParameter("article_id"));

    	boardArticleDao.updateArticleHit(articleId);
    	BoardArticle article = boardArticleDao.getArticle(articleId);
    	List<BoardComment> comments = commentDao.getAllCommentList(articleId);
    	
    	mav.addObject("article",article);
    	mav.addObject("comments",comments);
    	
    	if(article.getWriter().equals((String)request.getSession().getAttribute("userID"))){
    		mav.addObject("isWriter",true);
    	} else{
    		mav.addObject("isWriter",false);
    	}
    	
    	return mav;
    }
    
    @RequestMapping("/Board/insertRereplyComment.do")
    public ModelAndView insertRereplyComment(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/insertComment");
    	
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String article_id = ESAPI.encoder().encodeForHTML(request.getParameter("article_id"));
    	String comment_writer = ESAPI.encoder().encodeForHTML((String)request.getSession().getAttribute("userID"));
    	String comment_memo = ESAPI.encoder().encodeForHTML(request.getParameter("comment_memo"));
    	String comment_ip = ESAPI.encoder().encodeForHTML(request.getRemoteAddr());
    	String comment_rereply = ESAPI.encoder().encodeForHTML(request.getParameter("comment_rereply"));
    	
    	BoardComment comment = new BoardComment();
    	
    	comment.setArticle_id(Integer.parseInt(article_id));
    	comment.setComment_writer(comment_writer);
    	comment.setComment_memo(comment_memo);
    	comment.setComment_ip(comment_ip);
    	comment.setComment_rereplay(Integer.parseInt(comment_rereply));

    	mav.addObject("article_id", article_id);
    	if(commentDao.insertRereplayComment(comment)> 1){
    		mav.addObject("inserted", true);
    	}else{
    		mav.addObject("inserted", false);
    	}
    	
    	return mav;
    }

    @RequestMapping("/Board/insertComment.do")
    public ModelAndView insertComment(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/insertComment");
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    	BoardComment comment = new BoardComment();
    	
    	comment.setArticle_id(Integer.parseInt(request
    			.getParameter("article_id")));
    	comment.setComment_writer(ESAPI.encoder().encodeForHTML((String)request.getSession().getAttribute("userID")));
    	comment.setComment_memo(ESAPI.encoder().encodeForHTML(request.getParameter("memo")));

    	if(commentDao.insertComment(comment) > 1){
    		mav.addObject("inserted", true);
    	} else{
    		mav.addObject("inserted", false);
    	}
    	mav.addObject("article_id",request.getParameter("article_id"));
    	return mav;
    }
    
    
    @RequestMapping("/Board/deleteArticle.do")
    public ModelAndView deleteArticle(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/deleteArticle");
    	String id = request.getParameter("article_id");
    	String userID = (String) request.getSession().getAttribute("userID");

    	String writer = boardArticleDao.getArticle(Integer.parseInt(id)).getWriter();

    	if (userID.equals(writer) || request.getSession().getAttribute("userType").equals(User.TYPE_ADMIN)) {
    		if(boardArticleDao.deleteArticle(Integer.parseInt(id))>0){
    			mav.addObject("deleted", true);
    		}else{
    			mav.addObject("deleted", false);
    		}
    	} else{
    		mav.addObject("deleted", false);
    	}
    		
    	
    	
    	return mav;
    }
    
    @RequestMapping("/Board/insertArticleForm.do")
    public ModelAndView articleInsertForm(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/insertArticleForm");
    	User user = userDao.userGet((String)request.getSession().getAttribute("userID"));
    	boolean isAdmin = false;
    	if(user.getUser_type().equals(User.TYPE_ADMIN)){
    		isAdmin = true;
    	}
    	mav.addObject("isAdmin", isAdmin);
    	
    	return mav;
    }
    
    @RequestMapping("/Board/ajax_attachImage.do")
    public ModelAndView ajax_attachImage(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/ajax_attachImage");
    	FileUpload fileUpload = new FileUpload(request);
    	try {
			mav.addObject("src",fileUpload.upload());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return mav;
    }
    
    @RequestMapping("/Board/insertArticle.do")
    public ModelAndView insertArticle(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/insertArticle");
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	User user = userDao.userGet((String)request.getSession().getAttribute("userID"));

    	int listId = Integer.parseInt(request.getParameter("listId"));
    	String writer = ESAPI.encoder().encodeForHTML((String)request.getSession().getAttribute("userID"));
    	String title = ESAPI.encoder().encodeForHTML(request.getParameter("title"));
    	String content = request.getParameter("contents");
    	boolean isNotice = false;
    	if(request.getParameter("isNotice") != null && user.getUser_type().equals(User.TYPE_ADMIN)){
    		isNotice = true;
    	}

    	BoardArticle boardArticle = new BoardArticle(listId,writer, title, content, isNotice);

    	
    	if(boardArticleDao.insertArticle(boardArticle) > 0){
    		mav.addObject("inserted", true);
    	} else{
    		mav.addObject("inserted", false);
    	}
    	
    	return mav;
    }
    
    @RequestMapping("/Board/updateArticleForm.do")
    public ModelAndView updateArticleForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	ModelAndView mav = new ModelAndView("/Board/updateArticleForm");
    	
    	int article_id = Integer.parseInt(request.getParameter("article_id"));
    	BoardArticle article = boardArticleDao.getArticle(article_id);
    	
    	if(request.getSession().getAttribute("userType").equals("") && (request.getSession().getAttribute("userID") == null || !((String)request.getSession().getAttribute("userID")).equals(article.getWriter()))){
    		throw new Exception("Error");
    	} else{
    		mav.addObject("contentForJs", ESAPI.encoder().encodeForJavaScript(article.getContent()));
    	}
    	mav.addObject("article",article);
    	
    	
    	return mav;
    }
    
    @RequestMapping("/Board/updateArticle.do")
    public ModelAndView updateArticle(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	ModelAndView mav = new ModelAndView("/Board/updateArticle");

    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	int article_id = Integer.parseInt(request.getParameter("article_id"));
    	BoardArticle article = boardArticleDao.getArticle(article_id);
    	
    	if(request.getSession().getAttribute("userType").equals("") && (request.getSession().getAttribute("userID") == null || !((String)request.getSession().getAttribute("userID")).equals(article.getWriter()))){
    		throw new Exception("Error");
    	}

    	int listId = Integer.parseInt(request.getParameter("listId"));
    	
    	String userID = (String) request.getSession().getAttribute("userID");
    	String writer = ESAPI.encoder().encodeForHTML((String)request.getSession().getAttribute("userID"));
    	String title = ESAPI.encoder().encodeForHTML(request.getParameter("title"));
    	String content = request.getParameter("contents");
    	
    	article.setTitle(title);
    	article.setContent(content);

    	if (userID.equals(writer) || request.getSession().getAttribute("userType").equals(User.TYPE_ADMIN)) {
	    	if(boardArticleDao.updateArticle(article) > 0){
	    		mav.addObject("updated", true);
	    	} else{
	    		mav.addObject("updated", false);
	    	}
	    	mav.addObject("article_id",article_id);
    	}
    	
    	return mav;
    }
    
    @RequestMapping("/Board/commentDelete.do")
    public ModelAndView commentDelete(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView mav = new ModelAndView("/Board/deleteComment");
    	
    	String id = request.getParameter("comment_id");
    	String articldId = request.getParameter("article_id");
    	String userID = (String) request.getSession().getAttribute("userID");
    	
    	mav.addObject("article_id", articldId);

    	String writer = commentDao.getComment(Integer.parseInt(id)).getComment_writer();

    	if (userID.equals(writer) || request.getSession().getAttribute("userType").equals(User.TYPE_ADMIN)) {
    		if(commentDao.deleteComment(Integer.parseInt(id))>0){
    			mav.addObject("deleted", true);
    		} else{
    			mav.addObject("deleted", false);
    		}
    	} else{
    		mav.addObject("deleted", false);
    	}
    	return mav;
    }
    
}
