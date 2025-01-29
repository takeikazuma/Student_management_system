package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;

public class SubjectDao extends DAO {
	/**
	 *科目の一覧を取得
	 *
	 * @param なし
	 *
	 * @return 科目のリスト:List<Subject> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
	 public List<Subject> getSubject() throws Exception {
		// リストを初期化
		List<Subject> list = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement("select * from SUBJECT");
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 科目インスタンスの初期化
				Subject subject = new Subject();
				// 科目インスタンスに値をセット
				subject.setSubjectId(rSet.getInt("SUBJECT_ID"));
				subject.setSubjectCode(rSet.getString("SUBJECT_CODE"));
				subject.setSubjectName(rSet.getString("SUBJECT_NAME"));
				subject.setCourseYear(rSet.getInt("COURSE_YEAR"));
				// リストに追加
				list.add(subject);
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
	 *科目IDから科目名と科目コードを取得
	 *
	 * @param subject_id:int
	 *
	 * @return 科目:String 存在しない場合はnull
	 * @throws Exception
	 */
	 public Subject getSubjectNameCode(int subject_id) throws Exception {
		// Subjectインスタンスを初期化
		Subject subject = new Subject();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement("select * from SUBJECT where subject_id = ?");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1,subject_id);
			ResultSet rSet = statement.executeQuery();

			if (rSet.next()) {
				// リザルトセットが存在する場合
				// Usersインスタンスに検索結果をセット
				subject.setSubjectName(rSet.getString("subject_name"));
				subject.setSubjectCode(rSet.getString("subject_code"));
			} else {
				// リザルトセットが存在しない場合
				// Usersインスタンスにnullをセット
				subject = null;
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
		return subject;
	}
}
