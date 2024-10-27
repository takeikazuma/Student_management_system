package bean;

import java.io.Serializable;
//datetimeの変わりにZonedDateTimeを使用するためのライブラリ(落合)
//タイムゾーン情報を含む日付と時間を表すクラス
//（例: 2023-10-12T15:30+09:00[Asia/Tokyo]）
import java.time.ZonedDateTime;

public class GradeClass implements Serializable{

	private int gradeClassId;
	private String gradeClassName;
	private ZonedDateTime regDate;//	private datetime regDate;
	private ZonedDateTime updateDate;//	private datetime updateDate;

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