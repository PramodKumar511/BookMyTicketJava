package com.java.fsd.bmt.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.fsd.bmt.config.UserInfoUserDetails;
import com.java.fsd.bmt.entity.EventEntity;
import com.java.fsd.bmt.entity.PurchaseOrderEntity;
import com.java.fsd.bmt.entity.UserInfo;
import com.java.fsd.bmt.model.AuthRequest;
import com.java.fsd.bmt.request.PurchaseRequest;
import com.java.fsd.bmt.response.APIResponse;
import com.java.fsd.bmt.response.AuthResponse;
import com.java.fsd.bmt.service.BmtService;
import com.java.fsd.bmt.utils.JwtService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bmt")
public class BookMyTicketController {

	@Autowired
	private BmtService service;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	private static final Logger LOGGER = LogManager.getLogger(BookMyTicketController.class);

	@GetMapping("/welcome")
	public List<EventEntity> welcome() {
		return service.getEventList();
	}

	@PostMapping("/signup")
	public ResponseEntity<APIResponse> addNewUser(@RequestBody UserInfo userInfo) throws Exception {
		LOGGER.info("Request to add user details of user {}", userInfo.getEmailId());
		try {

			if (!service.isUserExist(userInfo.getEmailId())) {
				LOGGER.info("User {} already exist", userInfo.getEmailId());
				return new ResponseEntity<>(new APIResponse(HttpStatus.FOUND.value(), "", "", "User Already Exist"),
						HttpStatus.OK);
			}

			service.addUser(userInfo);
			LOGGER.info("Successfully added user {}", userInfo.getEmailId());
			return new ResponseEntity<>(new APIResponse(HttpStatus.OK.value(), "", "", "Successfully Signed Up"),
					HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.info("Exception while creating account of : {}", userInfo.getEmailId());
			throw e;
		}

	}

	@GetMapping("/getEventList")
	public List<EventEntity> getAllTheProducts() {
		return service.getEventList();
	}

	@GetMapping("/cancelOrder")
	public ResponseEntity<APIResponse> cancelOrder(@RequestParam String orderId, @RequestParam String eventId,
			@RequestParam String emailId) {

		APIResponse response = new APIResponse();
		if (service.cancelOrder(orderId) > 0) {
			response.setStatus(200);
			response.setData(orderId);
			response.setMessage("Ticket cancelled due to timeout. Try again!");
			return ResponseEntity.ok(response);

		} else {

			response.setStatus(500);
			response.setData("");
			response.setMessage("Unable to cancel ticket, even though transaction time out.");
			return ResponseEntity.ok(response);
		}

	}

	@PostMapping("/purchaseTicket")
	public ResponseEntity<APIResponse> purchaseTicket(@RequestBody PurchaseRequest purchaseReq) {

		APIResponse response = new APIResponse();
		int orderId = service.getFirstPersonInprogress(purchaseReq);
		if (orderId > 0) {

			response.setStatus(200);
			response.setData(orderId);
			response.setMessage("Success");
			return ResponseEntity.ok(response);

		} else {

			response.setStatus(500);
			response.setData("");
			response.setMessage("Ticket is locked for other user, please try after 5 mins");
			return ResponseEntity.ok(response);
		}

//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		
//		
//		
//		Timestamp eventExpiryTimer = service.getExpiryTime(purchaseReq.getEventId());
//		if(null != eventExpiryTimer ) {
//			
//			if(!timestamp.before(eventExpiryTimer)) {
//				PurchaseOrderEntity poe =  service.buyTickets(purchaseReq);
//				response.setStatus(200);
//				response.setData(poe.getOrderId());
//				response.setMessage("Success");
//				return ResponseEntity.ok(response);
//				
//			}else {
//				response.setStatus(500);
//				response.setData("");
//				response.setMessage("Ticket is locked for other user, please try after 5 mins");
//				return ResponseEntity.ok(response);
//				
//			}
//			
//		} else {
//			
//			return ResponseEntity.ok(response);
//		}

	}

	@GetMapping("/initiatPurchase")
	public ResponseEntity<PurchaseOrderEntity> setTimer(@RequestParam String eventId, @RequestParam String emailId) {

		return ResponseEntity.ok(service.initiatPurchase(eventId, emailId));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		LOGGER.info("{} attempting to login", authRequest.getUserName());
		AuthResponse authResponse = new AuthResponse();
		String jwtToken = "";
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

			UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
			authResponse.setEmailId(userDetails.getUsername());
			authResponse.setFirstname(userDetails.getFirstName());
			authResponse.setUid(userDetails.getUid());

			if (authentication.isAuthenticated()) {
				jwtToken = jwtService.generateToken(authRequest.getUserName());
				LOGGER.info("{} successfully logged in", userDetails.getUsername());
			} else {
				LOGGER.error("{} failed to login due to invalid credentials", authRequest.getUserName());
				throw new UsernameNotFoundException("invalid user request !");
			}

		} catch (Exception e) {
			LOGGER.error("Exception while authenticating user {}", e.getMessage());
			throw e;
		}

		authResponse.setAccessToken(jwtToken);
		authResponse.setStatus("success");
		return ResponseEntity.ok(authResponse);

	}
}
