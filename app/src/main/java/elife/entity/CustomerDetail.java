package elife.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomerDetail  implements Serializable{
	
	private String User_ID="";
	private String Customer_Id="";
	private String Tenant_name="";
	private String First_Name = "";
    private String Last_Name = "";
    private String Date_of_birth="";
    private String Email_ID="";
    private String Gender="";
    private String Mobile_Number="";
    private String Unit_Id="";
    private String Unit_Code="";
    
    
    public String getUnitCode() {
		return Unit_Code;
	}
    
	public void setUnitCode(String _unitCode) {
		Unit_Code = _unitCode;
	}
	
	public String getUnitId() {
		return Unit_Id;
	}
    
	public void setUnitId(String _unit_Id) {
		Unit_Id = _unit_Id;
	}
	
	
	public String getUserId() {
		return User_ID;
	}
	public void setUserId(String userId) {
		User_ID = userId;
	}

	public String getTenantName() {
		return Tenant_name;
	}
	public void setTenantName(String MemberName) {
		Tenant_name = MemberName;
	}
	
	public String getCustomerId() {
		return Customer_Id;
	}
	public void setCustomerId(String customerId) {
		Customer_Id = customerId;
	}
	
	public String getFirstName() {
		return First_Name;
	}
	public void setFirstName(String firstName) {
		First_Name = firstName;
	}
	
	public String getLastName() {
		return Last_Name;
	}
	public void setLastName(String lastname) {
		Last_Name = lastname;
	}

	public String getBirthDate() {
		return Date_of_birth;
	}
	public void setBirthDate(String birthDate) {
		Date_of_birth = birthDate;
	}

	public String getEmailID() {
		return Email_ID;
	}
	public void setEmailId(String relation) {
		Email_ID = relation;
	}
	
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	
	public String getMobileNumber() {
		return Mobile_Number;
	}
	public void setMobileNumber(String mobileNumber) {
		Mobile_Number = mobileNumber;
	}
	
	
}
