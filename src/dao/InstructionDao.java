package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Instruction;

public class InstructionDao extends DAO {
	/**
	 * filterメソッド 学生番号を指定して該当する指導表データを取得する
	 *
	 * @param student_id:学生番号
	 * @return 指導表リスト:List<Instruction> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
	public List<Instruction> getInstruction(int student_id) throws Exception {
		// リストを初期化
		List<Instruction> instructionList = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//SQL文字列
		String sqlString = "";
		//SQL文字列の代入
		sqlString = "SELECT " +
					"    INSTRUCTION.STUDENT_ID, " +
					"    STUDENT.STUDENT_NAME, " +
					"    INSTRUCTION.INSTRUCTION_ID, " +
					"    INSTRUCTION.INPUT_DATE, " +
					"    INSTRUCTION.USERS_ID, " +
					"    USERS.USERS_NAME, " +
					"    INSTRUCTION.INSTRUCTIONS " +
					"FROM INSTRUCTION " +
					"LEFT JOIN USERS " +
					"ON " +
					"    INSTRUCTION.USERS_ID = USERS.USERS_ID " +
					"LEFT JOIN STUDENT " +
					"ON " +
					"    INSTRUCTION.STUDENT_ID = STUDENT.STUDENT_ID " +
					"WHERE " +
					"    INSTRUCTION.STUDENT_ID = ? " +
					"ORDER BY " +
					"    INSTRUCTION.REG_DATE ASC ";

		try {

			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement(sqlString);

			// プリペアードステートメントに学生番号をバインド
			statement.setInt(1, student_id);

			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 指導表インスタンスの初期化
				Instruction instruction = new Instruction();
				// 指導表インスタンスに値をセット
				instruction.setInstructionId(rSet.getInt("INSTRUCTION_ID"));
				instruction.setInputDate(rSet.getDate("INPUT_DATE"));
				instruction.setUsersId(rSet.getInt("USERS_ID"));
				instruction.setUsersName(rSet.getString("USERS_NAME"));
				instruction.setInstructions(rSet.getString("INSTRUCTIONS"));
				instruction.setStudentName(rSet.getString("STUDENT_NAME"));

				// リストに追加
				instructionList.add(instruction);
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

		return instructionList;
	}

	public List<Instruction> getInstructionByInstructionId(int instruction_id) throws Exception {
		// リストを初期化
		List<Instruction> instructionList = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//SQL文字列
		String sqlString = "";
		//SQL文字列の代入
		sqlString = "SELECT * FROM PUBLIC.INSTRUCTION WHERE INSTRUCTION_ID = ?";

		try {

			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement(sqlString);

			// プリペアードステートメントに学生番号をバインド
			statement.setInt(1, instruction_id);

			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 指導表インスタンスの初期化
				Instruction instruction = new Instruction();
				// 指導表インスタンスに値をセット
				instruction.setInstructionId(rSet.getInt("INSTRUCTION_ID"));
				instruction.setInputDate(rSet.getDate("INPUT_DATE"));
				instruction.setUsersId(rSet.getInt("USERS_ID"));
				instruction.setInstructions(rSet.getString("INSTRUCTIONS"));

				// リストに追加
				instructionList.add(instruction);
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

		return instructionList;
	}


	public boolean saveInstruction(Instruction instruction) throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//更新件数
		int count = 0;
		//sql
		String sql = "INSERT INTO INSTRUCTION " +
				"(INPUT_DATE, USERS_ID, STUDENT_ID, INSTRUCTIONS, REG_DATE, UPDATE_DATE)  " +
				"VALUES " +
				"(?, ?, ?, ?, NOW(), NOW())";

		try {

			// プリペアードステートメンにINSERT文をセット
			statement = connection.prepareStatement(sql);
			// プリペアードステートメントに値をバインド
			statement.setDate(1, instruction.getInputDate());
			statement.setInt(2, instruction.getUsersId());
			statement.setInt(3, instruction.getStudentId());
			statement.setString(4, instruction.getInstructions());

			// プリペアードステートメントを実行
			count = statement.executeUpdate();


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

		if (count > 0) {
			// 実行件数が1件以上ある場合
			return true;
		} else {
			// 実行件数が0件の場合
			return false;
		}
	}


	public boolean updateInstruction(Instruction instruction) throws Exception {

		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//更新件数
		int count = 0;
		//sql
		String sql = "UPDATE " +
					"	INSTRUCTION " +
					"SET  " +
					"	INPUT_DATE=?,  " +
					"	USERS_ID=?,  " +
					"	INSTRUCTIONS=?,  " +
					"	UPDATE_DATE=NOW() " +
					"WHERE  " +
					"	INSTRUCTION_ID=? ";
		try {

			// プリペアードステートメンにINSERT文をセット
			statement = connection.prepareStatement(sql);
			// プリペアードステートメントに値をバインド
			statement.setDate(1, instruction.getInputDate());
			statement.setInt(2, instruction.getUsersId());
			statement.setString(3, instruction.getInstructions());

			statement.setInt(4, instruction.getInstructionId());

			// プリペアードステートメントを実行
			count = statement.executeUpdate();


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

		if (count > 0) {
			// 実行件数が1件以上ある場合
			return true;
		} else {
			// 実行件数が0件の場合
			return false;
		}

	}

	public boolean deleteInstruction(Instruction instruction) throws Exception {

		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//更新件数
		int count = 0;
		//sql
		String sql = "DELETE FROM  " +
				"	INSTRUCTION " +
				"WHERE " +
				"	INSTRUCTION_ID = ? ";

		try {
			// プリペアードステートメンにINSERT文をセット
			statement = connection.prepareStatement(sql);
			// プリペアードステートメントに値をバインド
			statement.setInt(1, instruction.getInstructionId());
			// プリペアードステートメントを実行
			count = statement.executeUpdate();

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

		if (count > 0) {
			// 実行件数が1件以上ある場合
			return true;
		} else {
			// 実行件数が0件の場合
			return false;
		}

	}


}
