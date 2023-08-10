package com.java.fsd.bmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.fsd.bmt.entity.PurchaseOrderEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseOrderEntity, Integer> {

//	@Query("SELECT orderId, eventId, emailId, status, timer  FROM PurchaseOrderEntity WHERE status = 'inprogress' AND eventId = ?1 ORDER BY timer ASC")
//	public List<PurchaseOrderEntity> getOrdersInprogress(String eventId);

	@Query("SELECT u FROM PurchaseOrderEntity u WHERE u.status = 'inprogress' and u.eventId = :eventId ORDER BY u.timer ASC")
	List<PurchaseOrderEntity> findOrdersInprogress(@Param("eventId") String eventId);

}