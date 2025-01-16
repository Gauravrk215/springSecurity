package com.realnet.event_management.Services;
import com.realnet.event_management.Repository.Event_ManagementRepository;
import com.realnet.event_management.Entity.Event_Management;import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.realnet.SequenceGenerator.Service.SequenceService;
import com.realnet.Notification.Entity.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
	import org.springframework.stereotype.Service;

@Service
 public class Event_ManagementService {
@Autowired
private Event_ManagementRepository Repository;














public Event_Management Savedata(Event_Management data) {














Event_Management save = Repository.save(data);
				return save;	
			}


//	get all with pagination
	public Page<Event_Management> getAllWithPagination(Pageable page) {
		return Repository.findAll(page);
	}			
public List<Event_Management> getdetails() {
				return (List<Event_Management>) Repository.findAll();
			}


public Event_Management getdetailsbyId(Integer id) {
	return Repository.findById(id).get();
			}


	public void delete_by_id(Integer id) {
 Repository.deleteById(id);
}


public Event_Management update(Event_Management data,Integer id) {
	Event_Management old = Repository.findById(id).get();
old.setPractice_match(data.getPractice_match());

old.setAdmin_name(data.getAdmin_name());

old.setGround(data.getGround());

old.setDatetime(data.getDatetime());

old.setName(data.getName());

old.setDescription(data.getDescription());

old.setActive (data.isActive());

final Event_Management test = Repository.save(old);
  return test;}}
