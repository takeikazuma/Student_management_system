package bean;

import java.io.Serializable;
import java.sql.Date;
import java.time.ZonedDateTime;

public class Absence implements Serializable{

	private Integer absenceId;	// DBに追加する際はnullなので、intではなくIntegerにする
	private int gradeClassId;
	private Date startDate;
	private Date endDate;
	private ZonedDateTime regDate;
	private ZonedDateTime updateDate;

	public Integer getAbsenceId() {
		return absenceId;
	}
	public void setAbsenceId(Integer absenceId) {
		this.absenceId = absenceId;
	}
	public int getGradeClassId() {
		return gradeClassId;
	}
	public void setGradeClassId(int gradeClassId) {
		this.gradeClassId = gradeClassId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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