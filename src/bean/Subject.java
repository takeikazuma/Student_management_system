package bean;

import java.io.Serializable;

public class Subject implements Serializable{

	private String subjectId;
	private String subjectCode;
	private String subjectName;
	private int courseYear;
	private datetime regDate;
	private datetime updateDate;


	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public int getCourseYear(){
		return  courseYear;
	}
	public void setCourseYear(int courseYear) {
		this.courseYear = courseYear;
	}

	public datetime getRegDate(){
		return  regDate;
	}
	public void setRegDate(datetime regDate) {
		this.regDate = regDate;
	}

	public datetime getUpdateDate(){
		return  updateDate;
	}
	public void setUpdateDate(datetime updateDate) {
		this.updateDate = updateDate;
	}
}