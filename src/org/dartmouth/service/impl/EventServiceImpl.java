package org.dartmouth.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.dartmouth.common.CommonUtils;
import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.ParticipantDO;
import org.dartmouth.service.EventCacheService;
import org.dartmouth.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	@Qualifier("ehcache")
	private EventCacheService eventCacheService;

	static Logger logger = Logger.getLogger(EventServiceImpl.class.getName());

	@Override
	public void addEventByUser(EventDO event, ParticipantDO user) {
		user.hostEvent(event);
		eventCacheService.addEvent(event);
	}

	@Override
	public PriorityQueue<EventDO> getNearByEvents(final float lat,
			final float lon, String zip) {
		List<EventDO> events = eventCacheService.getEventsByZip(zip);
		if (events.size() == 0) {
			return new PriorityQueue<EventDO>();
		}
		PriorityQueue<EventDO> queue = new PriorityQueue<EventDO>(
				events.size(), new Comparator<EventDO>() {
					@Override
					public int compare(EventDO o1, EventDO o2) {
						float dis1 = CommonUtils.distFrom(o1.getLat(),
								o1.getLon(), lat, lon);
						float dis2 = CommonUtils.distFrom(o2.getLat(),
								o2.getLon(), lat, lon);
						return Float.compare(dis1, dis2);
					}
				});

		for (EventDO e : events) {
			queue.add(e);
		}
		return queue;
	}

	@Override
	public EventDO getEvent(Long id) {
		return eventCacheService.getEventById(id);
	}

	@Override
	public EventDO deleteEvent(Long id) {
		EventDO event = eventCacheService.deleteEvent(id);
		return event;
	}

	@Override
	public List<EventDO> getAll() {
		return eventCacheService.getAll();
	}

	@Override
	public void deleteAll() {
		this.eventCacheService.clearMemory();

	}

}
