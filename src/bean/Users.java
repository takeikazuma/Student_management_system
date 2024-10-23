package bean;

import java.io.Serializable;

public class Users implements Serializable{

	private int usersId;
	private String usersName;
	private String password;
	private int gradeClassId;
	private datetime regDate;
	private datetime updateDate;

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