/**
 * 
 */
package com.example.AZ_Enterprise.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AZ_Enterprise.model.Customer;

/**
 * @author Samuel Columbus Jan 20, 2021
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
