package elife.entity;

public class ImageDetail {
	private String FILE_NAME="";
	private String FILE_DATE = "";
    private Boolean SELECTED = false;
    
	public String getFileName() {
		return FILE_NAME;
	}
	public void setFileName(String _File_Name) {
		FILE_NAME = _File_Name;
	}

	public String getFileDate() {
		return FILE_DATE;
	}
	public void setFileDate(String _File_Date) {
		FILE_DATE = _File_Date;
	}
	
	public Boolean getSelected() {
		return SELECTED;
	}
	
	public void setSelected(Boolean _Selected) {
		SELECTED= _Selected;
	}
	
}
