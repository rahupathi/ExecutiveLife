package elife.entity;

public class InboxMain {
	
	private String MessageCount="";
	private String ReceivedOn = "";
	private String DayName = "";
    public String getMessageCount() {
		return MessageCount;
	}
	public void setMessageCount(String _MessageCount) {
		MessageCount = _MessageCount;
	}
	public String getReceivedOn() {
		return ReceivedOn;
	}
	public void setReceivedOn(String _ReceivedOn) {
		ReceivedOn = _ReceivedOn;
	}
	public String getDayName() {
		return DayName;
	}
	public void setDayName(String _DayName) {
		DayName = _DayName;
	}
}
