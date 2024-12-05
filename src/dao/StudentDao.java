package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
