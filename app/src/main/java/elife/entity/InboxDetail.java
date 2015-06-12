package elife.entity;

public class InboxDetail {
	
	private String MessageId="";
	private String ParentId="";
	private String CustomerId = "";
	private String UnitId = "";
    private String ReceiverId="";
    private String ReceivedOn = "";
    private String SenderId = "";
    private String Subject = "";
    private String MsgDescription = "";
    private String Tenant_name="";
    private String ReceivedTime="";
    
    public String getMessageId() {
		return MessageId;
	}
	public void setMessageId(String _MessageId) {
		MessageId = _MessageId;
	}
	public String getTenantName() {
		return Tenant_name;
	}
	public void setTenantName(String _TenantName) {
		Tenant_name = _TenantName;
	}
	
	public String getParentId() {
		return ParentId;
	}
	public void setParentId(String _ParentId) {
		ParentId = _ParentId;
	}
	
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String _CustomerId) {
		CustomerId = _CustomerId;
	}
		
	public String getUnitCode() {
		return UnitId;
	}
	public void setUnitCode(String _UnitId) {
		UnitId = _UnitId;
	}
	
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String _subject) {
		Subject = _subject;
	}
	
	public String getReceiverId() {
		return ReceiverId;
	}
	public void setReceiverId(String _ReceiverId) {
		ReceiverId = _ReceiverId;
	}
	
	public String getSenderId() {
		return SenderId;
	}
	public void setSenderId(String _SenderId) {
		SenderId = _SenderId;
	}
	
	public String getMsgDescription() {
		return MsgDescription;
	}
	public void setMsgDescription(String _MsgDescription) {
		MsgDescription = _MsgDescription;
	}
	public String getReceivedDate() {
		return ReceivedOn;
	}
	public void setReceivedDate(String _ReceivedDate) {
		ReceivedOn = _ReceivedDate;
	}
	public String getReceivedTime() {
		return ReceivedTime;
	}
	public void setReceivedTime(String _ReceivedTime) {
		ReceivedTime = _ReceivedTime;
	}
}
