package es.chg.portal.requests.db;

import java.util.Date;

public class RequestVO {

	private long id;

	private byte[] docContent;

	private String pfDetailedStatus;

	private String pfReference;

	private Date registerDate;

	private String status;
	
	private TemplateVO template = new TemplateVO();
	
	public RequestVO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getDocContent() {
		return this.docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

	public String getPfDetailedStatus() {
		return this.pfDetailedStatus;
	}

	public void setPfDetailedStatus(String pfDetailedStatus) {
		this.pfDetailedStatus = pfDetailedStatus;
	}

	public String getPfReference() {
		return this.pfReference;
	}

	public void setPfReference(String pfReference) {
		this.pfReference = pfReference;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TemplateVO getTemplate() {
		return template;
	}

	public void setTemplate(TemplateVO template) {
		this.template = template;
	}
	
}