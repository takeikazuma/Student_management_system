package action;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import bean.Instruction;
import bean.Users;
import dao.InstructionDao;
import tool.Action;

public class RegistrationInstructionAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//変数宣言を整理
		InstructionDao instructionDao = new InstructionDao();
		Users users = null;

		//リクエストパラメータ
		int studentId = 0;
		String studentIdStr = req.getParameter("studentIdHidden");
		String inputDateStr = req.getParameter("inputDate");
		String instructionContent = req.getParameter("inputInstructions");

		//セッション情報を取得
		HttpSession session = req.getSession(true);
		Object sessionUser = session.getAttribute("users");

		//セッションユーザーが取得できない場合の処理
		if (sessionUser == null) {
		    req.setAttribute("message", "ログインしてください。");
		    req.getRequestDispatcher("login.jsp").forward(req, res);
		    return;
		}

		//セッション情報をUsers型にキャスト
		users = (Users) sessionUser;

		//学生IDの取得と変換
		try {
		    studentId = Integer.parseInt(studentIdStr); // String→int
		} catch (NumberFormatException e) {
			//日付が正しくなかった場合（空欄で来てしまった場合もこちら）
	        req.setAttribute("message", "日付の形式が正しくありません。");
		}

		// 日付の変換
		java.sql.Date inputDate = null;
		try {
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    java.util.Date utilDate = format.parse(inputDateStr);
		    inputDate = new java.sql.Date(utilDate.getTime()); // java.sql.Date に変換

		} catch (ParseException e) {
		    req.setAttribute("message", "無効な日付が入力されました。");
		    req.getRequestDispatcher("error.jsp").forward(req, res);
		    return; // 処理終了
		}

		// Instructionオブジェクトにデータをセット
		Instruction instruction = new Instruction();
		instruction.setInputDate(inputDate);
		instruction.setUsersId(users.getUsersId());
		instruction.setInstructions(instructionContent);
		instruction.setStudentId(studentId);

		// 指導表テーブルに更新
		boolean isUpdated = instructionDao.saveInstruction(instruction);

		if (!isUpdated) {
		    // エラー発生時の処理
		    req.setAttribute("message", "指導表の更新に失敗しました。");
		    req.getRequestDispatcher("error.jsp").forward(req, res);
		    return;
		}

		// 正常終了後、指導表データを取得
		List<Instruction> instructionList = instructionDao.getInstruction(studentId);

		// 指導表データが取得できた場合
		if (!instructionList.isEmpty()) {
		    req.setAttribute("instruction_list", instructionList);

		    // 学生氏名をリクエストにセット
		    String studentName = instructionList.get(0).getStudentName();
		    req.setAttribute("studentName", studentName);
		}

		// 学生番号をリクエストにセット
		req.setAttribute("student_id", studentIdStr);

		// フォワード
		req.getRequestDispatcher("instruction.jsp").forward(req, res);




	}
}
