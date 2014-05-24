package org.dartmouth.common;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public interface GlobalVariables {
	interface RESPONSE_KEYS {
		public static final String SUCCESS = "success";
		public static final String MSG = "msg";
		public static final String USERID = "userid";
		public static final String EVENTID = "event_id";
	}

	interface RESPONSE_MESSAGES {
		public static final String INVALID_PARAMETERS = "Invalid Params";
		public static final String DB_ERROR = "Dabase Failure";
		public static final String LOGIN_FAIL = "Wrong Username/Password";
		public static final String SIGN_UP_DUPLICATE = "User Already Existed";
		public static final String EVENT_USER_MISMATCH = "Permission Denied";
	}
	
	public interface SIGN_UP_TYPE {
		static final int normal = 0;
		static final int facebook = 1;
		static final int twitter = 2;
	}
}
