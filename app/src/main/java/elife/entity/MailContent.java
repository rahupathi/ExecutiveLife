package elife.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MailContent implements Serializable {

	private String mMailContent="";
	private String mMailSubject = "";
	private String mFromMailId = "";
    
	public String getMailContent() {
		return mMailContent;
	}
	public void setMailContent(String _mail_content) {
		mMailContent = _mail_content;
	}

	public String getMailSubject() {
		return mMailSubject;
	}
	public void setMailSubject(String _mMailSubject) {
		mMailSubject = _mMailSubject;
	}
	
	public String getFromMailId() {
		return mFromMailId;
	}
	
	public void setFromMailId(String _mFromMailId) {
		mFromMailId= _mFromMailId;
	}
	
}
