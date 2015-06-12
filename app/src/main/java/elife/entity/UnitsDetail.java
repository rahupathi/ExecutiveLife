package elife.entity;

public class UnitsDetail {
	private String UnitID="";
	private String UnitCode = "";
    private String StreetAddress = "";
    private String EstateAgentID = "";
    private String SuburbName="";
    private String PostalCode="";
    private String SupplierName="";
    private String ContactName="";
    
	public String getUnitId() {
		return UnitID;
	}
	public void setUnitId(String _UnitID) {
		UnitID = _UnitID;
	}

	public String getUnitCode() {
		return UnitCode;
	}
	public void setUnitCode(String _UnitCode) {
		UnitCode = _UnitCode;
	}
	
	public String getStreetAddress() {
		return StreetAddress;
	}
	
	public void setStreetAddress(String _StreetAddress) {
		StreetAddress = _StreetAddress;
	}
	
	public String getEstateAgentID() {
		return EstateAgentID;
	}
	
	public void setEstateAgentID(String _EstateAgentID) {
		EstateAgentID = _EstateAgentID;
	}
	
	public String getSuburbNames() {
		return SuburbName;
	}
	
	public void setSuburbName(String _SuburbName) {
		SuburbName = _SuburbName;
	}
	
	public String getPostalCode() {
		return PostalCode;
	}
	
	public void setPostalCode(String _PostalCode) {
		PostalCode = _PostalCode;
	}
	
	public String getSupplierName() {
		return SupplierName;
	}
	
	public void setSupplierName(String _SupplierName) {
		SupplierName = _SupplierName;
	}
	public String getContactNamer() {
		return ContactName;
	}
	
	public void setBillProvider(String _ContactName) {
		ContactName = _ContactName;
	}
}
