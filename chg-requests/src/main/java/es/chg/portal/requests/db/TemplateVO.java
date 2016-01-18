package es.chg.portal.requests.db;

public class TemplateVO {

	private long id;

	private int active;

	private String approvalCircuit;

	private byte[] content;

	private String name;

	private String version;

	public TemplateVO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getApprovalCircuit() {
		return this.approvalCircuit;
	}

	public void setApprovalCircuit(String approvalCircuit) {
		this.approvalCircuit = approvalCircuit;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}