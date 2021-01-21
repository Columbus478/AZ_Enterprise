/**
 * 
 */
package com.example.AZ_Enterprise.Repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.AZ_Enterprise.model.Transaction;

/**
 * @author Samuel Columbus Jan 21, 2021
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
  @Query(
      value = "SELECT * FROM trandetails t WHERE t.acnumber = :acnumber order by t.create_time desc limit 5",
      nativeQuery = true)
  public Set<Transaction> getTransDeatailsByAcNum(@Param("acnumber") String acnumber);

  @Query(
      value = "SELECT * FROM trandetails WHERE create_time > DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY create_time",
      nativeQuery = true)
  public Set<Transaction> getTransDeatailsByDays(@Param("days") String days);

  @Query(value = "SELECT t.tnumber FROM trandetails t WHERE t.tnumber = :tnumber",
      nativeQuery = true)
  public String getTransDeatailsIDByTransId(@Param("tnumber") String tnumber);

  @Modifying
  @Query(
      value = "INSERT INTO trandetails (acnumber, create_time, payer_id, state, transaction_amount) values (:acnumber, :create_time, :payer_id, :state, :transaction_amount)",
      nativeQuery = true)
  public int saveTransDetails(@Param("acnumber") String acnumber, @Param("payer_id") String payerId,
      @Param("state") String state, @Param("transaction_amount") double transaction_amount,
      @Param("create_time") String create_time);

  @Modifying
  @Query(value = "DELETE FROM trandetails WHERE payerId = :payerId", nativeQuery = true)
  public int daleteTransDetail(@Param("payerId") String payerId);
}
