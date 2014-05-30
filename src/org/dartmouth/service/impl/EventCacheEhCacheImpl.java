package org.dartmouth.service.impl;

import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.apache.log4j.Logger;
import org.dartmouth.domain.EventDO;
import org.dartmouth.service.EventCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier(value = "ehcache")
public class EventCacheEhCacheImpl implements EventCacheService {

	static Logger logger = Logger.getLogger(EventCacheEhCacheImpl.class
			.getName());

	private Cache container = null;

	public EventCacheEhCacheImpl() {
		Configuration cacheManagerConfig = new Configuration();
		CacheConfiguration cacheConfig = new CacheConfiguration("eventCache", 0);
		Searchable searchable = new Searchable();
		cacheConfig.addSearchable(searchable);
		searchable.addSearchAttribute(new SearchAttribute().name(
				"event_zip_code").expression("value.getEvent_zip_code()"));

		CacheManager manager = CacheManager.newInstance(cacheManagerConfig);
		manager.addCache(new Cache(cacheConfig));
		container = manager.getCache("eventCache");
	}

	@Override
	public void addEvent(EventDO event) {
		Element e = new Element(event.getEvent_id(), event);
		container.put(e);
	}

	@Override
	public EventDO getEventById(Long eventID) {
		return (EventDO) container.get(eventID).getObjectValue();
	}

	@Override
	public List<EventDO> getEventsByZip(String zip) {
		Attribute<String> zip_code = container
				.getSearchAttribute("event_zip_code");
		Results results = container.createQuery().addCriteria(zip_code.eq(zip))
				.includeValues().execute();

		List<Result> list = results.all();
		List<EventDO> result = new LinkedList<EventDO>();
		for (Result r : list) {
			result.add((EventDO) r.getValue());
		}
		return result;
	}

	@Override
	public EventDO deleteEvent(Long eventID) {
		EventDO result = (EventDO) container.get(eventID).getObjectValue();
		container.remove(eventID);
		return result;
	}

	@Override
	public void clearMemory() {
		container.removeAll();
	}

	@Override
	@SuppressWarnings("unchecked")
	public LinkedList<EventDO> getAll() {
		LinkedList<EventDO> result = new LinkedList<EventDO>();
		List<Object> keys = (List<Object>) container.getKeys();
		for (Object o : keys) {
			result.add(this.getEventById((Long) o));
		}
		return result;
	}
}
