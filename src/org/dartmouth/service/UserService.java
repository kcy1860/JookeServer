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

	Result login(String usr, String pwd);
	
	Result update(UserDO user);

	List<UserDO> findUser(Map<String, Object> query);
}
