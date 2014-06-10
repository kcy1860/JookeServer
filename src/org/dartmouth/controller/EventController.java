package org.dartmouth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.ParticipantDO;
import org.dartmouth.service.EventService;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
@Controller
public class EventController {

	@Autowired
	private EventService eventService;
	
	static Logger logger = Logger.getLogger(EventController.class.getName());
	
	@RequestMapping(value = "/create_event")
	public void create_event(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			EventDO event = EventDO.getNewInstance();
			event.fillByRequest(request);

			// TODO if event is valid
			event.setEvent_id(EventDO.incrementCount());
			Long host_id = Long.valueOf(request.getParameter("host_id"));
			String host_ip = request.getParameter("host_ip");
			String profile_img = request.getParameter("host_profile_img");
			String name = request.getParameter("host_name");

			ParticipantDO part = new ParticipantDO();
			part.setHost_ip(host_ip);
			part.setId(host_id);
			part.setProfile_img(profile_img);
			part.setName(name);

			this.eventService.addEventByUser(event, part);

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(true).key(GlobalVariables.RESPONSE_KEYS.EVENTID)
					.value(event.getEvent_id().toString()).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/create_event_pc")
	public void create_event_pc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			EventDO event = EventDO.getNewInstance();
			event.fillByRequest(request);

			// TODO if event is valid
			event.setEvent_id(EventDO.incrementCount());
			Long host_id = Long.valueOf(request.getParameter("host_id"));
			String host_ip = request.getParameter("host_ip");
			String profile_img = request.getParameter("host_profile_img");
			String name = request.getParameter("host_name");

			ParticipantDO part = new ParticipantDO();
			// only difference with
			// part.setHost_ip(host_ip);
			event.setPc_ip(host_ip);
			part.setId(host_id);
			part.setProfile_img(profile_img);
			part.setName(name);

			this.eventService.addEventByUser(event, part);

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(true).key(GlobalVariables.RESPONSE_KEYS.EVENTID)
					.value(event.getEvent_id().toString()).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/discover")
	public void discover(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Float lat = Float.valueOf(request.getParameter("current_loc_lat"));
			Float lon = Float.valueOf(request.getParameter("current_loc_lon"));
			String zip = request.getParameter("current_zip");
			PriorityQueue<EventDO> es = this.eventService.getNearByEvents(lat,
					lon, zip);
			// JSONObject result = new JSONObject();
			JSONObject element = new JSONObject();
			List<JSONObject> arr = new ArrayList<JSONObject>();
			// JSONArray arr = new JSONArray();
			while (!es.isEmpty()) {
				EventDO ev = es.poll();
				JSONObject ee = new JSONObject();
				ee.put("event_mode", ev.getEvent_mode());
				ee.put("allow_add", ev.getAllow_add());
				ee.put("event_id", ev.getEvent_id());
				ee.put("event_name", ev.getEvent_name());
				ee.put("host_id", ev.getHost().getId().toString());
				ee.put("host_profile_img", ev.getHost().getProfile_img());
				ee.put("host_name", ev.getHost().getName());
				arr.add(ee);
			}
			element.accumulate("events", arr.toString());
			stringer.object().key("value").value(element).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()

					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/pull_info")
	public void pull_info(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Long eventId = Long.valueOf(request.getParameter("event_id"));
			EventDO event = this.eventService.getEvent(eventId);
			if (event != null) {
				stringer.object().key("event_name")
						.value(event.getEvent_name()).key("event_mode")
						.value(event.getEvent_mode()).key("event_time")
						.value(event.getEvent_time()).key("pc_ip")
						.value(event.getPc_ip()).key("host_id")
						.value(event.getHost().getId()).endObject();
				response.getWriter().append(stringer.toString());
			}
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/host_ip")
	public void host_ip(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Long eventId = Long.valueOf(request.getParameter("event_id"));
			EventDO event = this.eventService.getEvent(eventId);
			if (event != null) {
				stringer.object().key("host_ip")
						.value(event.getHost().getHost_ip()).endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/leave")
	public void leave(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Long eventId = Long.valueOf(request.getParameter("event_id"));
			Long hostId = Long.valueOf(request.getParameter("host_id"));
			EventDO event = this.eventService.getEvent(eventId);
			if (event.getHost().getId().equals(hostId)) {
				this.eventService.deleteEvent(eventId);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
			} else {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.EVENT_USER_MISMATCH)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/heart_beat")
	public void heart_beat(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Long eventId = Long.valueOf(request.getParameter("event_id"));
			Long hostId = Long.valueOf(request.getParameter("host_id"));
			String host_ip = request.getParameter("host_ip");
			EventDO event = this.eventService.getEvent(eventId);
			if (event.getHost().getId().equals(hostId)) {
				event.getHost().setHost_ip(host_ip);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
			} else {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.EVENT_USER_MISMATCH)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

}
