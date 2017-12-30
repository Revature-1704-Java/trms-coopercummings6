package com.revature.trms;
import java.io.Serializable;
import java.sql.Timestamp;

public class RequestForm implements Serializable
{
	/**
	 * RequestForm is a bean to store forms for reimbursement requests.
	 */
	private static final long serialVersionUID = -5475718354903135081L;
	int requestID;
	int requesterID;
	Timestamp dateSubmitted;
	String location;
	String gradingFormat;
	String eventType;
	String description;
	double cost = -1;
	double workTimeMissed = -1;
	String attachmentPath;
	Timestamp finalDate;
	String finalGrade;
	Boolean SupervisorApproval;
	Boolean depHeadApproval;
	Boolean bCoordinatorApproval;
	String denialReason;
	public String getDenialReason() {
		return denialReason;
	}
	public void setDenialReason(String denialReason) {
		this.denialReason = denialReason;
	}
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public int getRequesterID() {
		return requesterID;
	}
	public void setRequesterID(int requesterID) {
		this.requesterID = requesterID;
	}
	public Timestamp getDateSubmitted() {
		return dateSubmitted;
	}
	public void setDateSubmitted(Timestamp dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getGradingFormat() {
		return gradingFormat;
	}
	public void setGradingFormat(String gradingFormat) {
		this.gradingFormat = gradingFormat;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getWorkTimeMissed() {
		return workTimeMissed;
	}
	public void setWorkTimeMissed(double workTimeMissed) {
		this.workTimeMissed = workTimeMissed;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public Timestamp getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(Timestamp finalDate) {
		this.finalDate = finalDate;
	}
	public String getFinalGrade() {
		return finalGrade;
	}
	public void setFinalGrade(String finalGrade) {
		this.finalGrade = finalGrade;
	}
	public Boolean isSupervisorApproval() {
		return SupervisorApproval;
	}
	public void setSupervisorApproval(Boolean supervisorApproval) {
		SupervisorApproval = supervisorApproval;
	}
	public Boolean isDepHeadApproval() {
		return depHeadApproval;
	}
	public void setDepHeadApproval(Boolean depHeadApproval) {
		this.depHeadApproval = depHeadApproval;
	}
	public Boolean isbCoordinatorApproval() {
		return bCoordinatorApproval;
	}
	public void setbCoordinatorApproval(Boolean bCoordinatorApproval) {
		this.bCoordinatorApproval = bCoordinatorApproval;
	}
}
