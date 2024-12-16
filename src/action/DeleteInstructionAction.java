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

		//リクエストパラメタ用
		String studentIdStr = "";
		int studentId;
		String instructionIdStr = "";
		int instructionId = 0;

		InstructionDao instructionDao = new InstructionDao();

		//リクエストパラメータ―の取得 2
		//学生番号
		studentIdStr = req.getParameter("studentIdHidden");
		studentId = Integer.parseInt(studentIdStr);	//String→int
		//指導表番号
		instructionIdStr = req.getParameter("instructionId");
		instructionId =  Integer.parseInt(instructionIdStr);		//String→int

		Instruction instruction = new Instruction();
		instruction.setInstructionId(instructionId);

		//指導表テーブルから該当データを削除
		if (!(instructionDao.deleteInstruction(instruction))){
			//削除失敗時
	        req.setAttribute("message", "指導表の削除処理に失敗しました。");
	        return;

		}else{
			//正常終了した場合
			List<Instruction> instructionList = null;

			//指導表テーブルからデータ取得
			instructionList = instructionDao.getInstruction(studentId);

			if (instructionList.size() != 0) { // データが取得された場合
				// リクエストに指導表データをセット
				req.setAttribute("instruction_list", instructionList);

				//学生氏名をリクエストにセット
				String studentName = instructionList.get(0).getStudentName();
			    req.setAttribute("studentName", studentName);
			}
		}

		//リクエストに学生番号、学生氏名をセット
		req.setAttribute("student_id", studentIdStr);

		req.getRequestDispatcher("instruction.jsp").forward(req, res);

	}

}
