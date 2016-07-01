package es.babel.model;

public class DataModel {
	
	private Integer id;
	private Integer userId;
	private Integer groupId;
	private Boolean privateLayout;
	private Integer parentLayoutId;
	private String name;
	private String title;
	private String description;
	private String keywords;
	private String robots;
	private String type;
	private String typeSettings;
	private Boolean hidden;
	private String friendlyURL;
	
	public DataModel(Integer id, Integer userId, Integer groupId, Boolean privateLayout, Integer parentLayoutId, String name, String title,
			String description, String keywords, String robots, String type, String typeSettings, Boolean hidden,
			String friendlyURL) {
		super();
		this.id = id;
		this.userId = userId;
		this.groupId = groupId;
		this.privateLayout = privateLayout;
		this.parentLayoutId = parentLayoutId;
		this.name = name;
		this.title = title;
		this.description = description;
		this.keywords = keywords;
		this.robots = robots;
		this.type = type;
		this.typeSettings = typeSettings;
		this.hidden = hidden;
		this.friendlyURL = friendlyURL;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Boolean getPrivateLayout() {
		return privateLayout;
	}

	public void setPrivateLayout(Boolean privateLayout) {
		this.privateLayout = privateLayout;
	}

	public Integer getParentLayoutId() {
		return parentLayoutId;
	}

	public void setParentLayoutId(Integer parentLayoutId) {
		this.parentLayoutId = parentLayoutId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getRobots() {
		return robots;
	}

	public void setRobots(String robots) {
		this.robots = robots;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeSettings() {
		return typeSettings;
	}

	public void setTypeSettings(String typeSettings) {
		this.typeSettings = typeSettings;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getFriendlyURL() {
		return friendlyURL;
	}

	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}

	@Override
	public String toString() {
		return "DataModel [id=" + id + ", userId=" + userId + ", groupId=" + groupId + ", privateLayout="
				+ privateLayout + ", parentLayoutId=" + parentLayoutId + ", name=" + name + ", title=" + title
				+ ", description=" + description + ", keywords=" + keywords + ", robots=" + robots + ", type=" + type
				+ ", typeSettings=" + typeSettings + ", hidden=" + hidden + ", friendlyURL=" + friendlyURL + "]";
	}
	
}
