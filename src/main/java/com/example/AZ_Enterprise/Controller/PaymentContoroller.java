/**
 * 
 */
package com.example.AZ_Enterprise.Controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.AZ_Enterprise.PaymentGateWay.AZPayPalSDK;
import com.example.AZ_Enterprise.Service.TransactionService;
import com.example.AZ_Enterprise.Utility.JSONParser;
import com.example.AZ_Enterprise.model.Transaction;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@RestController
@RequestMapping("/api")
public class PaymentContoroller {
  private static final Logger logger = LogManager.getLogger(PaymentContoroller.class);
  @Autowired
  AZPayPalSDK azpaypalSDK;
  @Value("${sandbox.paypal.client-id}")
  private String paypalClient_id;
  @Value("${sandbox.paypal.secret}")
  private String client_secret;
  private static String executionMode = "sandbox";
  @Autowired
  TransactionService transactionService;

  @ResponseBody
  @PostMapping(path = "/pay/{paymentId}/{amount}")
  public String CheckoutOrders(@PathVariable("paymentId") String paymentId,
      @PathVariable("amount") String amount) {
    logger.info("Amount passed:{}", amount);
    Transaction trans = null;
    if (amount == null) {
      logger.error("Amount can not be empty: {}", amount);
      throw new RuntimeException("Amount can not be empty");
    } else {
      trans = azpaypalSDK.AZEntCapturePayPalAPI(paymentId, amount);
      if (trans != null) {
        trans = transactionService.getTransDeatailsIDByPayerId(trans.getPayerId());
        return JSONParser.Object_to_JSONString(trans);
      }
    }
    return null;
  }

  @GetMapping(value = "/pay/cancel")
  public String cancelPaypalPay() {
    logger.info("PAYPAL INFO: Payment is cancelled!");
    return "cancel";
  }

  @GetMapping(value = "/pay/success")
  public String successPaypalPay(@RequestParam("paymentId") String paymendId,
      @RequestParam("PayerId") String payerId) {
    try {
      APIContext azEntapiContext = new APIContext(paypalClient_id, client_secret, executionMode);
      PaymentExecution azentPaymentExecution = new PaymentExecution();
      azentPaymentExecution.setPayerId(payerId);
      Payment payment = executePayment(paymendId, payerId, azEntapiContext);
      System.out.println(payment.toJSON());
      if (payment.getState().equals("approved")) {
        logger.info("PAYPAL INFO: Payment is approved!");
        return "success";
      }
    } catch (PayPalRESTException e) {
      logger.info(e.getMessage());
    }
    return "redirect:/";
  }

  @SuppressWarnings("deprecation")
  public Payment executePayment(String paymentId, String payerId, APIContext apiContext)
      throws PayPalRESTException {
    Payment payment = new Payment();
    payment.setId(paymentId);

    PaymentExecution paymentExecute = new PaymentExecution();
    paymentExecute.setPayerId(payerId);
    return payment.execute(apiContext, paymentExecute);
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Exception> handelAllPaymentExceptions(RuntimeException ex) {
    return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
