CREATE TABLE customer (
id int(11) NOT NULL AUTO_INCREMENT,
firstName varchar(255) DEFAULT NULL,
lastName varchar(255) DEFAULT NULL,
username varchar(255) DEFAULT NULL;
email varchar(255) DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT username UNIQUE(username)
)
INSERT INTO customer VALUES('C00001','A','B','AB','ab@gmail.com');
INSERT INTO customer VALUES('C00002','A','C','AC','ac@gmail.com');
private String cust_username;
  private int opening_balance;
  private int current_balance;
  private Date aod;
  private String atype;
  private String astatus;
INSERT INTO account VALUES('AB',1000.00,235000.00,'2012-12-15','Saving','Active');
INSERT INTO account VALUES('AC',1000.00,235000,00'2012-06-12','Saving','Active');