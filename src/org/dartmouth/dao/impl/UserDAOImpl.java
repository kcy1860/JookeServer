package org.dartmouth.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.UserDAO;
import org.dartmouth.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
@Repository
public class UserDAOImpl implements UserDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Result insertUser(UserDO user) {
		Result result = new Result();
		// check for duplicate
		Map<String, Object> map = new HashMap<String, Object>();
		if (user.getThirdparty_id() != null) {
			map.put("third_party_id", user.getThirdparty_id());
		} else {
			map.put("email", user.getEmail());
		}
		List<UserDO> list = query(map);
		if (list.size() > 0) {
			result.setSuccess(false);
			result.setResultObj(-1);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.SIGN_UP_DUPLICATE);
			return result;
		}
		String inserQuery = "insert into user (name,email,password,profile_img,signup_type,third_party_id,last_modified,created_at) values (?,?, ?, ?, ?, ?,NOW(), NOW()) ";
		Object[] params = new Object[] { user.getFullname(), user.getEmail(),
				user.getPassword(), user.getProfile_img(),
				user.getSignup_type(), user.getThirdparty_id() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.INTEGER };

		result.setSuccess(jdbcTemplate.update(inserQuery, params, types) == 1);
		list = query(map);
		result.setResultObj(list.get(0).getId());
		result.setMsg(result.isSuccess() ? null
				: GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
		return result;
	}

	@Override
	public int updateUser(Map<String, Object> params) {
		return 0;
	}

	@Override
	public int deleteUserByQuery(Map<String, Object> params) {
		return 0;
	}

	@Override
	public List<UserDO> query(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer("select * from user where 1=1");
		Object[] ps = new Object[params.size()];
		int index = 0;
		for (Entry<String, Object> entry : params.entrySet()) {
			sql.append(" and ");
			sql.append(entry.getKey() + "=?");
			ps[index++] = entry.getValue();
		}
		sql.append(" order by id desc");
		List<UserDO> result = jdbcTemplate.query(sql.toString(), ps,
				new ResultSetExtractor<List<UserDO>>() {
					@Override
					public List<UserDO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						List<UserDO> list = new ArrayList<UserDO>();
						while (rs.next()) {
							UserDO u = new UserDO();
							u.setCreated_at(rs.getDate("created_at"));
							u.setEmail(rs.getString("email"));
							u.setFacebook_link(rs.getString("facebooklink"));
							u.setGender(rs.getBoolean("gender"));
							u.setId(rs.getLong("id"));
							u.setInstagram_link(rs.getString("instagramlink"));
							u.setLast_modified(rs.getDate("last_modified"));
							u.setFullname(rs.getString("name"));
							u.setProfile_img(rs.getString("profile_img"));
							u.setPassword(rs.getString("password"));
							u.setSignup_type(rs.getInt("signup_type"));
							u.setThirdparty_id(rs.getLong("third_party_id"));
							u.setTwitter_link(rs.getString("twitterlink"));
							list.add(u);
						}
						return list;
					}
				});
		return result;
	}

}
