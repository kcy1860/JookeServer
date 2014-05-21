package org.dartmouth.service;

import java.util.List;
import java.util.Map;

import org.dartmouth.common.Result;
import org.dartmouth.domain.UserDO;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public interface UserService {
	Result addUser(UserDO user);

	boolean login(String usr, String pwd);

	List<UserDO> findUser(Map<String, Object> query);
}
