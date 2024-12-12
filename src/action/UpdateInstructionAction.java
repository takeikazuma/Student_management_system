package action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Instruction;
import bean.Users;
import dao.InstructionDao;
import tool.Action;

public class UpdateInstructionAction  extends Action  {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//ローカル変数の宣言 1
		InstructionDao instructionDao = new InstructionDao();

		Users users = null;

		//リクエストパラメタ用
		int student_id;
		String student_id_str = "";
		String input_date_str = "";
		String instruction_content = "";
		String instruction_id_str = "";
		int instruction_id =0;

		// セッション情報を取得
		HttpSession session = req.getSession(true);
		// セッションからログイン情報を取得
		Object sessionUser = session.getAttribute("users");
		// セッション情報をUsers型にキャスト
		users = (Users)sessionUser;

		//リクエストパラメータ―の取得 2
		student_id_str = req.getParameter("student_id_hidden");
		student_id = Integer.parseInt(student_id_str);	//String→int
		input_date_str = req.getParameter("input_date");
		instruction_content = req.getParameter("input_instructions");

		instruction_id_str = req.getParameter("instructionId");
		instruction_id =  Integer.parseInt(instruction_id_str);		//String→int

		// 日付の文字列を java.sql.Date に変換
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 日付のフォーマット
		java.util.Date utilDate = format.parse(input_date_str); // java.util.Date に変換
		Date input_date = new Date(utilDate.getTime()); // java.sql.Date に変換

		Instruction instruction = new Instruction();
		instruction.setInputDate(input_date);
		instruction.setUsersId(users.getUsersId());
		instruction.setInstructions(instruction_content);
		instruction.setStudentId(student_id);
		instruction.setInstructionId(instruction_id);

		//指導表テーブルに更新
		if (!(instructionDao.updateInstruction(instruction))){
			//エラーが発生した場合

		}else{
			//正常終了した場合

			List<Instruction> instruction_list = null;

			//指導表テーブルからデータ取得
			instruction_list = instructionDao.getInstruction(student_id);

			if (instruction_list.size() != 0) { // データが取得された場合
				// リクエストに指導表データをセット
				req.setAttribute("instruction_list", instruction_list);

				//学生氏名をリクエストにセット
				String studentName = instruction_list.get(0).getStudentName();
			    req.setAttribute("studentName", studentName);
			}

		}

		//リクエストに学生番号、学生氏名をセット
		req.setAttribute("studentId", student_id_str);


	}

}
