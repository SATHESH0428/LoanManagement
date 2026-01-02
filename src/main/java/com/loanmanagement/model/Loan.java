package com.loanmanagement.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Loan {
	 private long id;
	    private String loanAccountNo;
	    private long customerId;
	    private String loanType;
	    private BigDecimal principalAmount;
	    private BigDecimal interestRate;
	    private int tenureMonths;
	    private String status;
	    private Timestamp createdDate;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getLoanAccountNo() {
			return loanAccountNo;
		}
		public void setLoanAccountNo(String loanAccountNo) {
			this.loanAccountNo = loanAccountNo;
		}
		public long getCustomerId() {
			return customerId;
		}
		public void setCustomerId(long customerId) {
			this.customerId = customerId;
		}
		public String getLoanType() {
			return loanType;
		}
		public void setLoanType(String loanType) {
			this.loanType = loanType;
		}
		public BigDecimal getPrincipalAmount() {
			return principalAmount;
		}
		public void setPrincipalAmount(BigDecimal principalAmount) {
			this.principalAmount = principalAmount;
		}
		public BigDecimal getInterestRate() {
			return interestRate;
		}
		public void setInterestRate(BigDecimal interestRate) {
			this.interestRate = interestRate;
		}
		public int getTenureMonths() {
			return tenureMonths;
		}
		public void setTenureMonths(int tenureMonths) {
			this.tenureMonths = tenureMonths;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Timestamp getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Timestamp createdDate) {
			this.createdDate = createdDate;
		}
	    

}
