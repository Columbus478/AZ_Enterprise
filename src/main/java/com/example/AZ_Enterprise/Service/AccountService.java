/**
 * 
 */
package com.example.AZ_Enterprise.Service;

import java.sql.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.AZ_Enterprise.Repository.AccountRepository;
import com.example.AZ_Enterprise.model.Account;

/**
 * @author Samuel Columbus Jan 21, 2020
 */
@Service
@Transactional
public class AccountService {
  @Autowired
  AccountRepository accountRepository;

  public int saveAccount(String acnumber, String username, String bid, int opening_balance,
      int current_balance, Date aod, String atype, String astatus) {
    return accountRepository.saveAccount(acnumber, username, opening_balance, current_balance, aod,
        atype, astatus);
  }

  public int updateAccount(String acnumber, String username, int current_balance) {
    return accountRepository.updateAccount(acnumber, username, current_balance);
  }

  public Account getAccountByAccountNum(String acnumber) {
    return accountRepository.getAccountByAccountNum(acnumber);
  }

  public Account getAccountByCustID(String username) {
    return accountRepository.getAccountByUsername(username);
  }

  public int deleteAccount(String username) {
    return accountRepository.deleteAccount(username);
  }
}
