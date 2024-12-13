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

		//変数の宣言
		String url = "instruction.jsp";
		int studentId = 0;
		String studentIdStr = req.getParameter("student_id");

		//DAOインスタンス
		InstructionDao instructionDao = new InstructionDao();
		StudentDao studentDao = new StudentDao();
		List<Instruction> instructionList = null;
		List<Student> studentList = null;

		//学生番号の取得
		try {
		    studentId = Integer.parseInt(studentIdStr); // String→int

		} catch (NumberFormatException e) {
			//空欄で来た場合もこちら
		    req.setAttribute("message", "無効な学生番号が入力されました。");
		    req.getRequestDispatcher(url).forward(req, res);
		    return; //処理終了
		}

		//学生番号の存在チェック
		studentList = studentDao.getStudent(studentId);

		//学生番号が存在しない場合
		if (studentList.isEmpty()) {
		    req.setAttribute("message", "入力された学生番号は存在しません。");

		} else {
		    //学生氏名をリクエストに設定
		    String studentName = studentList.get(0).getStudentName();
		    req.setAttribute("studentName", studentName);

		    //指導表データを取得
		    instructionList = instructionDao.getInstruction(studentId);

		    //指導表データが存在する場合
		    if (!instructionList.isEmpty()) {
		    	//リクエストに該当データをセット
		        req.setAttribute("instruction_list", instructionList);
		    }
		}

		//学生番号をリクエストにセット
		req.setAttribute("student_id", studentIdStr);

		//フォワード
		req.getRequestDispatcher(url).forward(req, res);



	}

}
