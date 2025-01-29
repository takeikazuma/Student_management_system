package action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Score;
import bean.Subject;
import dao.ScoreDao;
import tool.Action;


public class StudentRegisterAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		//ローカル変数の宣言 1
		String url = "";
		String student_id_str = "";
		ScoreDao scoreDao = new ScoreDao();
		List<Score> score_list = new ArrayList<>();
		Subject subject = new Subject();
		String[] scoreSubjectId = null;
		String scoreidStr = null;// 成績ID文字列
		String monthStr = null;// 入力月文字列
		String valueStr = null;	// 得点文字列
		int studentid = 0;	// 学生番号
		int subjectid = 0; // 科目ID
		int scoreid = 0;// 成績ID
		int month = 0;// 得点
		int value = 0;// 入力月
		boolean register;
//		String subjectcode = "";// 科目コード

// リクエストパラメータ―の取得 2

		student_id_str = req.getParameter("student_id");//学生番号
		// 学生番号を整数に変換
		studentid = Integer.parseInt(student_id_str);

//シーケンスNo.24：メソッドNo.12(1件の成績を更新)
		// 入力された科目IDの一覧を取得
		scoreSubjectId = req.getParameterValues("score_subjectId_set[]");

		// 科目IDを全件走査
		for (String subjectId : scoreSubjectId) {

			// 入力された「month_科目ID」の入力月文字列を取得
			monthStr = req.getParameter("month_" + subjectId);
			if(monthStr.equals("0")){
				continue;
			}
			// 成績インスタンスを初期化
			Score score = new Score();
			// 入力された「scoreid_科目ID」の成績ID文字列を取得
			scoreidStr = req.getParameter("scoreid_" + subjectId);
			System.out.println(subjectId);
			// 入力された「value_科目ID」の得点文字列を取得
			valueStr = req.getParameter("value_" + subjectId);

			// 成績ID文字列を整数に変換
			scoreid = Integer.parseInt(scoreidStr);
			// 入力月文字列を整数に変換
			month = Integer.parseInt(monthStr);
			// 得点文字列を整数に変換
			value = Integer.parseInt(valueStr);
			// 科目ID文字列を整数に変換
			subjectid = Integer.parseInt(subjectId);


			// 成績インスタンスに値をセット
			score.setScoreId(scoreid);
			score.setStudentId(studentid);
			score.setScoreMonth(month);
			score.setScoreValue(value);
			score.setSubjectId(subjectid);

			score_list.add(score);
		}
		register = scoreDao.saveScore(score_list);

		if(register){
			req.setAttribute("registercheck_flag", true);
		}
		else{
			req.setAttribute("registercheck_flag", false);
		}
//レスポンス値をセット6
		req.setAttribute("studentid", studentid);
//フォワード
		url = "StudentScore.action";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
