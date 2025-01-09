package com.realnet.Payment.Razorpay;

import com.razorpay.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/razorpay")
public class RazorpayController {

	@Autowired
	private RazorpayRepo repo;

	@Value("${razorpay.api.key}")
	private String razorpayApiKey;

	@Value("${razorpay.api.secret}")
	private String razorpayApiSecret;

	@PostMapping("/create-order")
	public void payOrder(@RequestBody Map<String, Object> data) throws RazorpayException {

		int amt = Integer.parseInt(data.get("amount").toString());
		amt = 50;

		RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

		String trnx = UUID.randomUUID().toString();
		JSONObject orderDetails = createOrderJSON(amt, "INR", "_INV/0001/2023");

		Order order = razorpayClient.orders.create(orderDetails);

		System.out.println(order);
		// Taking record into Our database
		RazorpayDTO dto = RazorpayDTO.builder().orderId(order.get("order_id")).bodyField(String.valueOf(order)).build();
		repo.save(dto);
	}

	@PostMapping("/verify-payment")
	public ResponseEntity<String> verifyPayment(@RequestParam("orderId") JSONObject orderId) {
		try {
			RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);
			Payment payment = razorpayClient.payments.fetch(orderId.get("order_id").toString());

			boolean isSignatureValid = Utils.verifyPaymentSignature(orderId, razorpayApiSecret);

			if (payment != null && payment.get("status").equals("captured") && isSignatureValid) {
				return ResponseEntity.ok("Payment successfully verified.");
			} else {
				return ResponseEntity.badRequest().body("Payment verification failed.");
			}
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred during payment verification.");
		}
	}

	@PostMapping("update-order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {

		RazorpayDTO current_order = repo.findByOrderId(data.get("order_id").toString());
		current_order.setBodyField(data.toString());
		repo.save(current_order);

//        return ResponseEntity.ok(Map.of("msg", "updated"));
		return ResponseEntity.ok("updated");

	}

	public JSONObject createOrderJSON(Integer amount, String currency, String trxnId) {
		JSONObject options = new JSONObject();
		options.put("amount", amount * 100); // amount in paise = Rs*100
		options.put("currency", currency);
		options.put("receipt", trxnId);

		return options;
	}

}
