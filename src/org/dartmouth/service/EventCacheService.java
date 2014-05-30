package org.dartmouth.service;

import java.util.LinkedList;
import java.util.List;

import org.dartmouth.domain.EventDO;


public interface EventCacheService {
	
	public void addEvent(EventDO event) ;

	public EventDO getEventById(Long eventID);

	public List<EventDO> getEventsByZip(String zip);

	public EventDO deleteEvent(Long eventID);

	public void clearMemory();

	public LinkedList<EventDO> getAll();
	
}
