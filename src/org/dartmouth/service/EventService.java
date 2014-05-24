package org.dartmouth.service;

import java.util.PriorityQueue;

import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.ParticipantDO;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public interface EventService {
	public void addEventByUser(EventDO event, ParticipantDO p);

	public PriorityQueue<EventDO> getNearByEvents(float lat, float lon, String zip);

	public EventDO getEvent(Long id);
	
	public EventDO deleteEvent(Long id);
}
