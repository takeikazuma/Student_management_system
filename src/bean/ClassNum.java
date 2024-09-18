package bean;

import java.io.Serializable;

public class ClassNum implements Serializable {
	/**
	 * 学校:School
	 */
	private School school;

	/**
	 * クラス番号:String
	 */
	private String num;

	/**
	 * ゲッター、セッター
	 */
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}
