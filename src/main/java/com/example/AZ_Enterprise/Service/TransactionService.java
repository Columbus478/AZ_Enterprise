/**
 * 
 */
package com.example.AZ_Enterprise.Service;

import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.AZ_Enterprise.Repository.TransactionRepository;
import com.example.AZ_Enterprise.model.Transaction;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@Service
@Transactional
public class TransactionService {
  @Autowired
  TransactionRepository transactionRepository;

  public Set<Transaction> getTransDeatailsByAcNum(String acnumber) {
    return transactionRepository.getTransDeatailsByAcNum(acnumber);
  }

  public String getTransDeatailsIDByTransId(String tnumber) {
    return transactionRepository.getTransDeatailsIDByTransId(tnumber);
  }

  public Set<Transaction> getTransDeatailsByDays(String days) {
    return transactionRepository.getTransDeatailsByDays(days);
  }

  public Set<Transaction> getAllTransDeatails() {
    return transactionRepository.getAllTransDeatails();
  }

  public int saveTransDetails(Transaction transaction) {
    return transactionRepository.saveTransDetails(transaction.getAcnumber(),
        transaction.getPayerId(), transaction.getState(), transaction.getTransaction_amount(),
        transaction.getCreate_time().toString());
  }

  public int daleteTransDetail(String tnumber) {
    return transactionRepository.daleteTransDetail(tnumber);
  }
}
