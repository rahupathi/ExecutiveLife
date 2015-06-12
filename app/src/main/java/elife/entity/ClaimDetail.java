package elife.entity;

public class ClaimDetail {
	private String SCHEDULE_DATE="";
	private String CLAIM_NO = "";
    private String SERVICE_FROM = "";
    private String SERVICE_TO = "";
    private String CLAIM_STATUS="";
    private String CLOSED_DATE="";
    private String REND_PROV_NAME="";
    private String BILL_PROV_NAME="";
    
	public String getScheduleDate() {
		return SCHEDULE_DATE;
	}
	public void setScheduleDate(String _SCHEDULE_DATE) {
		SCHEDULE_DATE = _SCHEDULE_DATE;
	}

	public String getClaimNo() {
		return CLAIM_NO;
	}
	public void setClaimNo(String _CLAIM_NO) {
		CLAIM_NO = _CLAIM_NO;
	}
	
	public String getServiceFrom() {
		return SERVICE_FROM;
	}
	
	public void setServiceFrom(String _SERVICE_FROM) {
		SERVICE_FROM = _SERVICE_FROM;
	}
	
	public String getServiceTo() {
		return SERVICE_TO;
	}
	
	public void setServiceTo(String _SERVICE_TO) {
		SERVICE_TO = _SERVICE_TO;
	}
	
	public String getClaimStatus() {
		return CLAIM_STATUS;
	}
	
	public void setClaimStatus(String _CLAIM_STATUS) {
		CLAIM_STATUS = _CLAIM_STATUS;
	}
	
	public String getCloseDate() {
		return CLOSED_DATE;
	}
	
	public void setCloseDate(String _CLOSE_DATE) {
		CLOSED_DATE = _CLOSE_DATE;
	}
	
	public String getRendProvider() {
		return REND_PROV_NAME;
	}
	
	public void setRendProvider(String _REND_PROV_NAME) {
		REND_PROV_NAME = _REND_PROV_NAME;
	}
	public String getBillProvider() {
		return BILL_PROV_NAME;
	}
	
	public void setBillProvider(String _BILL_PROV_NAME) {
		BILL_PROV_NAME = _BILL_PROV_NAME;
	}
}
