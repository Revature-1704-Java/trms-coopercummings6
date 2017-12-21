import java.io.Serializable;
import java.util.Date;

public class RequestForm implements Serializable
{
	/**
	 * RequestForm is a bean to store froms for reimbursement requests.
	 */
	private static final long serialVersionUID = -5475718354903135081L;
	int requestID;
	int requesterID;
	Date dateSubmitted;
	String location;
	String gradingFormat;
	String eventType;
	String description;
	double cost;
	double workTimeMissed;
	String attachmentPath;
	Date finalDate;
	String finalGrade;
	boolean SupervisorApproval;
	boolean depHeadApproval;
	boolean bCoordinatorApproval;
	
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
	public Date getDateSubmitted() {
		return dateSubmitted;
	}
	public void setDateSubmitted(Date dateSubmitted) {
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
	public Date getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}
	public String getFinalGrade() {
		return finalGrade;
	}
	public void setFinalGrade(String finalGrade) {
		this.finalGrade = finalGrade;
	}
	public boolean isSupervisorApproval() {
		return SupervisorApproval;
	}
	public void setSupervisorApproval(boolean supervisorApproval) {
		SupervisorApproval = supervisorApproval;
	}
	public boolean isDepHeadApproval() {
		return depHeadApproval;
	}
	public void setDepHeadApproval(boolean depHeadApproval) {
		this.depHeadApproval = depHeadApproval;
	}
	public boolean isbCoordinatorApproval() {
		return bCoordinatorApproval;
	}
	public void setbCoordinatorApproval(boolean bCoordinatorApproval) {
		this.bCoordinatorApproval = bCoordinatorApproval;
	}
}
