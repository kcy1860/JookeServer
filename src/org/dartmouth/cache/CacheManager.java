package org.dartmouth.cache;

import java.io.Serializable;

import org.dartmouth.common.Settings;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public class CacheManager implements Serializable {

	private static final long serialVersionUID = -2818096788568622496L;

	public static EventCache eventCache = new EventCache(Settings.MAX_EVENT);
}
