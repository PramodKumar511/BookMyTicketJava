package com.java.fsd.bmt.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.fsd.bmt.config.UserInfoUserDetails;
import com.java.fsd.bmt.customexception.BookMyTicketException;
import com.java.fsd.bmt.entity.EventEntity;
import com.java.fsd.bmt.entity.PurchaseOrderEntity;
import com.java.fsd.bmt.entity.UserInfo;
import com.java.fsd.bmt.model.AuthRequest;
import com.java.fsd.bmt.repository.EventRepository;
import com.java.fsd.bmt.repository.PurchaseRepository;
import com.java.fsd.bmt.repository.UserInfoRepository;
import com.java.fsd.bmt.request.PurchaseRequest;
import com.java.fsd.bmt.utils.JwtService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BmtService {

	@Autowired
	private UserInfoRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	public List<EventEntity> getEventList() {

		return eventRepository.findAll();
	}

//	public EventInfo getEvent(int id) {
//		return eventList.stream().filter(product -> product.getEventId() == id).findAny()
//				.orElseThrow(() -> new RuntimeException("product " + id + " not found"));
//	}

	public boolean addUser(UserInfo userInfo) {

		try {
			userInfo.setPwd(passwordEncoder.encode(userInfo.getPwd()));
			userRepository.save(userInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

//	public String authenticateUser(AuthRequest authRequest) {
//
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
//
//		UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
//
//		if (authentication.isAuthenticated()) {
//			return jwtService.generateToken(authRequest.getUserName());
//		} else {
//			throw new UsernameNotFoundException("invalid user request !");
//		}
//	}

	public boolean isUserExist(String emailId) throws BookMyTicketException {

		Optional<UserInfo> optUserInfo = userRepository.findByEmailId(emailId);

		return optUserInfo.isEmpty();

	}

	public PurchaseOrderEntity buyTickets(PurchaseRequest orderDetails) {

		PurchaseOrderEntity poEntity = new PurchaseOrderEntity();
		poEntity.setEmailId(orderDetails.getEmailId());
		poEntity.setEventId(orderDetails.getEventId());

		return purchaseRepository.save(poEntity);
	}

	public PurchaseOrderEntity initiatPurchase(String eventId, String emailId) {
		PurchaseOrderEntity poe = new PurchaseOrderEntity();
		poe.setEmailId(emailId);
		poe.setEventId(eventId);
		poe.setStatus("inprogress");
		poe.setTimer(new Timestamp(System.currentTimeMillis()));
		return purchaseRepository.save(poe);

		// eventRepository.setTimeronEvent(eventId);
	}

	public Timestamp getExpiryTime(String eventId) {
		Optional<EventEntity> optEventData = eventRepository.findById(Integer.parseInt(eventId));
		if (optEventData.isPresent()) {
			return optEventData.get().getTimer();
		} else {
			return null;
		}
	}

	public int getFirstPersonInprogress(PurchaseRequest purchaseRequest) {

		List<PurchaseOrderEntity> poeList = purchaseRepository.findOrdersInprogress(purchaseRequest.getEventId());
		PurchaseOrderEntity fetchedPOE = poeList.get(0);
		if (fetchedPOE.getEmailId().equalsIgnoreCase(purchaseRequest.getEmailId())) {
			fetchedPOE.setStatus("success");
			purchaseRepository.save(fetchedPOE);
			return fetchedPOE.getOrderId();
		} else {
			return -1;
		}
	}

	public int cancelOrder(String orderId) {
		Optional<PurchaseOrderEntity> fetchedPOEOpt = purchaseRepository.findById(Integer.parseInt(orderId));
		if (fetchedPOEOpt.isPresent()) {
			PurchaseOrderEntity poe = fetchedPOEOpt.get();
			poe.setStatus("cancel");
			purchaseRepository.save(poe);
			return poe.getOrderId();
		} else {
			return 0;
		}

	}
}
