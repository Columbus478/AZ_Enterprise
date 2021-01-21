/**
 * 
 */
package com.example.AZ_Enterprise.Controller;

import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.AZ_Enterprise.Service.TransactionService;
import com.example.AZ_Enterprise.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@RestController
@RequestMapping("/api")
public class TransactionDetailController {
  private static final Logger logger = LogManager.getLogger(TransactionDetailController.class);
  @Autowired
  TransactionService transactionService;

  @RequestMapping("/transactions/days/{days}")
  public String getTransactionsByDays(@PathVariable(name = "days") String days) {
    Set<Transaction> transdetails = transactionService.getTransDeatailsByDays(days);
    if (transdetails == null) {
      logger.info("Error with getting Transaction details");
      return "redirect:/";
    }
    String jsonString = "";
    ObjectMapper mapper = new ObjectMapper();
    // Converting the Object to JSONString
    try {
      jsonString = mapper.writeValueAsString(transdetails);
      return jsonString;
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
    }
    logger.info("Transaction details received succesfully");

    return jsonString;
  }

  @RequestMapping("/transaction/delete/{tnumber}")
  public String deleteCustomer(@PathVariable(name = "tnumber") String tnumber) {
    if (transactionService.daleteTransDetail(tnumber) >= 1) {
      logger.info("Transaction detail with transaction id {}  has been deleted successfully.",
          tnumber);
      return "redirect:/";
    }
    return "redirect:/";
  }
}
