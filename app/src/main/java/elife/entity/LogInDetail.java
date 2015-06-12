package elife.entity;

public class LogInDetail {
	
	private String Member_SSN="";
	private String Member_Name="";
	private String Sec_Question1 = "";
	private String Sec_Answer1 = "";
    private String Member_System_Id="";
    private String User_Name="";
    private String Pass_word="";
    private String RegId="";
    
	public String getMemberSSN() {
		return Member_SSN;
	}
	public void setMemberSSN(String ssn) {
		Member_SSN = ssn;
	}

	public String getMemberName() {
		return Member_Name;
	}
	public void setMemberName(String MemberName) {
		Member_Name = MemberName;
	}
	public String getMemberSystemId() {
		return Member_System_Id;
	}
	public void setMemberSystemId(String memberSystemId) {
		Member_System_Id = memberSystemId;
	}
	
	public String getSecQuestion1() {
		return Sec_Question1;
	}
	public void setFirstName(String secQuestion1) {
		Sec_Question1 = secQuestion1;
	}
	
	public String getSecAnswer() {
		return Sec_Answer1;
	}
	public void setLastName(String secAnswer1) {
		Sec_Answer1 = secAnswer1;
	}
	public String getUserName() {
		return User_Name;
	}
	public void setUserName(String _userName) {
		User_Name =  _userName;
	}

	public String getPassword() {
		return Pass_word;
	}
	public void setPassword(String _password) {
		Pass_word = _password;
	}
	
	public String getRegId() {
		return RegId;
	}
	public void setRegId(String _regId) {
		RegId = _regId;
	}
	
}
