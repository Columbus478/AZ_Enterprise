/**
 * 
 */
package com.example.AZ_Enterprise.PaymentGateWay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.AZ_Enterprise.Service.TransactionService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@Service
public class AZPayPalSDK {
  private static final Logger logger = LogManager.getLogger(AZPayPalSDK.class);
  @Value("${sandbox.paypal.client-id}")
  private String paypalClient_id;
  @Value("${sandbox.paypal.secret}")
  private String client_secret;
  private static String executionMode = "sandbox";
  @Autowired
  TransactionService transactionService;

  // This is simple API call which will capture a specified amount for any given
  // Payer or User
  public void AZEntCapturePayPalAPI(String amount) {

    Payer azEntPayer = new Payer();
    azEntPayer.setPaymentMethod("paypal");

    // Redirect URLs
    RedirectUrls azEntRedirectUrls = new RedirectUrls();
    azEntRedirectUrls.setCancelUrl("http://localhost:8080/AZEnterprise/api/pay/cancel");
    azEntRedirectUrls.setReturnUrl("http://localhost:8080/AZEnterprise/api");
    // Set Payment Details Object
    Details paymentDetails = new Details();
    String shippingFee = String.valueOf(Float.parseFloat(amount + "f") * 0.05);
    String taxPayable = String.valueOf(Float.parseFloat(amount + "f") * 0.02);
    String subTotal = String.valueOf(Float.parseFloat(amount + "f") - Float.parseFloat(shippingFee)
        - Float.parseFloat(taxPayable));
    paymentDetails.setShipping(shippingFee);
    paymentDetails.setSubtotal(subTotal);
    paymentDetails.setTax(taxPayable);

    // Set Payment amount
    Amount payLoadAmount = new Amount();
    payLoadAmount.setCurrency("USD");
    payLoadAmount.setTotal(amount);
    payLoadAmount.setDetails(paymentDetails);

    // Set Transaction information
    Transaction azEntTransaction = new Transaction();
    azEntTransaction.setAmount(payLoadAmount);
    azEntTransaction
        .setDescription("AZ Enterprise Transaction with PayPal REST API using Java Client.");
    List<Transaction> azEntTransactions = new ArrayList<Transaction>();
    azEntTransactions.add(azEntTransaction);

    // Add Payment details
    Payment azEntPayment = new Payment();

    // Set Payment intent to authorize
    azEntPayment.setIntent("authorize");
    azEntPayment.setPayer(azEntPayer);
    azEntPayment.setTransactions(azEntTransactions);
    azEntPayment.setRedirectUrls(azEntRedirectUrls);

    // Pass the clientID, secret and mode. The easiest, and most widely used option.
    APIContext azEntapiContext = new APIContext(paypalClient_id, client_secret, executionMode);

    try {

      Payment myPayment = azEntPayment.create(azEntapiContext);
      // Save this transaction for the current logged in customer
      // I will used customer AB account info but I will get the customer username from
      // SpringSecurityContext in
      // future
      // AB account ID:
      com.example.AZ_Enterprise.model.Transaction transaction =
          new com.example.AZ_Enterprise.model.Transaction();
      transaction.setAcnumber("C0001");
      Date date = new Date();
      java.sql.Timestamp ct = new java.sql.Timestamp(date.getTime());
      transaction.setCreate_time(ct);
      transaction.setPayerId(myPayment.getId());
      transaction.setState(myPayment.getState());
      // Convert USD to NGN
      transaction.setTransaction_amount(Double.parseDouble(amount) * 380.00);
      // Save this transaction details to DB
      if (transactionService.saveTransDetails(transaction) >= 1) {
        logger.info("Transaction Details saved successfully.");
      }
      System.out.println("createdPayment Obejct Details ==> " + myPayment.toString());

      // Identifier of the payment resource created
      azEntPayment.setId(myPayment.getId());

      PaymentExecution azentPaymentExecution = new PaymentExecution();

      // Set your PayerID. The ID of the Payer, passed in the `return_url` by PayPal.
      azentPaymentExecution.setPayerId(myPayment.getId());

      // This call will fail as user has to access Payment on UI. Programmatically
      // there is no way you can get Payer's consent.
      Payment createdAuthPayment = azEntPayment.execute(azEntapiContext, azentPaymentExecution);

      // Transactional details including the amount and item details.
      Authorization azAentAuthorization = createdAuthPayment.getTransactions().get(0)
          .getRelatedResources().get(0).getAuthorization();

      logger.info("Here is your Authorization ID:{}", azAentAuthorization.getId());

    } catch (PayPalRESTException e) {

      // The "standard" error output stream. This stream is already open and ready to
      // accept output data.
      logger.error(e.getDetails());
    }
  }

}
