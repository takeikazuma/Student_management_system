package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends DAO {
	/**
	 * getメソッド 学生番号を指定して学生インスタンスを1件取得する
	 *
	 * @param no:String
	 *            学生番号
	 * @return 学生クラスのインスタンス 存在しない場合はnull
	 * @throws Exception
	 */
	public Student get(String no) throws Exception {
		// 学生インスタンスを初期化
		Student student = new Student();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select * from student where no=?");
			// プリペアードステートメントに学生番号をバインド
			statement.setString(1, no);
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// 学校Daoを初期化
			SchoolDao schoolDao = new SchoolDao();

			if (rSet.next()) {
				// リザルトセットが存在する場合
				// 学生インスタンスに検索結果をセット
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				// 学校フィールドには学校コードで検索した学校インスタンスをセット
				student.setSchool(schoolDao.get(rSet.getString("school_cd")));
			} else {
				// リザルトセットが存在しない場合
				// 学生インスタンスにnullをセット
				student = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return student;
	}

	/**
	 * baseSql:String 共通SQL文 プライベート
	 */
	private String baseSql = "select * from student where school_cd=? ";

	/**
	 * postFilterメソッド フィルター後のリストへの格納処理 プライベート
	 *
	 * @param rSet:リザルトセット
	 * @param school:School
	 *            学校
	 * @return 学生のリスト:List<Student> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
	private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
		// リストを初期化
		List<Student> list = new ArrayList<>();
		try {
			// リザルトセットを全権走査
			while (rSet.next()) {
				// 学生インスタンスを初期化
				Student student = new Student();
				// 学生インスタンスに検索結果をセット
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(school);
				// リストに追加
				list.add(student);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * filterメソッド 学校、入学年度、クラス番号、在学フラグを指定して学生の一覧を取得する
	 *
	 * @param school:School
	 *            学校
	 * @param entYear:int
	 *            入学年度
	 * @param classNum:String
	 *            クラス番号
	 * @param isAttend:boolean
	 *            在学フラグ
	 * @return 学生のリスト:List<Student> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
		// リストを初期化
		List<Student> list = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// リザルトセット
		ResultSet rSet = null;
		// SQL文の条件
		String condition = "and ent_year=? and class_num=?";
		// SQL文のソート
		String order = " order by no asc";

		// SQL文の在学フラグ条件
		String conditionIsAttend = "";
		// 在学フラグがtrueの場合
		if (isAttend) {
			conditionIsAttend = "and is_attend=true";
		}

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
			// プリペアードステートメントに学校コードをバインド
			statement.setString(1, school.getCd());
			// プリペアードステートメントに入学年度をバインド
			statement.setInt(2, entYear);
			// プリペアードステートメントにクラス番号をバインド
			statement.setString(3, classNum);
			// プライベートステートメントを実行
			rSet = statement.executeQuery();
			// リストへの格納処理を実行
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return list;
	}

	/**
	 * filterメソッド 学校、入学年度、在学フラグを指定して学生の一覧を取得する
	 *
	 * @param school:School
	 *            学校
	 * @param entYear:int
	 *            入学年度
	 * @param isAttend:boolean
	 *            在学フラグ
	 * @return 学生のリスト:List<Student> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
//	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
//		// リストを初期化
//		List<Student> list = new ArrayList<>();
//		// コネクションを確立
//		Connection connection = getConnection();
//		// プリペアードステートメント
//		PreparedStatement statement = null;
//		// リザルトセット
//		ResultSet rSet = null;
//		// SQL文の条件
//		String condition = "and ent_year=? ";
//		// SQL文のソート
//		String order = " order by no asc";
//
//		// SQL文の在学フラグ
//		String conditionIsAttend = "";
//		// 在学フラグがtrueだった場合
//		if (isAttend) {
//			conditionIsAttend = "and is_attend=true";
//		}
//
//		try {
//			// プリペアードステートメントにSQL文をセット
//			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
//			// プリペアードステートメントに学校コードをバインド
//			statement.setString(1, school.getCd());
//			// プリペアードステートメントに入学年度をバインド
//			statement.setInt(2, entYear);
//			// プリペアードステートメントを実行
//			rSet = statement.executeQuery();
//			// リストへの格納処理を実行
//			list = postFilter(rSet, school);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			// プリペアードステートメントを閉じる
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//			// コネクションを閉じる
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//		}
//
//		return list;
//	}

	/**
	 * filterメソッド 学校、在学フラグを指定して学生の一覧を取得する
	 *
	 * @param school:School
	 *            学校
	 * @param isAttend:boolean
	 *            在学フラグ
	 * @return 学生のリスト:List<Student> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
//	public List<Student> filter(School school, boolean isAttend) throws Exception {
//		// リストを初期化
//		List<Student> list = new ArrayList<>();
//		// コネクションを確立
//		Connection connection = getConnection();
//		// プリペアードステートメント
//		PreparedStatement statement = null;
//		// リザルトセット
//		ResultSet rSet = null;
//		// SQL文の条件
//		String order = " order by no asc";
//
//		// SQL文の在学フラグ
//		String conditionIsAttend = "";
//		// 在学フラグがtrueの場合
//		if (isAttend) {
//			conditionIsAttend = "and is_attend=true";
//		}
//
//		try {
//			// プリペアードステートメントにSQL文をセット
//			statement = connection.prepareStatement(baseSql + conditionIsAttend + order);
//			// プリペアードステートメントに学校コードをバインド
//			statement.setString(1, school.getCd());
//			// プリペアードステートメントを実行
//			rSet = statement.executeQuery();
//			// リストへの格納処理を実行
//			list = postFilter(rSet, school);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			// プリペアードステートメントを閉じる
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//			// コネクションを閉じる
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//		}
//
//		return list;
//	}

	/**
	 * saveメソッド 学生インスタンスをデータベースに保存する データが存在する場合は更新、存在しない場合は登録
	 *
	 * @param student：Student
	 *            学生
	 * @return 成功:true, 失敗:false
	 * @throws Exception
	 */
//	public boolean save(Student student) throws Exception {
//		// コネクションを確立
//		Connection connection = getConnection();
//		// プリペアードステートメント
//		PreparedStatement statement = null;
//		// 実行件数
//		int count = 0;
//
//		try {
//			// データベースから学生を取得
//			Student old = get(student.getNo());
//			if (old == null) {
//				// 学生が存在しなかった場合
//				// プリペアードステートメンにINSERT文をセット
//				statement = connection.prepareStatement(
//						"insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)");
//				// プリペアードステートメントに値をバインド
//				statement.setString(1, student.getNo());
//				statement.setString(2, student.getName());
//				statement.setInt(3, student.getEntYear());
//				statement.setString(4, student.getClassNum());
//				statement.setBoolean(5, student.isAttend());
//				statement.setString(6, student.getSchool().getCd());
//			} else {
//				// 学生が存在した場合
//				// プリペアードステートメントにUPDATE文をセット
//				statement = connection
//						.prepareStatement("update student set name=?, ent_year=?, class_num=?, is_attend=? where no=?");
//				// プリペアードステートメントに値をバインド
//				statement.setString(1, student.getName());
//				statement.setInt(2, student.getEntYear());
//				statement.setString(3, student.getClassNum());
//				statement.setBoolean(4, student.isAttend());
//				statement.setString(5, student.getNo());
//			}
//
//			// プリペアードステートメントを実行
//			count = statement.executeUpdate();
//
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			// プリペアードステートメントを閉じる
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//			// コネクションを閉じる
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//		}
//
//		if (count > 0) {
//			// 実行件数が1件以上ある場合
//			return true;
//		} else {
//			// 実行件数が0件の場合
//			return false;
//		}
//	}

	/**
	 * deleteメソッド 学生をデータベースから削除する
	 *
	 * @param student:Student
	 * @return 成功:true, 失敗:false
	 * @throws Exception
	 */
//	public boolean delete(Student student) throws Exception {
//		// コネクションを確立
//		Connection connection = getConnection();
//		// プリペアードステートメント
//		PreparedStatement statement = null;
//		// 実行件数
//		int count = 0;
//
//		try {
//			// プリペアードステートメントにDELETE文をセット
//			statement = connection.prepareStatement("delete from student where no=?");
//			// プリペアードステートメントに学生番号をバインド
//			statement.setString(1, student.getNo());
//			// プリペアードステートメントを実行
//			count = statement.executeUpdate();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			// プリペアードステートメントを閉じる
//			if (statement != null) {
//				try {
//					statement.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//			// コネクションを閉じる
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (SQLException sqle) {
//					throw sqle;
//				}
//			}
//		}
//
//		if (count > 0) {
//			// 実行件数が1件以上ある場合
//			return true;
//		} else {
//			// 実行件数が0件の場合
//			return false;
//		}
//	}
}
