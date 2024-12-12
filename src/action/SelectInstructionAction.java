/**
 * @author k_arita
 *
 */
package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Instruction;
import bean.Student;
import dao.InstructionDao;
import dao.StudentDao;
import tool.Action;


//学生番号に紐づく指導表データを取得する
public class SelectInstructionAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//ローカル変数の宣言
		String url = "";
		int student_id;
		String student_id_str = "";

		InstructionDao instructionDao = new InstructionDao();
		StudentDao studentDao = new StudentDao();
		List<Instruction> instruction_list = null;
		List<Student> student_list = null;

		//リクエストパラメータ―の取得 2
		student_id_str = req.getParameter("student_id");
		student_id = Integer.parseInt(student_id_str);	//String→int

		//学生番号の存在チェック
		student_list = studentDao.getStudent(student_id);

		//リクエストに学生番号をセット
		req.setAttribute("studentId", student_id_str);

		//データが存在しない場合
		if (student_list.size() == 0) {

			// エラーメッセージをセット
			req.setAttribute("message", "入力された学生番号は存在しません。");

		}else{

			//学生氏名をリクエストに設定
			if (!student_list.isEmpty()) {
			    String studentName = student_list.get(0).getStudentName();
			    req.setAttribute("studentName", studentName);
			}

			//指導表テーブルからデータ取得 3
			instruction_list = instructionDao.getInstruction(student_id);	//指導表データ取得

			if (instruction_list.size() != 0) { // データが取得された場合
				// リクエストに指導表データをセット
				req.setAttribute("instruction_list", instruction_list);

			}

		}

		//フォワード
		url = "instruction.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}

}
