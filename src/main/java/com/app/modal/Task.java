package com.app.modal;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private String clientSourceURL;

	private boolean isAssign;
	private boolean isDeleted;
	private boolean isCompleted;
	private String status;

	@ManyToMany
	@JoinTable(name = "task_user", // Junction table name
			joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Task(Long id, String clientName, String clientEmail, String clientPhoneNumber, String clientWebsite,
			String clientCompanyName, String requirementType, String source, String clientCountry,
			String ourContactSource, String leadGenerationDate, String lastResponseDate, String nextFollowUpDate,
			String comments, boolean isAssign, boolean isDeleted, boolean isCompleted, String status, Set<User> users) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.clientEmail = clientEmail;
		this.clientPhoneNumber = clientPhoneNumber;
		this.clientWebsite = clientWebsite;
		this.clientCompanyName = clientCompanyName;
		this.requirementType = requirementType;
		this.source = source;
		this.clientCountry = clientCountry;
		this.ourContactSource = ourContactSource;
		this.leadGenerationDate = leadGenerationDate;
		this.lastResponseDate = lastResponseDate;
		this.nextFollowUpDate = nextFollowUpDate;
		this.comments = comments;
		this.isAssign = isAssign;
		this.isDeleted = isDeleted;
		this.isCompleted = isCompleted;
		this.status = status;
		this.users = users;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public String getClientSourceURL() {
		return clientSourceURL;
	}

	public void setClientSourceURL(String clientSourceURL) {
		this.clientSourceURL = clientSourceURL;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", clientName=" + clientName + ", clientEmail=" + clientEmail + ", clientPhoneNumber="
				+ clientPhoneNumber + ", clientWebsite=" + clientWebsite + ", clientCompanyName=" + clientCompanyName
				+ ", requirementType=" + requirementType + ", source=" + source + ", clientCountry=" + clientCountry
				+ ", ourContactSource=" + ourContactSource + ", leadGenerationDate=" + leadGenerationDate
				+ ", lastResponseDate=" + lastResponseDate + ", nextFollowUpDate=" + nextFollowUpDate + ", comments="
				+ comments + ", clientSourceURL"+ clientSourceURL +", isAssign=" + isAssign + ", isDeleted=" + isDeleted + ", isCompleted=" + isCompleted
				+ ", status=" + status + ", users=" + users + "]";
	}

}
