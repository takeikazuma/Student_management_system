package bean;

import java.io.Serializable;

public class Student implements Serializable{

	private int studentId;
	private int gradeClassId;
	private int admissionYear;
	private String studentName;
	private String studentKana;
	private int schoolYear;
	private datetime withdrawalDate;
	private boolean isEnrollment;
	private datetime regDate;
	private datetime updateDate;

	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getGradeClassId() {
		return gradeClassId;
	}
	public void setGradeClassId(int gradeClassId) {
		this.gradeClassId = gradeClassId;
	}

	public int getAdmissionYear() {
		return admissionYear;
	}
	public void setAdmissionYear(int admissionYear) {
		this.admissionYear = admissionYear;
	}

	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentKana() {
		return studentKana;
	}
	public void setStudentKana(String studentkana) {
		this.studentKana = studentKana;
	}

	public int getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(int schoolYear) {
		this.schoolYear = schoolYear;
	}

	public datetime getWithdrawalDate() {
		return withdrawalDate;
	}
	public void setWithdrawalDate(datetime withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	public boolean getIsEnrollment() {
		return isEnrollment;
	}
	public void setIsEnrollment(boolean isEnrollment) {
		this.isEnrollment = isEnrollment;
	}

	public datetime getRegDate() {
		return regDate;
	}
	public void setRegDate(datetime regDate) {
		this.regDate = regDate;
	}

	public datetime getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(datetime updateDate) {
		this.updateDate = updateDate;
	}

}