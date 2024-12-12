package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Instruction;
import dao.InstructionDao;
import tool.Action;


public class DeleteInstructionAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

			//ローカル変数の宣言 1
			InstructionDao instructionDao = new InstructionDao();

			//リクエストパラメタ用
			String student_id_str = "";
			int student_id;
			String instruction_id_str = "";
			int instruction_id = 0;

			//リクエストパラメータ―の取得 2
			//学生番号
			student_id_str = req.getParameter("student_id_hidden");
			student_id = Integer.parseInt(student_id_str);	//String→int
			//指導表番号
			instruction_id_str = req.getParameter("instructionId");
			instruction_id =  Integer.parseInt(instruction_id_str);		//String→int

			Instruction instruction = new Instruction();
			instruction.setInstructionId(instruction_id);

			//指導表テーブルから該当データを削除
			if (!(instructionDao.deleteInstruction(instruction))){
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
