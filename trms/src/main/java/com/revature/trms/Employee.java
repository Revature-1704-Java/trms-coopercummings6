package com.revature.trms;
import java.io.Serializable;

public class Employee implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String name;
	double claimsAmountRemaining;
	String eMail;
	String password;
	String employeeType;
	int employeeTypeID;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getClaimsAmountRemaining() {
		return claimsAmountRemaining;
	}
	public void setClaimsAmountRemaining(double claimsAmountRemaining) {
		this.claimsAmountRemaining = claimsAmountRemaining;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public int getEmployeeTypeID() {
		return employeeTypeID;
	}
	public void setEmployeeTypeID(int employeeTypeID) {
		this.employeeTypeID = employeeTypeID;
	}
	
	
}
