package org.dartmouth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.UserDAO;
import org.dartmouth.domain.UserDO;
import org.dartmouth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDAO;

	@Override
	public Result addUser(UserDO user) {
		Result result = this.userDAO.insertUser(user);
		return result;
	}

	@Override
	public Result login(String email, String pwd) {
		Result result = new Result();
		if (email == null || pwd == null) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.LOGIN_FAIL);
			return result;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("email", email);
		queryMap.put("password", pwd);
		List<UserDO> users = findUser(queryMap);
		if (users.size() > 0) {
			result.setSuccess(true);
			result.setResultObj(users.get(0).getId());
		} else {
			result.setSuccess(false);
			result.setResultObj(-1);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.LOGIN_FAIL);
		}
		return result;
	}

	@Override
	public List<UserDO> findUser(Map<String, Object> query) {
		return userDAO.query(query);
	}
}
