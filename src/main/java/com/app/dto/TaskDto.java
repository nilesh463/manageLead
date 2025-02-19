package com.app.dto;

public class TaskDto {

	private Long id;
	private String clientName;
	private String clientEmail;
	private String clientPhoneNumber;
	private String clientWebsite;
	private String clientCompanyName;
	private String requirementType;
	private String source;
	private String clientCountry;
	private String ourContactSource;
	private String leadGenerationDate;
	private String lastResponseDate;
	private String nextFollowUpDate;
	private String comments;
	private String employeeAssinedName;
	private String employeeAssinedEmail;
	private String status;
	private String clientSourceURL;

	private boolean isAssign;
	private boolean isDeleted;
	private boolean isCompleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}

	public String getClientWebsite() {
		return clientWebsite;
	}

	public void setClientWebsite(String clientWebsite) {
		this.clientWebsite = clientWebsite;
	}

	public String getClientCompanyName() {
		return clientCompanyName;
	}

	public void setClientCompanyName(String clientCompanyName) {
		this.clientCompanyName = clientCompanyName;
	}

	public String getRequirementType() {
		return requirementType;
	}

	public void setRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}

	public String getOurContactSource() {
		return ourContactSource;
	}

	public void setOurContactSource(String ourContactSource) {
		this.ourContactSource = ourContactSource;
	}

	public String getLeadGenerationDate() {
		return leadGenerationDate;
	}

	public void setLeadGenerationDate(String leadGenerationDate) {
		this.leadGenerationDate = leadGenerationDate;
	}

	public String getLastResponseDate() {
		return lastResponseDate;
	}

	public void setLastResponseDate(String lastResponseDate) {
		this.lastResponseDate = lastResponseDate;
	}

	public String getNextFollowUpDate() {
		return nextFollowUpDate;
	}

	public void setNextFollowUpDate(String nextFollowUpDate) {
		this.nextFollowUpDate = nextFollowUpDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmployeeAssinedName() {
		return employeeAssinedName;
	}

	public void setEmployeeAssinedName(String employeeAssinedName) {
		this.employeeAssinedName = employeeAssinedName;
	}

	public String getEmployeeAssinedEmail() {
		return employeeAssinedEmail;
	}

	public void setEmployeeAssinedEmail(String employeeAssinedEmail) {
		this.employeeAssinedEmail = employeeAssinedEmail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAssign() {
		return isAssign;
	}

	public void setAssign(boolean isAssign) {
		this.isAssign = isAssign;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getClientSourceURL() {
		return clientSourceURL;
	}

	public void setClientSourceURL(String clientSourceURL) {
		this.clientSourceURL = clientSourceURL;
	}

	@Override
	public String toString() {
		return "TaskDto [id=" + id + ", clientName=" + clientName + ", clientEmail=" + clientEmail
				+ ", clientPhoneNumber=" + clientPhoneNumber + ", clientWebsite=" + clientWebsite
				+ ", clientCompanyName=" + clientCompanyName + ", requirementType=" + requirementType + ", source="
				+ source + ", clientCountry=" + clientCountry + ", ourContactSource=" + ourContactSource
				+ ", leadGenerationDate=" + leadGenerationDate + ", lastResponseDate=" + lastResponseDate
				+ ", nextFollowUpDate=" + nextFollowUpDate + ", comments=" + comments + ", employeeAssinedName="
				+ employeeAssinedName + ", employeeAssinedEmail=" + employeeAssinedEmail + ", status=" + status
				+ ", clientSourceURL=" + clientSourceURL + ", isAssign=" + isAssign + ", isDeleted=" + isDeleted
				+ ", isCompleted=" + isCompleted + "]";
	}

}
