package org.dartmouth.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dartmouth.domain.EventDO;

/**
 * 
 * @author Yaozhong Kang
 * @date May 23, 2014
 */
public class EventCache extends LRUCache {

	private static final long serialVersionUID = 73954831791646322L;

	public EventCache(int capacity) {
		super(capacity);
		events = new HashMap<String, List<EventDO>>();
	}

	private Map<String, List<EventDO>> events;

	public void addEvent(EventDO event) {
		this.set(event.getEvent_id(), event);
		List<EventDO> list = events.get(event.getEvent_zip_code());
		if (list == null) {
			list = new LinkedList<EventDO>();
			events.put(event.getEvent_zip_code(), list);
		}
		list.add(event);
	}

	public EventDO getEventById(Long eventID) {
		return (EventDO) super.get(eventID);
	}

	public List<EventDO> getEventsByZip(String zip) {
		List<EventDO> result = this.events.get(zip);
		result = result == null ? new LinkedList<EventDO>() : result;
		return result;
	}

	public EventDO deleteEvent(Long eventID) {
		EventDO e = (EventDO) super.delete(eventID);
		this.events.get(e.getEvent_zip_code()).remove(e);
		return e;
	}

	public void clearMemory() {
		this.events.clear();
		this.map.clear();
		this.list = new DoubleLinkedList();
	}

	public LinkedList<EventDO> getAll() {
		LinkedList<EventDO> result = new LinkedList<EventDO>();
		for (Entry<Object, Node> entry : this.map.entrySet()) {
			result.add((EventDO) entry.getValue().val);
		}
		return result;
	}
}
