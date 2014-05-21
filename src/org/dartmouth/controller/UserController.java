package org.dartmouth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dartmouth.common.CommonUtils;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.domain.UserDO;
import org.dartmouth.service.UserService;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	// TODO @RequestMapping(method = RequestMethod.GET)
	@RequestMapping(value = "/login")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			int loginType = Integer.valueOf(request.getParameter("login_type"));
			JSONStringer stringer = new JSONStringer();
			// if is login using third party account
			if (loginType != 0) {
				Long third_party_id = Long.valueOf(request
						.getParameter("thirdparty_id"));
				String name = request.getParameter("fullname");
				String profile_img = request.getParameter("profile_img");

				Map<String, Object> query = new HashMap<String, Object>();
				query.put("third_party_id", third_party_id);
				List<UserDO> users = this.userService.findUser(query);
				// if it is the first time the user login
				// then create a profile for him
				if (users.size() == 0) {
					UserDO user = new UserDO();
					user.setThird_party_id(third_party_id);
					user.setName(name);
					user.setProfile_img(profile_img);
					this.userService.addUser(user);
				}
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
				return;
			}
			String email = request.getParameter("email");
			String pwd = request.getParameter("password");

			boolean result = userService.login(email, pwd);
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(result).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			response.getWriter().append("invalid parameters");
		}
	}

	@RequestMapping(value = "/signup")
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			Integer type = Integer.valueOf(request.getParameter("signup_type"));
			String str_thirdparty_id = request.getParameter("thirdparty_id");
			Long thirdparty_id = str_thirdparty_id == null ? null : Long
					.valueOf(str_thirdparty_id);
			String email = request.getParameter("email");
			if (email == null && thirdparty_id == null) {
				throw new Exception("invalid parameters");
			}
			String pwd = request.getParameter("password");
			String name = request.getParameter("fullname");
			String profile_img = request.getParameter("profile_img");
			UserDO user = new UserDO();
			user.setTag(type);
			user.setThird_party_id(thirdparty_id);
			user.setEmail(email);
			user.setPwd(pwd);
			user.setName(name);
			user.setProfile_img(profile_img);
			
			Result result = userService.addUser(user);
			JSONStringer stringer = new JSONStringer();

			if (result.isSuccess()) {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
			} else {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false).key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(result.getMsg()).endObject();
			}
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			response.getWriter().append("invalid parameters");
		}
	}

	@RequestMapping(value = "/listusers")
	public void list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			List<UserDO> list = this.userService
					.findUser(new HashMap<String, Object>());
			StringBuffer buffer = new StringBuffer();
			for (UserDO u : list) {
				buffer.append(CommonUtils.testObj2String(u));
				buffer.append("\n");
			}
			response.getWriter().append(buffer.toString());
		} catch (Exception e) {
			response.getWriter().append("invalid parameters:");
		}
	}
}
