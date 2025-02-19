package com.app.dto;

public class FollowUpLeads {
	private Long id;
	private String clientName;
	private String clientEmail;
	private String nextFollowUpDate;
	private String ourContactSource;
	
	public FollowUpLeads() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FollowUpLeads(String clientName, String clientEmail, String nextFollowUpDate,
			String ourContactSource) {
		super();
		
		this.clientName = clientName;
		this.clientEmail = clientEmail;
		this.nextFollowUpDate = nextFollowUpDate;
		this.ourContactSource = ourContactSource;
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

	public String getNextFollowUpDate() {
		return nextFollowUpDate;
	}

	public void setNextFollowUpDate(String nextFollowUpDate) {
		this.nextFollowUpDate = nextFollowUpDate;
	}

	public String getOurContactSource() {
		return ourContactSource;
	}

	public void setOurContactSource(String ourContactSource) {
		this.ourContactSource = ourContactSource;
	}

	@Override
	public String toString() {
		return "FollowUpLeads [id=" + id + ", clientName=" + clientName + ", clientEmail=" + clientEmail
				+ ", nextFollowUpDate=" + nextFollowUpDate + ", ourContactSource=" + ourContactSource + "]";
	}

}
