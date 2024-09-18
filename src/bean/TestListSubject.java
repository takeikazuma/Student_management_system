package bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestListSubject implements Serializable {
	/**
	 * 入学年度:int
	 */
	private int entYear;

	/**
	 * 学生番号:String
	 */
	private String studentNo;

	/**
	 * 学生氏名:String
	 */
	private String studentName;

	/**
	 * クラス番号:String
	 */
	private String classNum;

	/**
	 * 得点マップ:Map<Integer, Integer> key:回数, value:得点
	 */
	private Map<Integer, Integer> points;

	/**
	 * コンストラクタ
	 */
	public TestListSubject() {
		// 得点マップを初期化
		points = new HashMap<>();
	}

	/**
	 * ゲッター、セッター
	 */
	public int getEntYear() {
		return entYear;
	}

	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String student_no) {
		this.studentNo = student_no;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String student_name) {
		this.studentName = student_name;
	}

	public String getClassNum() {
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

	public Map<Integer, Integer> getPoints() {
		return points;
	}

	public void setPoints(Map<Integer, Integer> points) {
		this.points = points;
	}

	/**
	 * getPointメソッド 得点マップから値を取得する
	 *
	 * @param key:int
	 *            回数
	 * @return 得点:String
	 */
	public String getPoint(int key) {
		// 得点マップから値を取得
		Integer k = points.get(key);
		if (k == null) {
			// 得点マップに値が存在しなかった場合"-"を返却
			return "-";
		} else {
			// 得点マップに値が存在した場合、文字列として得点を返却
			return k.toString();
		}
	}

	/**
	 * putPointメソッド 得点マップに値を格納する
	 *
	 * @param key:int
	 *            回数
	 * @param value:int
	 *            得点
	 */
	public void putPoint(int key, int value) {
		// 得点マップに値を格納
		points.put(key, value);
	}
}
