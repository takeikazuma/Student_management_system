package action;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Instruction;
import bean.Student;
import dao.InstructionDao;
import dao.StudentDao;
import tool.Action;


public class ExportInstructionAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//データの取得
		//ローカル変数の宣言
		int student_id;
		String student_id_str = "";

		InstructionDao instructionDao = new InstructionDao();
		StudentDao studentDao = new StudentDao();
		List<Instruction> instruction_list = null;
		List<Student> student_list = null;

		//リクエストパラメータ―の取得 2
		student_id_str = req.getParameter("student_id_hidden");
		student_id = Integer.parseInt(student_id_str);	//String→int

		//学生番号の存在チェック
		student_list = studentDao.getStudent(student_id);

		//リクエストに学生番号をセット
		req.setAttribute("studentId", student_id_str);

		//データが存在しない場合
		if (student_list.size() == 0) {
			// エラーメッセージをセット
			req.setAttribute("message", "出力対象のデータが存在しません。");

		}else{

			//指導表テーブルからデータ取得
			instruction_list = instructionDao.getInstruction(student_id);	//指導表データ取得

			if (instruction_list.size() != 0) { // データが取得された場合
				// リクエストに指導表データをセット
				req.setAttribute("instruction_list", instruction_list);

				//カンマ
				final String COMMA = ",";
				//改行
				final String NEW_LINE= "\r\n";

				String studentName = student_list.get(0).getStudentName();

				//学生氏名をリクエストに設定
				if (!student_list.isEmpty()) {
				    req.setAttribute("studentName", studentName);
				}

				//CSV出力処理
		        String filename = "指導表.csv";

//		        res.setHeader("Content-Type", "text/csv; charset=UTF-8");
		        res.setHeader("Content-Type", "text/csv; charset=Shift_JIS");
		        res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(studentName +  "_"+ filename, "UTF-8") + "\"");

		        PrintWriter out = res.getWriter();

		        //ヘッダを作る
		        out.append("学生番号,学生氏名,日付,入力者,内容");
		        out.append(NEW_LINE);

		        //データ取得分行を作成する
		        for (Instruction i : instruction_list) {
		        	out.append(addDoubleQutation(student_id_str));
		        	out.append(COMMA);
		        	out.append(addDoubleQutation(i.getStudentName()));
		        	out.append(COMMA);
		        	out.append(addDoubleQutation(String.valueOf(i.getInputDate())));
		        	out.append(COMMA);
		        	out.append(addDoubleQutation(i.getUsersName()));
		        	out.append(COMMA);
		        	out.append(addDoubleQutation(i.getInstructions()));
		        	out.append(NEW_LINE);
		        }

		        out.close();
			}

		}

	}


	private String addDoubleQutation(String content){

		//ダブルクォーテーション
		final String DQ = "\"";
		return DQ + content + DQ;

	}

}
