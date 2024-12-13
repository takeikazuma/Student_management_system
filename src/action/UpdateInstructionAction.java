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

//		//ローカル変数の宣言 1
//		InstructionDao instructionDao = new InstructionDao();
//
//		Users users = null;
//
//		//リクエストパラメタ用
//		int student_id;
//		String student_id_str = "";
//		String input_date_str = "";
//		String instruction_content = "";
//		String instruction_id_str = "";
//		int instruction_id =0;
//
//		// セッション情報を取得
//		HttpSession session = req.getSession(true);
//		// セッションからログイン情報を取得
//		Object sessionUser = session.getAttribute("users");
//		// セッション情報をUsers型にキャスト
//		users = (Users)sessionUser;
//
//		//リクエストパラメータ―の取得 2
//		student_id_str = req.getParameter("studentIdHidden");
//		student_id = Integer.parseInt(student_id_str);	//String→int
//		input_date_str = req.getParameter("inputDate");
//		instruction_content = req.getParameter("inputInstructions");
//
//		instruction_id_str = req.getParameter("instructionId");
//		instruction_id =  Integer.parseInt(instruction_id_str);		//String→int
//
//		// 日付の文字列を java.sql.Date に変換
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 日付のフォーマット
//		java.util.Date utilDate = format.parse(input_date_str); // java.util.Date に変換
//		Date input_date = new Date(utilDate.getTime()); // java.sql.Date に変換
//
//		Instruction instruction = new Instruction();
//		instruction.setInputDate(input_date);
//		instruction.setUsersId(users.getUsersId());
//		instruction.setInstructions(instruction_content);
//		instruction.setStudentId(student_id);
//		instruction.setInstructionId(instruction_id);
//
//		//指導表テーブルに更新
//		if (!(instructionDao.updateInstruction(instruction))){
//			//エラーが発生した場合
//
//		}else{
//			//正常終了した場合
//
//			List<Instruction> instruction_list = null;
//
//			//指導表テーブルからデータ取得
//			instruction_list = instructionDao.getInstruction(student_id);
//
//			if (instruction_list.size() != 0) { // データが取得された場合
//				// リクエストに指導表データをセット
//				req.setAttribute("instruction_list", instruction_list);
//
//				//学生氏名をリクエストにセット
//				String studentName = instruction_list.get(0).getStudentName();
//			    req.setAttribute("studentName", studentName);
//			}
//
//		}
//
//		//リクエストに学生番号、学生氏名をセット
//		req.setAttribute("studentId", student_id_str);


	    //リクエストパラメタ用
	    int studentId = Integer.parseInt(req.getParameter("studentIdHidden"));
	    String inputDateStr = req.getParameter("inputDate");
	    String instructionContent = req.getParameter("inputInstructions");
	    int instructionId = Integer.parseInt(req.getParameter("instructionId"));

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

	}

}
