package elife.entity;

public class MessageDetail {
	
	private String MessageDate="";
	private String Messages="";
	private String MessageType = "";
	private String Subject = "";
    private String Status="";
    private String PostDate = "";
    private String ReplyDate = "";
    
    public String getMessageDate() {
		return MessageDate;
	}
	public void setMessageDate(String _messageDate) {
		MessageDate = _messageDate;
	}

	public String getMessages() {
		return Messages;
	}
	public void setMessages(String _messages) {
		Messages = _messages;
	}
	
	public String getMessageType() {
		return MessageType;
	}
	public void setMessageType(String _messageType) {
		MessageType = _messageType;
	}
		
	public String getStatus() {
		return Status;
	}
	public void setStatus(String _status) {
		Status = _status;
	}
	
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String _subject) {
		Subject = _subject;
	}
	
	public String getPostDate() {
		return PostDate;
	}
	public void setPostDate(String _postDate) {
		Status = _postDate;
	}
	
	public String getReplyDate() {
		return ReplyDate;
	}
	public void setReplyDate(String _replyDate) {
		ReplyDate = _replyDate;
	}
	
	
	
		
}
