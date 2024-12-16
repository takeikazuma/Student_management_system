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

public class UpdateInstructionAction  extends Action  {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

	    //リクエストパラメタ用
	    int studentId = Integer.parseInt(req.getParameter("studentIdHidden"));
	    String inputDateStr = req.getParameter("inputDate");
	    String instructionContent = req.getParameter("inputInstructions");
	    String instructionIdStr = req.getParameter("instructionId");
	    int instructionId = Integer.parseInt(instructionIdStr);

	    //セッションからログイン情報を取得
	    HttpSession session = req.getSession(true);
	    Users users = (Users) session.getAttribute("users");

	    //セッション情報がない場合の処理
	    if (users == null) {
	        //ログインしていない場合の処理
	    	req.setAttribute("message", "ログインしてください。");
		    req.getRequestDispatcher("login.jsp").forward(req, res);
		    return;
	    }

	    //日付の文字列を java.sql.Date に変換
	    java.sql.Date inputDate = null;
	    try {

	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date utilDate = format.parse(inputDateStr);
	        inputDate = new java.sql.Date(utilDate.getTime());

	    } catch (ParseException e) {
	        //日付が正しくなかった場合（空欄で来てしまった場合もこちら）
	        req.setAttribute("message", "日付の形式が正しくありません。");
	        return;
	    }

	    //更新用データの準備
	    Instruction instruction = new Instruction();
	    instruction.setInputDate(inputDate);
	    instruction.setUsersId(users.getUsersId());
	    instruction.setInstructions(instructionContent);
	    instruction.setStudentId(studentId);
	    instruction.setInstructionId(instructionId);

	    //データを更新
	    InstructionDao instructionDao = new InstructionDao();
	    boolean updateResult = instructionDao.updateInstruction(instruction);

	    if (!updateResult) {
	        //更新失敗時
	    	req.setAttribute("error", "指導表の更新に失敗しました。");
		    req.getRequestDispatcher("error.jsp").forward(req, res);
	        return;

	    } else {
	        //更新成功時
	    	//リストのデータを再取得
	        List<Instruction> instructionList = instructionDao.getInstruction(studentId);
	        //データが取得できた場合のみ画面にセット
	        if (instructionList != null && !instructionList.isEmpty()) {
	            req.setAttribute("instruction_list", instructionList);
	            req.setAttribute("studentName", instructionList.get(0).getStudentName());
	        }
	    }

	    // リクエストに学生番号をセット
	    req.setAttribute("student_id", studentId);

	    req.getRequestDispatcher("instruction.jsp").forward(req, res);

	}

}
