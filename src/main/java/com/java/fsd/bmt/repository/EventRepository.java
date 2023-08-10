package com.java.fsd.bmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.fsd.bmt.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

	@Modifying
	@Query("update EventEntity u set u.timer = DATEADD(minute, 5, CURRENT_TIMESTAMP) where u.eventId = :eventId")
	void setTimeronEvent(@Param("eventId") String eventId);

}
