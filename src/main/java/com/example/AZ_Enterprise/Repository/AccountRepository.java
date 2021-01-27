/**
 * 
 */
package com.example.AZ_Enterprise.Repository;

import java.sql.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.AZ_Enterprise.model.Account;

/**
 * @author Samuel Columbus Jan 21, 2020
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
  @Modifying
  @Query(
      value = "INSERT INTO account (acnumber, cust_username, opening_balance, current_balance, aod, atype, astatus) values (:acnumber, :username, :opening_balance, :current_balance, :aod, :atype, :astatus)",
      nativeQuery = true)
  public int saveAccount(@Param("acnumber") String acnumber, @Param("username") String custid,
      @Param("opening_balance") int opening_balance, @Param("current_balance") int current_balance,
      @Param("aod") Date aod, @Param("atype") String atype, @Param("astatus") String astatus);

  @Query(value = "SELECT * FROM ACCOUNT a WHERE a.acnumber =:acnumber", nativeQuery = true)
  public Account getAccountByAccountNum(@Param("acnumber") String acnumber);

  @Query(value = "SELECT * FROM ACCOUNT a WHERE a.cust_username = :username", nativeQuery = true)
  public Account getAccountByUsername(@Param("username") String username);

  @Modifying
  @Query(
      value = "UPDATE account a SET a.current_balance = :current_balance WHERE a.acnumber = :acnumber AND a.custid = :custid",
      nativeQuery = true)
  public int updateAccount(@Param("acnumber") String acnumber, @Param("custid") String custid,
      @Param("current_balance") int current_balance);

  @Modifying
  @Query(value = "DELETE FROM ACCOUNT WHERE ACCOUNT.username = :username", nativeQuery = true)
  public int deleteAccount(@Param("username") String username);
}

