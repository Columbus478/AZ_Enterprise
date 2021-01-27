/**
 * 
 */
package com.example.AZ_Enterprise.Service;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.AZ_Enterprise.Repository.CustomerRepository;
import com.example.AZ_Enterprise.model.Customer;

/**
 * @author Samuel Columbus Jan 20, 2021
 */
@Service
@Transactional
public class CustomerService {
  @Autowired
  CustomerRepository customerRepository;

  public Customer saveCustomer(Customer customer) {
    Customer customerModel = setToCustomerEntity(customer);
    return customerRepository.save(customerModel);
  }

  public boolean deleteCustomer(String customerId) {
    customerRepository.deleteById(customerId);
    return true;
  }

  public List<Customer> getAllCustomers() {
    List<Customer> customerList = customerRepository.findAll();
    return customerList;
  }

  public Customer getCustomerById(String customerId) {
    return customerRepository.findById(customerId)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
  }

  public String getCustIDByUsername(String username) {
    return customerRepository.getCustIDByUsername(username);
  }

  private Customer setToCustomerEntity(Customer customer) {
    Customer customerObj = new Customer();
    customerObj.setFirstName(customerObj.getFirstName());
    customerObj.setLastName(customerObj.getLastName());
    customerObj.setUsername(customerObj.getUsername());
    customerObj.setEmail(customerObj.getEmail());
    return customerObj;
  }
}
