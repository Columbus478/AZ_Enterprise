/**
 * 
 */
package com.example.AZ_Enterprise.PaymentGateWay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.AZ_Enterprise.Service.CustomerService;
import com.example.AZ_Enterprise.Service.TransactionService;
import com.example.AZ_Enterprise.Utility.CharacterGenerator;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
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
  @Value("${sandbox.paypal.authURL}")
  private String authURL;
  private static String executionMode = "sandbox";
  @Autowired
  TransactionService transactionService;
  @Autowired
  CustomerService customerService;

  // This is simple API call which will capture a specified amount for any given
  // Payer or User
  public com.example.AZ_Enterprise.model.Transaction AZEntCapturePayPalAPI(String paymentID,
      String amount) {
    com.example.AZ_Enterprise.model.Transaction transaction = null;
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
    // Total must be equal to sum of shipping, tax and subtotal.
    payLoadAmount.setTotal(amount);
    payLoadAmount.setDetails(paymentDetails);

    // Set Transaction information
    Transaction azEntTransaction = new Transaction();
    azEntTransaction.setAmount(payLoadAmount);
    azEntTransaction
        .setDescription("AZ Enterprise Transaction with PayPal REST API using Java Client.");

    // Add transaction to a list
    List<Transaction> azEntTransactions = new ArrayList<Transaction>();
    azEntTransactions.add(azEntTransaction);

    // Add Payment details
    Payment azEntPayment = new Payment();

    // Set Payment intent to authorize
    azEntPayment.setIntent("sale");
    azEntPayment.setPayer(azEntPayer);
    azEntPayment.setTransactions(azEntTransactions);
    azEntPayment.setRedirectUrls(azEntRedirectUrls);

    // Create Payment
    try {
      // Pass the clientID, secret and mode. The easiest, and most widely used option.
      APIContext azEntapiContext = new APIContext(paypalClient_id, client_secret, executionMode);
      Payment myPayment = azEntPayment.create(azEntapiContext);
      Iterator links = myPayment.getLinks().iterator();
      while (links.hasNext()) {
        Links link = (Links) links.next();
        if (link.getRel().equalsIgnoreCase("approval_url")) {
          // Redirect the customer to link.getHref()
        }
      }
      // Save this transaction for the current logged in customer
      transaction = new com.example.AZ_Enterprise.model.Transaction();
      // Get customer ID from logged in user - Get a mock customer ID as we are test the API end
      // points
      String username = null;
      String custId = null;
      username = "A" + CharacterGenerator.charGenerator();
      // check if custID exists in the DB
      custId = customerService.getCustIDByUsername(username);
      if (custId == null) {
        username = "A" + CharacterGenerator.charGenerator();
        custId = customerService.getCustIDByUsername(username);
      }
      transaction.setAcnumber(custId);
      Date date = new Date();
      java.sql.Timestamp ct = new java.sql.Timestamp(date.getTime());
      transaction.setCreate_time(ct);
      transaction.setPayerId(myPayment.getId());
      transaction.setState(myPayment.getState());
      // Convert USD to NGN
      transaction.setTransaction_amount(Double.parseDouble(amount) * 380.00);
      // Save this transaction details to DB
      if (transactionService.saveTransDetails(transaction) > 0) {
        logger.info("Transaction Details saved successfully.");
      }
      System.out.println("createdPayment Obejct Details ==> " + myPayment.toString());
      // Identifier of the payment resource created
      azEntPayment.setId(myPayment.getId());

      PaymentExecution azentPaymentExecution = new PaymentExecution();

      // Set your PayerID. The ID of the Payer, passed in the `return_url` by PayPal.
      azentPaymentExecution.setPayerId(paymentID);

      // This call will fail as user has to access Payment on UI. Programmatically
      // there is no way you can get Payer's consent.
      Payment createdAuthPayment = azEntPayment.execute(azEntapiContext, azentPaymentExecution);

      // Transactional details including the amount and item details.
      Authorization azAentAuthorization = createdAuthPayment.getTransactions().get(0)
          .getRelatedResources().get(0).getAuthorization();

      logger.info("Here is your Authorization ID:{}", azAentAuthorization.getId());
      // Show Authorization Payment Details
      showAuthPaymentDetails(azAentAuthorization.getId());

    } catch (PayPalRESTException e) {
      // The "standard" error output stream. This stream is already open and ready to
      // accept output data.
      logger.error(e.getDetails());
    }
    return transaction;
  }

  // Show details for authorized payment
  public void showAuthPaymentDetails(String authID) {
    String clienID_secret = paypalClient_id + ":" + client_secret;
    System.setProperty("https.protocols", "TLSv1.1,TLSv1.2,SSLv3,SSLv2Hello");
    URL url;
    try {
      url = new URL(authURL + authID);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Authorization",
          "Basic " + new String(new Base64().encode(clienID_secret.getBytes())));
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty("Accept-Language", "en_US");
      conn.setUseCaches(false);
      conn.setDoOutput(true);

      // Read the response:
      BufferedReader reader =
          new java.io.BufferedReader(new InputStreamReader(conn.getInputStream()));
      if (conn.getResponseCode() == 200) {
        logger.info("Received Response: {}", reader.readLine());
      } else {
        logger.error("Error with getting Auth Payment Details.");
        logger.info("Response code:{}", conn.getResponseCode());
        logger.info("Response message:{}", conn.getResponseMessage());
      }
      reader.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
