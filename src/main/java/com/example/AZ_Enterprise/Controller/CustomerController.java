/**
 * 
 */
package com.example.AZ_Enterprise.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.AZ_Enterprise.Service.CustomerService;
import com.example.AZ_Enterprise.model.Customer;

/**
 * @author Samuel Columbus Jan 20, 2021
 */
@RestController
@Repository("/api")
public class CustomerController {
  @Autowired
  CustomerService customerService;

  @GetMapping
  public List<Customer> getCustomers() {
    return customerService.getAllCustomers();
  }

  @GetMapping("/customer/{id}")
  public Customer getCustomer(@PathVariable Long id) {
    return customerService.getCustomerById(id);
  }

  @PostMapping("/customer")
  public Customer saveCustomer(final @RequestBody Customer customer) {
    return customerService.saveCustomer(customer);
  }

  @DeleteMapping("/customer/{id}")
  public Boolean deleteCustomer(@PathVariable Long id) {
    return customerService.deleteCustomer(id);
  }
}
