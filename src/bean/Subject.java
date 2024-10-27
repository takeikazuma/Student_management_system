package bean;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Subject implements Serializable{

	private int subjectId;
	private char subjectCode;
	private char subjectName;
	private int courseYear;
	private ZonedDateTime regDate;//	private datetime regDate;
	private ZonedDateTime updateDate;//	private datetime updateDate;

	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public char getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(char subjectCode) {
		this.subjectCode = subjectCode;
	}

	public char getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(char subjectName) {
		this.subjectName = subjectName;
	}

	public int getCourseYear(){
		return  courseYear;
	}
	public void setCourseYear(int courseYear) {
		this.courseYear = courseYear;
	}

	public ZonedDateTime getRegDate(){
		return  regDate;
	}
	public void setRegDate(ZonedDateTime regDate) {
		this.regDate = regDate;
	}

	public ZonedDateTime getUpdateDate(){
		return  updateDate;
	}
	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}
}