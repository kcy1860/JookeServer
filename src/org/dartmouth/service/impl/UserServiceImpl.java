package org.dartmouth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		// TODO check whether the user has enough attributes
		Result result = this.userDAO.insertUser(user);
		return result;
	}

	@Override
	public boolean login(String email, String pwd) {
		if (email == null || pwd == null) {
			return false;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("email", email);
		queryMap.put("password", pwd);
		List<UserDO> users = findUser(queryMap);
		return users.size() > 0;
	}

	@Override
	public List<UserDO> findUser(Map<String, Object> query) {
		return userDAO.query(query);
	}
}
