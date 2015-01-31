package hoseo.b.auctionHammer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import util.Sha256Util;
import bean.LandnumZipcode;
import bean.RoadnameZipcode;
import bean.User;
import dao.LandnumZipcodeDao;
import dao.RoadnameZipcodeDao;
import dao.UserDao;

@Controller
public class UserInfoController {
	@Autowired
	UserDao userDao;
	@Autowired
	LandnumZipcodeDao LandNumZipcodeDao;
	@Autowired
	RoadnameZipcodeDao RoadNameZipcodeDao;

	@RequestMapping("/user/insertUserForm.do")
	public ModelAndView insertUserForm() {
		return new ModelAndView("/user/insertUserForm");
	}
	
	@RequestMapping("/user/insertUser.do")
	public ModelAndView insertUser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = new User();

		
		String id = request.getParameter("user_id");
		String password = request.getParameter("user_password");
		String pwdHint = request.getParameter("user_pwdHint");
		String answer = request.getParameter("user_answer");
		String name = request.getParameter("user_name");
		String nicname = request.getParameter("user_nicname");
		String gender = request.getParameter("user_gender");
		String birthday = request.getParameter("birthday_year") + request.getParameter("birthday_month") + request.getParameter("birthday_day");
		String emailID = request.getParameter("user_emailID");
		String emailDomain = request.getParameter("user_emailDomain");
		String localNum = request.getParameter("user_localNum");
		String callNum = request.getParameter("user_callNum01")
				+ request.getParameter("user_callNum02"); // !!!
		String phone = request.getParameter("user_phone01")
				+ request.getParameter("user_phone02")
				+ request.getParameter("user_phone03");
		String zipcode = request.getParameter("user_zipcode01")
				+ request.getParameter("user_zipcode02"); // !!!
		String addr1 = request.getParameter("user_addr01");
		String addr2 = request.getParameter("user_addr02");
		String type= request.getParameter("user_type");
		
		/*
		id = ESAPI.encoder().encodeForHTML(id);
		password = ESAPI.encoder().encodeForHTML(password);
		pwdHint = ESAPI.encoder().encodeForHTML(pwdHint);
		answer = ESAPI.encoder().encodeForHTML(answer);
		name = ESAPI.encoder().encodeForHTML(name);
		nicname = ESAPI.encoder().encodeForHTML(nicname);
		gender = ESAPI.encoder().encodeForHTML(gender);
		birthday = ESAPI.encoder().encodeForHTML(birthday);
		emailID = ESAPI.encoder().encodeForHTML(emailID);
		emailDomain = ESAPI.encoder().encodeForHTML(emailDomain);
		localNum = ESAPI.encoder().encodeForHTML(localNum);
		callNum = ESAPI.encoder().encodeForHTML(callNum);
		phone = ESAPI.encoder().encodeForHTML(phone);
		zipcode = ESAPI.encoder().encodeForHTML(zipcode);
		addr1 = ESAPI.encoder().encodeForHTML(addr1);
		addr2 = ESAPI.encoder().encodeForHTML(addr2);
		type = ESAPI.encoder().encodeForHTML(type);
		 */
		user.setUser_id(id);
		user.setUser_password(password);
		user.setUser_pwdHint(pwdHint);
		user.setUser_answer(answer);
		user.setUser_name(name);
		user.setUser_nicname(nicname);
		user.setUser_gender(gender);
		user.setUser_birthday(birthday);
		user.setUser_emailID(emailID);
		user.setUser_emailDomain(emailDomain);
		user.setUser_localNum(localNum);
		user.setUser_callNum(callNum);
		user.setUser_phone(phone);
		user.setUser_zipcode(zipcode);
		user.setUser_addr1(addr1);
		user.setUser_addr2(addr2);
		user.setUser_type(type);
		
		

		boolean result = false;
		if(userDao.userAdd(user)>0){
			result = true;	
		} else{
			result = false;	
		}
		
		
		
		return new ModelAndView("/user/insertUser", "result", result);
	}	

	@RequestMapping("/user/ajax/ajax_idCheck.do")
	public ModelAndView ajax_idCheck(HttpServletRequest request,
			HttpServletResponse response) {
		String user_id = request.getParameter("user_id");

		User user = userDao.userGet(user_id);

		String result = "";
		String msg = null;
		boolean able = false;

		if (user == null) {
			msg = "사용가능";
			able = true;
		} else {
			msg = "사용 불가능";
			able = false;
		}

		result += "{";
		result += "\"msg\" : \"" + msg + "\", ";
		result += "\"able\" : \"" + able + "\"";
		result += "}";

		return new ModelAndView("/user/ajax/ajax_idCheck", "result", result);
	}

	@RequestMapping("/user/ajax/ajax_getLandnumZipcodeList.do")
	public ModelAndView ajax_getLandnumZipcodeList(HttpServletRequest request,
			HttpServletResponse response) {

		String result = "";
		if (request.getParameter("keyword") == null) {
			return null;
		}

		String keyword = null;
		try {
			request.setCharacterEncoding("utf-8");
			keyword = new String(request.getParameter("keyword").getBytes(
					"8859_1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<LandnumZipcode> zipcodeList = LandNumZipcodeDao.getZipcodeList(keyword);

		int size = zipcodeList.size();

		if (size > 0) {
			result += "{ \"list\" : [";

			for (int i = 0; i < size; i++) {
				LandnumZipcode zipcodeObj = zipcodeList.get(i);

				String zipcode = zipcodeObj.getZipcode();
				String sido = zipcodeObj.getSido();
				String gugun = zipcodeObj.getGugun();
				String dong = zipcodeObj.getDong();
				String bunji = zipcodeObj.getBunji();
				String seq = zipcodeObj.getSeq();

				String close = null;
				if (i == size - 1) {
					close = "}	] }";
				} else {
					close = "},";
				}
				result += "{ ";
				result += "\"number\" : \"" + i + "\", ";
				result += "\"zipcode\" : \"" + zipcode + "\", ";
				result += "\"gugun\" : \"" + gugun + "\", ";
				result += "\"dong\" : \"" + dong + "\", ";
				result += "\"bunji\" : \"" + bunji + "\", ";
				result += "\"seq\" : " + seq;
				result += close;
			}
		} else if (size <= 0) {
			result = ("{ \"list\" : []}");
		}
		
		return new ModelAndView("/user/ajax/ajax_getLandnumZipcodeList",
				"result", result);
	}

	@RequestMapping("/user/ajax/ajax_getRoadnameZipcodeList.do")
	public ModelAndView ajax_getRoadnameZipcodeList(HttpServletRequest request,
			HttpServletResponse response) {
		String result = "";

		String keyword = null;
		try {
			request.setCharacterEncoding("utf-8");
			keyword = request.getParameter("keyword");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<RoadnameZipcode> zipcodeList = RoadNameZipcodeDao.getZipcodeList(keyword);
		int size = zipcodeList.size();
		
		
		if (size > 0) {
			result = "{ \"list\" : [";

			for (int i = 0; i < size; i++) {
				RoadnameZipcode zipcodeObj = zipcodeList.get(i);

				String zipcode = zipcodeObj.getRoad_zip_cd();
				String sido = zipcodeObj.getRoad_sido();
				String gugun = zipcodeObj.getRoad_gungu();
				String road_nm = zipcodeObj.getRoad_road_nm();
				String building_no = zipcodeObj.getRoad_building_no();
				String building_nm = zipcodeObj.getRoad_building_nm();

				String close = null;
				if (i == size - 1) {
					close = "}	] }";
				} else {
					close = "},";
				}
				result += "{ ";
				result += "\"number\" : \"" + i + "\", ";
				result += "\"zipcode\" : \"" + zipcode + "\", ";
				result += "\"gugun\" : \"" + gugun + "\", ";
				result += "\"road_nm\" : \"" + road_nm + "\", ";
				result += "\"building_no\" : \"" + building_no + "\", ";
				result += "\"building_nm\" : \"" + building_nm + "\"";
				result += close;
			}
		}else if (size <= 0) {
			result = "{ \"list\" : []}";
		}

		return new ModelAndView("/user/ajax/ajax_getRoadnameZipcodeList",
				"result", result);
	}
	
	@RequestMapping("/user/modify/modifyStep01.do")
	public ModelAndView modifyStep01(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/modifyStep01");
		
		return mav;
	}

	@RequestMapping("/user/modify/modifyStep01-1.do")
	public ModelAndView modifyStep01_1(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/modifyStep01-1");
		
		String userID = (String)request.getSession().getAttribute("userID");
		String userPwd = request.getParameter("userPwd");
		
		boolean isOk = false;
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = null;
		
		user = userDao.userGet(userID, Sha256Util.getEncrypt(userPwd));
		
		if(user != null){
			request.getSession().setAttribute("modify_auth", "pass");
			request.getSession().setAttribute("password", user.getUser_password());
			isOk = true;
		}else{		
			isOk = false;
		}
		
		mav.addObject("isOk", isOk);
		return mav;
	}
	@RequestMapping("/user/modify/modifyStep02.do")
	public ModelAndView modifyStep02(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/modifyStep02");
		
		User user = null;
		
		final String[] LOCALNUMLIST = {"02","031","032","033","041","042","043","044","049","051","052","053","054","055","061","062","063","064"};
		final String[] PHONELIST= {"010","011","016","019"};
		
		String loginID = (String) request.getSession().getAttribute("userID");
		String auth = (String) request.getSession().getAttribute("modify_auth");

		user = userDao.userGet(loginID,
				(String) request.getSession().getAttribute("password"));

		mav.addObject("user", user);
		mav.addObject("localNumList", LOCALNUMLIST);
		mav.addObject("phoneNumList", PHONELIST);
		return mav;
	}
	@RequestMapping("/user/modify/submitInfoModify.do")
	public ModelAndView submitInfoModify(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/submitInfoModify");
		boolean isUpdated = false;
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = new User();

		String id = request.getParameter("user_id");
		String pwdHint = request.getParameter("user_pwdHint");
		String answer = request.getParameter("user_answer");
		String name = request.getParameter("user_name");
		String nicname = request.getParameter("user_nicname");
		String gender = request.getParameter("user_gender");
		String birthday = request.getParameter("birthday_year") + request.getParameter("birthday_month") + request.getParameter("birthday_day");
		String emailID = request.getParameter("user_emailID");
		String emailDomain = request.getParameter("user_emailDomain");
		String localNum = request.getParameter("user_localNum");
		String callNum = request.getParameter("user_callNum01")
				+ request.getParameter("user_callNum02"); // !!!
		String phone = request.getParameter("user_phone01")
				+ request.getParameter("user_phone02")
				+ request.getParameter("user_phone03");
		String zipcode = request.getParameter("user_zipcode01")
				+ request.getParameter("user_zipcode02"); // !!!
		String addr1 = request.getParameter("user_addr01");
		String addr2 = request.getParameter("user_addr02");
		
		/*		
		id = ESAPI.encoder().encodeForHTML(id);
		pwdHint = ESAPI.encoder().encodeForHTML(pwdHint);
		answer = ESAPI.encoder().encodeForHTML(answer);
		name = ESAPI.encoder().encodeForHTML(name);
		nicname = ESAPI.encoder().encodeForHTML(nicname);
		gender = ESAPI.encoder().encodeForHTML(gender);
		birthday = ESAPI.encoder().encodeForHTML(birthday); //new Column
		emailID = ESAPI.encoder().encodeForHTML(emailID);
		emailDomain = ESAPI.encoder().encodeForHTML(emailDomain);
		localNum = ESAPI.encoder().encodeForHTML(localNum);
		callNum = ESAPI.encoder().encodeForHTML(callNum);
		phone = ESAPI.encoder().encodeForHTML(phone);
		zipcode = ESAPI.encoder().encodeForHTML(zipcode);
		addr1 = ESAPI.encoder().encodeForHTML(addr1);
		addr2 = ESAPI.encoder().encodeForHTML(addr2);
*/
		user.setUser_id(id);
		user.setUser_pwdHint(pwdHint);
		user.setUser_answer(answer);
		user.setUser_name(name);
		user.setUser_nicname(nicname);
		user.setUser_gender(gender);
		user.setUser_birthday(birthday);
		user.setUser_emailID(emailID);
		user.setUser_emailDomain(emailDomain);
		user.setUser_localNum(localNum);
		user.setUser_callNum(callNum);
		user.setUser_phone(phone);
		user.setUser_zipcode(zipcode);
		user.setUser_addr1(addr1);
		user.setUser_addr2(addr2);
		

		if (userDao.userModify(user) > 0) {
			isUpdated = true;
		} else {
			isUpdated = false;
		}
		
		mav.addObject("isUpdated", isUpdated);
		return mav;
	}
	
	@RequestMapping("/user/modify/modifyPwdStep01.do")
	public ModelAndView modifyPwdStep01(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/modifyPwdStep01");

		return mav;
	}
	
	@RequestMapping("/user/modify/submitPwdModify.do")
	public ModelAndView submitPwdModify(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/user/modify/submitPwdModify");
		boolean updated = false;

		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = new User();

		String id = (String)request.getSession().getAttribute("userID");
		String password = request.getParameter("user_password");

		user.setUser_id(id);
		user.setUser_password(password);
		System.out.println(user.getUser_password());


		if (userDao.userModifyPassword(user) > 0) {
			updated = false;
		} else{
			updated = true;
		}
		mav.addObject("updated", updated);
		
		return mav;
	}	
}
