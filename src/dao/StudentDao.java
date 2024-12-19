package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import bean.GradeClass;
import bean.Student;

public class StudentDao extends DAO {
	/**
	 *getClassメソッド クラス番号の一覧を取得する
	 *
	 * @param
	 * @return クラス番号の一覧:List<GradeClass>
	 * @throws Exception
	 */
	public List<GradeClass> getClassList() throws Exception {
	    // リストを初期化
	    List<GradeClass> list = new ArrayList<>();

		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

	    try {
	         	// プリペアードステートメントにSQL文をセット
	    		statement = connection.prepareStatement(
	             "SELECT GRADE_CLASS_ID, GRADE_CLASS_NAME FROM GRADE_CLASS ORDER BY GRADE_CLASS_ID");

	        // プリペアードステートメントを実行
	        ResultSet rSet = statement.executeQuery();

	        // リザルトセットを全件走査
	        while (rSet.next()) {
	            // 新しいGradeClassインスタンスを生成し、検索結果をセット
	            GradeClass gradeclass = new GradeClass();
	            gradeclass.setGradeClassId(rSet.getInt("GRADE_CLASS_ID"));
	            gradeclass.setGradeClassName(rSet.getString("GRADE_CLASS_NAME"));

	            // リストに追加
	            list.add(gradeclass);
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
	    return list;
	}

	/**
	 *getStudentメソッド 検索条件に一致する学生情報を取得する
	 *
	 * @param
	 * @return 学生情報:List<Student>
	 * @throws Exception
	 */
	public List<Student> getStudentList(String name, String kana, String gradeClass) throws Exception {
	    // リストを初期化
	    List<Student> list = new ArrayList<>();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

	    try {
         	// プリペアードステートメントにSQL文をセット
    		statement = connection.prepareStatement(
    		 "SELECT "
    		 + "student.student_id, grade_class.grade_class_name, student.student_name "
    		 + "FROM student INNER JOIN grade_class "
    		 + "ON student.grade_class_id = grade_class.grade_class_id "
    		 + "WHERE student.student_name LIKE '%" + name + "%' "
    		 + "AND student.student_kana LIKE '%" + kana + "%' "
    		 + "AND grade_class.grade_class_name LIKE '%" + gradeClass + "%' ");

    		// プリペアードステートメントを実行
    		ResultSet rSet = statement.executeQuery();

    		// リザルトセットを全件走査
    		while (rSet.next()) {
    			// 新しいGradeClassインスタンスを生成し、検索結果をセット
    			Student student = new Student();
    			student.setStudentId(rSet.getInt("STUDENT_ID"));
    			student.setGradeClassName(rSet.getString("GRADE_CLASS_NAME"));
    			student.setStudentName(rSet.getString("STUDENT_NAME"));

    			// リストに追加
    			list.add(student);
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
	    return list;
	}

	/***
	 * 年度、クラス名から学生情報を取得
	 *
	 * @param admission_year
	 *            年度
	 * @param class_name
	 *            クラス名
	 * @return 学生情報一覧:List<学生情報:Student>
	 * @throws Exception
	 */
	public List<Student> getStudent(int admission_year, String class_name)
			throws Exception {

		List<Student> studentList = new ArrayList<Student>();

		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"select * from STUDENT, GRADE_CLASS where STUDENT.GRADE_CLASS_ID = GRADE_CLASS.GRADE_CLASS_ID and ADMISSION_YEAR = ? and GRADE_CLASS.GRADE_CLASS_NAME like ? order by STUDENT_ID asc");
			statement.setInt(1, admission_year);
			statement.setString(2, class_name);

			// プリペアードステートメントを実行
			if (statement != null) {
				ResultSet rSet = statement.executeQuery();

				while (rSet.next()) {
					// リザルトセットが存在する場合
					Student student = new Student();
					student.setStudentId(rSet.getInt("student_id"));
					student.setGradeClassId(rSet.getInt("grade_class_id"));
					student.setAdmissionYear(rSet.getInt("admission_year"));
					student.setStudentName(rSet.getString("student_name"));
					student.setStudentKana(rSet.getString("student_kana"));
					student.setSchoolYear(rSet.getInt("school_year"));
					Date withdrawalDate = rSet.getDate("withdrawal_date");
					if(withdrawalDate != null) {
						student.setWithdrawalDate(withdrawalDate.toInstant().atZone(ZoneId.systemDefault()));
					}
					else {
						student.setWithdrawalDate(null);
					}
					student.setIsEnrollment(rSet.getBoolean("is_enrollment"));
					studentList.add(student);
				}
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

		return studentList;
	}

	/***
	 * 学生番号から学生情報を取得
	 *
	 * @param student_id
	 *            学生番号
	 * @return 学生情報一覧:List<学生情報:Student>
	 * @throws Exception
	 */
	public List<Student> getStudent(int student_id) throws Exception {

		// リストを初期化
	    List<Student> list = new ArrayList<>();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		String sql = "select * from STUDENT WHERE STUDENT_ID = ?";

	    try {
	    	statement = connection.prepareStatement(sql);
			statement.setInt(1, student_id);

    		// プリペアードステートメントを実行
    		ResultSet rSet = statement.executeQuery();

    		// リザルトセットを全件走査
    		while (rSet.next()) {
    			// 新しいGradeClassインスタンスを生成し、検索結果をセット
    			Student student = new Student();
    			student.setStudentId(rSet.getInt("student_id"));
				student.setGradeClassId(rSet.getInt("grade_class_id"));
				student.setAdmissionYear(rSet.getInt("admission_year"));
				student.setStudentName(rSet.getString("student_name"));
				student.setStudentKana(rSet.getString("student_kana"));
				student.setSchoolYear(rSet.getInt("school_year"));
				student.setIsEnrollment(rSet.getBoolean("is_enrollment"));

    			// リストに追加
    			list.add(student);
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
	    // データが無ければ、空のリストが返る
	    return list;
	}


}
