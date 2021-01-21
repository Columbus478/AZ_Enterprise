/**
 * 
 */
package com.example.AZ_Enterprise.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Samuel Columbus Jan 21, 2020
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "acnumber")
  private Long acnumber;
  @Column(unique = true)
  private String cust_username;
  private int opening_balance;
  private int current_balance;
  private Date aod;
  private String atype;
  private String astatus;
}
