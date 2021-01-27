/**
 * 
 */
package com.example.AZ_Enterprise.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.AZ_Enterprise.model.Customer;

/**
 * @author Samuel Columbus Jan 20, 2021
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
  @Query(
      value = "select id from customer where customer.username in (SELECT account.cust_username from account where account.cust_username = ?)",
      nativeQuery = true)
  public String getCustIDByUsername(@Param("username") String username);
}
