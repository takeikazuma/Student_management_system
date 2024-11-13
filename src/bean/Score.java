package bean;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Score implements Serializable{

	private int scoreId;
	private int studentId;
	private String subjectCode;
	private int scoreMonth;
	private int scoreValue;
	private ZonedDateTime regDate;//	private datetime regDate;
	private ZonedDateTime updateDate;//	private datetime updateDate;
	private Student student;//落合追加
	private Subject subject;//落合追加

	public int getScoreId() {
		return scoreId;
	}
	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public int getScoreMonth(){
		return  scoreMonth;
	}
	public void setScoreMonth(int scoreMonth) {
		this.scoreMonth = scoreMonth;
	}

	public int getScoreValue(){
		return  scoreValue;
	}
	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
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
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}