package bean;

import java.io.Serializable;
//datetimeの変わりにZonedDateTimeを使用するためのライブラリ(落合)
//タイムゾーン情報を含む日付と時間を表すクラス
//（例: 2023-10-12T15:30+09:00[Asia/Tokyo]）
import java.time.ZonedDateTime;

public class Student implements Serializable{

	private int studentId;
	private int gradeClassId;
	private String gradeClassName;
	private int admissionYear;
	private String studentName;
	private String studentKana;
	private int schoolYear;
	private ZonedDateTime withdrawalDate;//	private datetime withdrawalDate;
	private boolean isEnrollment;
	private ZonedDateTime regDate;//	private datetime regDate;
	private ZonedDateTime updateDate;//	private datetime updateDate;

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

	public String getGradeClassName() {
		return gradeClassName;
	}
	public void setGradeClassName(String gradeClassName) {
		this.gradeClassName = gradeClassName;
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
	public void setStudentKana(String studentKana) {
		this.studentKana = studentKana;
	}

	public int getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(int schoolYear) {
		this.schoolYear = schoolYear;
	}

	public ZonedDateTime getWithdrawalDate() {
		return withdrawalDate;
	}
	public void setWithdrawalDate(ZonedDateTime withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	public boolean getIsEnrollment() {
		return isEnrollment;
	}
	public void setIsEnrollment(boolean isEnrollment) {
		this.isEnrollment = isEnrollment;
	}

	public ZonedDateTime getRegDate() {
		return regDate;
	}
	public void setRegDate(ZonedDateTime regDate) {
		this.regDate = regDate;
	}

	public ZonedDateTime getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

}