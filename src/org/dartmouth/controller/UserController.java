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
		JSONStringer stringer = new JSONStringer();
		try {
			int loginType = Integer.valueOf(request.getParameter("login_type"));

			// if is login using third party account
			if (loginType != 0) {
				Long third_party_id = Long.valueOf(request
						.getParameter("thirdparty_id"));
				Map<String, Object> query = new HashMap<String, Object>();
				query.put("third_party_id", third_party_id);
				query.put("signup_type", loginType);

				List<UserDO> users = this.userService.findUser(query);
				// if it is the first time the user login, then create a new
				// profile
				if (users.size() == 0) {
					UserDO user = new UserDO();
					user.fillByRequest(request);
					user.setSignup_type(loginType);

					if (user.getThirdparty_id() == null
							|| user.getFullname() == null
							|| user.getProfile_img() == null) {
						stringer.object()
								.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
								.value(false)
								.key(GlobalVariables.RESPONSE_KEYS.MSG)
								.value("Fail to add user").endObject();
						response.getWriter().append(stringer.toString());
						return;
					}
					this.userService.addUser(user);
				}
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
				return;
			}
			String email = request.getParameter("email");
			String pwd = request.getParameter("password");

			Result result = userService.login(email, pwd);
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(result.isSuccess())
					.key(GlobalVariables.RESPONSE_KEYS.USERID)
					.value(result.getResultObj())
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(result.getMsg()).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/signup")
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			UserDO user = new UserDO();
			user.fillByRequest(request);
			// TODO need to validate the parameters

			Result result = userService.addUser(user);
			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(result.isSuccess())
					.key(GlobalVariables.RESPONSE_KEYS.USERID)
					.value(result.getResultObj())
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(result.getMsg()).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
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
		}
	}
}
