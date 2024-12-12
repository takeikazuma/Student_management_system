package bean;

import java.io.Serializable;
//datetimeの変わりにZonedDateTimeを使用するためのライブラリ(落合)
//タイムゾーン情報を含む日付と時間を表すクラス
//（例: 2023-10-12T15:30+09:00[Asia/Tokyo]）
import java.time.ZonedDateTime;


public class Users implements Serializable{

	private int usersId;
	private String usersName;
	private String password;
	private int gradeClassId;
	private String gradeClassName;
	private ZonedDateTime regDate;//	private datetime regDate;
	private ZonedDateTime updateDate;//	private datetime updateDate;

	public int getUsersId() {
		return usersId;
	}
	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

// 必要(落合)
	/**
	 * 認証済みフラグ:boolean true:認証済み
	 */
	private boolean isAuthenticated;

	/**
	 * ゲッター、セッター
	 */
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}