package com.trace.file.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trace.file.entity.Notification;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 12:37:35
 */
@Service
public class NotificationServiceImpl {
	
	@Autowired private EntityManager entityManager;
	
	/**
	 * This prevents memory blow‑up and reduces DB round‑trips.
	 * 
	 * @author PULIPATI VENKATA UDAYKIRAN
	 * @since Tuesday 25-August-2026 12:38:08
	 * @param notifications
	 */
	@Transactional
	public void saveNotifications(List<Notification> notifications) {
	    for (int i = 0; i < notifications.size(); i++) {
	        entityManager.persist(notifications.get(i));
	        if (i % 1000 == 0) { // flush every 1000
	            entityManager.flush();
	            entityManager.clear();
	        }
	    }
	}

}
