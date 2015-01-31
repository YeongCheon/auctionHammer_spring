package hoseo.b.auctionHammer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bean.User;
import dao.UserDao;

@Controller
public class LoginController {
	@Autowired(required=true)
	private HttpServletRequest request;
	
	@Autowired
	UserDao userDao;
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/login/loginForm.do")      
    public ModelAndView loginForm() {             
    
        String message = "loginForm";
        return new ModelAndView("login/loginForm", "message", message);       
    }
    
    @RequestMapping("/login/login.do")
    public ModelAndView login(){
    	String id = request.getParameter("userID");
    	String pwd = util.Sha256Util.getEncrypt(request.getParameter("userPwd"));
    	User user = userDao.userGet(id, pwd);
    	
    	boolean result = false;

    	
    	if(user != null){
	    	HttpSession session = request.getSession();

			session.setAttribute("userID", user.getUser_id());
			session.setAttribute("userName", user.getUser_name());
			session.setAttribute("userNicname", user.getUser_nicname());
			session.setAttribute("userType", user.getUser_type());
			
			result = true;
    	} 	

    	return new ModelAndView("login/login","logined", result);
    }
    

    @RequestMapping("/login/logout.do")
    public ModelAndView logout(){
    	request.getSession().invalidate();    	
    	
    	return new ModelAndView("login/logout");
    }
}
