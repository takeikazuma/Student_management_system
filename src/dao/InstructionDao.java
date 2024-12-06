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
	public List<Instruction> getInstruction(String student_id) throws Exception {
		// リストを初期化
		List<Instruction> instructionList = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		//SQL文字列
		String sqlString = "";

		System.out.println("ここを通ったdao-1");

		//SQL文字列の代入
		sqlString = "SELECT " +
					"    INSTRUCTION.STUDENT_ID, " +
					"    INSTRUCTION.INSTRUCTION_ID, " +
					"    INSTRUCTION.INPUT_DATE, " +
					"    INSTRUCTION.USERS_ID, " +
					"    USERS.USERS_NAME, " +
					"    INSTRUCTION.INSTRUCTIONS, " +
					"    INSTRUCTION.REG_DATE " +
					"FROM INSTRUCTION " +
					"LEFT JOIN USERS " +
					"ON " +
					"    INSTRUCTION.USERS_ID = USERS.USERS_ID " +
					"WHERE " +
					"    INSTRUCTION.STUDENT_ID = ? " +
					"ORDER BY " +
					"    INSTRUCTION.REG_DATE ASC ";

		try {

			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement(sqlString);

			// プリペアードステートメントに学生番号をバインド
			statement.setInt(1, Integer.parseInt(student_id));

			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 指導表インスタンスの初期化
				Instruction instruction = new Instruction();

				// 指導表インスタンスに値をセット
				instruction.setInstructionId(rSet.getInt("INSTRUCTION_ID"));
//				日付の取得の方法後ほど確認
				instruction.setInputDate(rSet.getDate("INPUT_DATE"));
				instruction.setUsersId(rSet.getInt("USERS_ID"));
				instruction.setUsersName(rSet.getString("USERS_NAME"));
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
}
