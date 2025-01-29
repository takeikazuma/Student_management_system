package bean;

import java.sql.Date;
import java.time.ZonedDateTime;

public class Absence {
    private int absenceId;
    private int gradeClassId;   // クラスID
    private String className;    // クラス名
    private int admissionYear;   // 入学年度
    private Date startDate;      // 開始日
    private Date endDate;        // 終了日
    private ZonedDateTime regDate;   // 登録日時
    private ZonedDateTime updateDate; // 更新日時

    // Getter and Setter methods
    public int setAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(int absenceId) {
        this.absenceId = absenceId;
    }

    public int getGradeClassId() {
        return gradeClassId;
    }

    public void setGradeClassId(int gradeClassId) {
        this.gradeClassId = gradeClassId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
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

