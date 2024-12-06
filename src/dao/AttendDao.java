package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bean.Attend;
import bean.Student;

public class AttendDao extends DAO {

	/**
	 * 出席状況を保存
	 * @param studen_id 学生番号
	 * @param attend_date 出欠日付
	 * @param status 出欠状況
	 * @return 正常に保存されたら true、失敗したら false
	 * @throws Exception
	 */
	public boolean saveAttend(int studen_id, String attend_date, int status) throws Exception {
		boolean result = false;
		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			// 登録する学生番号と出欠日付が既に存在していたら更新、無ければ追加する
			statement = connection.prepareStatement(
					"MERGE INTO attend AS target " +
					"USING (VALUES (?, ?::date, ?)) AS source(student_id, attend_date, attend_status) " +
					"ON target.student_id = source.student_id and target.attend_date = source.attend_date " +
					"WHEN MATCHED THEN UPDATE SET attend_status = source.attend_status, update_date = CURRENT_TIMESTAMP " +
					"WHEN NOT MATCHED THEN " +
					    "INSERT (student_id, attend_date, attend_status, reg_date, update_date)" +
					    "VALUES (source.student_id, source.attend_date, source.attend_status, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");

			statement.setInt(1, studen_id);
			statement.setTimestamp(2, Timestamp.valueOf(attend_date));
			statement.setInt(3, status);

			// プリペアードステートメントを実行
			int iSet = statement.executeUpdate();
			result = (iSet == 1);
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

		return result;
	}

	/**
	 *
	 * @param year 取得年
	 * @param month 取得月
	 * @param day 取得日(null:指定月全ての日を取得)
	 * @param student_list 取得学生リスト
	 * @return 学生出席状況リスト
	 * @throws Exception
	 */
	public List<Attend> getAttend(int year, int month, Integer day, List<Student> student_list) throws Exception {

		List<Attend> attendList = new ArrayList<Attend>();

		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			if(student_list.size() > 0) {
				// デフォルトは月単位で検索
				String ymd = String.format("%04d-%02d", year, month);
				String ymdFormat = "YYYY-MM";
				if(day != null) {
					// 日が指定されていれば、日指定で検索
					ymd = String.format("%04d-%02d-%02d", year, month, day);
					ymdFormat = "YYYY-MM-DD";
				}

				// プリペアードステートメントにSQL文をセット
				statement = connection.prepareStatement(
						"select * from ATTEND where to_char(ATTEND_DATE, '" + ymdFormat + "') = ? AND STUDENT_ID in (" +
						String.join(",", Collections.nCopies(student_list.size(), "?")) +
						")");

				statement.setString(1, ymd);
				int i = 2;
				for(Student student: student_list) {
					// プリペアードステートメントに学生IDをバインド
					statement.setInt(i, student.getStudentId()); // プレースホルダは1-based index
					i++;
				}
			}

			// プリペアードステートメントを実行
			if(statement != null) {
				ResultSet rSet = statement.executeQuery();

				while (rSet.next()) {
					Attend attend = new Attend();
					attend.setAttendId(rSet.getInt("ATTEND_ID"));
					attend.setStudentId(rSet.getString("STUDENT_ID"));
					attend.setAttendDate(rSet.getDate("ATTEND_DATE"));
					attend.setAttendStatus(rSet.getInt("ATTEND_STATUS"));
					attendList.add(attend);
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

		return attendList;
	}

}
