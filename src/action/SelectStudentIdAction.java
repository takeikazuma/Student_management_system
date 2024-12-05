package action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import dao.StudentDao;
import tool.Action;


public class SelectStudentIdAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の宣言
		String name = "";
		String kana = "";
		String gradeClass = "";
		StudentDao studentDao = new StudentDao();
		List<Student> student = null;

		// リクエストパラメータの取得
		name = req.getParameter("name");// 氏名
		kana = req.getParameter("kana");// カナ
		gradeClass = req.getParameter("gradeClass");// クラス

		// DBからデータ取得
		student = studentDao.getStudentList(name, kana, gradeClass);

		if (student.size() != 0) { // データが取得された場合

			// 取得データをセット
			req.setAttribute("student", student);
			req.getRequestDispatcher("searchStudentId.jsp").forward(req, res);

		} else { // データが取得されなかった場合

			// エラーメッセージをセット
			List<String> errors = new ArrayList<>();
			errors.add("学生情報を取得できませんでした");
			req.setAttribute("errors", errors);
		}

		//フォワード
		req.getRequestDispatcher("searchStudentId.jsp").forward(req, res);
	}
}
