/**
 * 
 */
package com.example.AZ_Enterprise.Controller;

import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.AZ_Enterprise.Service.TransactionService;
import com.example.AZ_Enterprise.Utility.JSONParser;
import com.example.AZ_Enterprise.model.Transaction;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@Controller
@RequestMapping("/api")
public class TransactionDetailController {
  private static final Logger logger = LogManager.getLogger(TransactionDetailController.class);
  @Autowired
  TransactionService transactionService;

  @ResponseBody
  @GetMapping(path = "/transactions/days/{days}")
  public String getTransactionsByDays(@PathVariable(name = "days") String days) {
    Set<Transaction> transdetails = transactionService.getTransDeatailsByDays(days);
    if (transdetails == null) {
      logger.info("Error with getting Transaction details");
      throw new RuntimeException("Error with getting Transaction details.");
    }
    String jsonString = JSONParser.Object_to_JSONString(transdetails);
    logger.info("Specified Transaction details are received succesfully:{}", jsonString);
    return jsonString;
  }

  @ResponseBody
  @GetMapping(path = "/transactions/days")
  public String getTransactionsByDays() {
    // No day is passed, Get All trans
    Set<Transaction> transdetails = null;
    transdetails = transactionService.getAllTransDeatails();
    if (transdetails == null) {
      logger.info("Error with getting Transaction details");
      throw new RuntimeException("Error with getting Transaction details.");
    }
    String jsonString = JSONParser.Object_to_JSONString(transdetails);
    logger.info("Transaction details received succesfully:{}", jsonString);
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

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Exception> handelAllTransactionExceptions(RuntimeException ex) {
    return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
