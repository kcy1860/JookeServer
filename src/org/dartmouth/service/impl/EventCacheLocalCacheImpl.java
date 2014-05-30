package org.dartmouth.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dartmouth.cache.LRUCache;
import org.dartmouth.common.Settings;
import org.dartmouth.domain.EventDO;
import org.dartmouth.service.EventCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Yaozhong Kang
 * @date May 23, 2014
 */
@Service
@Transactional
@Qualifier(value = "local")
public class EventCacheLocalCacheImpl extends LRUCache implements
		EventCacheService {

	private static final long serialVersionUID = 73954831791646322L;

	static Logger logger = Logger.getLogger(EventCacheLocalCacheImpl.class
			.getName());

	/**
	 * use default setting
	 */
	public EventCacheLocalCacheImpl() {
		super(Settings.MAX_EVENT);
		events = new HashMap<String, List<EventDO>>();
	}

	public EventCacheLocalCacheImpl(int capacity) {
		super(capacity);
		events = new HashMap<String, List<EventDO>>();
	}

	private Map<String, List<EventDO>> events;

	@Override
	public void addEvent(EventDO event) {
		this.set(event.getEvent_id(), event);
		List<EventDO> list = events.get(event.getEvent_zip_code());
		if (list == null) {
			list = new LinkedList<EventDO>();
			events.put(event.getEvent_zip_code(), list);
		}
		list.add(event);
	}

	@Override
	public EventDO getEventById(Long eventID) {
		return (EventDO) super.get(eventID);
	}

	@Override
	public List<EventDO> getEventsByZip(String zip) {
		List<EventDO> result = this.events.get(zip);
		result = result == null ? new LinkedList<EventDO>() : result;
		return result;
	}

	@Override
	public EventDO deleteEvent(Long eventID) {
		EventDO e = (EventDO) super.delete(eventID);
		this.events.get(e.getEvent_zip_code()).remove(e);
		return e;
	}

	@Override
	public void clearMemory() {
		this.events.clear();
		this.map.clear();
		this.list = new DoubleLinkedList();
	}

	@Override
	public LinkedList<EventDO> getAll() {
		LinkedList<EventDO> result = new LinkedList<EventDO>();
		for (Entry<Object, Node> entry : this.map.entrySet()) {
			result.add((EventDO) entry.getValue().getVal());
		}
		
		return result;
	}
}
