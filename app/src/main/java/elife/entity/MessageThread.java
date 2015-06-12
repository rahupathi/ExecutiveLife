package elife.entity;

public class MessageThread {
	
	private String Messages="";
	private String MessageId = "";
	private String Subject = "";
    private String Status="";
    private String PostDate = "";
    private String ReplyDate = "";
    

	public String getMessages() {
		return Messages;
	}
	public void setMessages(String _messages) {
		Messages = _messages;
	}
	
	public String getMessageId() {
		return MessageId;
	}
	public void setMessageType(String _messageId) {
		MessageId = _messageId;
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
		PostDate = _postDate;
	}
	
	public String getReplyDate() {
		return ReplyDate;
	}
	public void setReplyDate(String _replyDate) {
		ReplyDate = _replyDate;
	}
	
	
	
		
}
