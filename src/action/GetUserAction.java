package action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Attend;
import bean.Student;
import bean.Users;
import dao.AttendDao;
import dao.StudentDao;
import dao.UsersDao;
import tool.Action;


public class GetUserAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		String url = "";
		String id = "";
		String password = "";
		UsersDao usersDao = new UsersDao();
		Users users = null;
		StudentDao studentDao = new StudentDao();
		List<Student> students = null;
		AttendDao attendDao = new AttendDao();
		Attend pastAttend = new Attend();
		List<Attend> attendList = null;
		List<Attend> alertAttendList = new ArrayList<>();
		double totalAttendNum[] = new double[50];
		String alertStudentName[] = new String[50];
		int counter = -1;
		double attendNum = 0;

		//シーケンスNo.1：メソッドNo.8(ユーザ情報を取得)
		//リクエストパラメータ―の取得 2
		id = req.getParameter("id");// 教員ID
		password = req.getParameter("password");//パスワード

		//DBからデータ取得 3
		users = usersDao.getUser(id, password);//ユーザデータ取得

		if (users != null) {// 認証成功の場合
			// セッション情報を取得
			HttpSession session = req.getSession(true);
			// 認証済みフラグを立てる(必要か検討：落合)
			users.setAuthenticated(true);
			// セッションにログイン情報を保存
			session.setAttribute("users", users);

			//シーケンスNo.2：メソッドNo.6(学生情報を取得)
			students = studentDao.getStudentList("", "", users.getGradeClassName());

			//シーケンスNo.3：メソッドNo.2(出席情報を取得)
			attendList = attendDao.getAttend(students);
			attendList.sort(Comparator.comparing(Attend::getStudentId));

			// 欠席日数の合算処理
			for(Attend attend: attendList){
				// 対象者が切り替わった場合
				if(!attend.getStudentId().equals(pastAttend.getStudentId())){
					if(attendNum >= 10){
						// 欠席情報を格納
						alertAttendList.add(pastAttend);
						totalAttendNum[counter] = attendNum;
						for(Student student: students){
							if(student.getStudentId() == Integer.parseInt(pastAttend.getStudentId())){
								alertStudentName[counter] = student.getStudentName();
							}
						}
					}
					// 欠席日数のクリア
					attendNum = 0;
					// カウントアップ
					counter += 1;
				}

				if(attend.getAttendStatus() == 1){
					// 欠席の場合 +1
					attendNum += 1;
				} else{
					// 遅刻・早退の場合 +0.3
					String strNum = String.valueOf(attendNum);
					int periodNum = strNum.indexOf(".");
					// x.6 の場合、次の値は x.9 ではなく切り上げた値とする
					if(strNum.substring(periodNum + 1, periodNum + 2).equals("6")){
						attendNum = Math.ceil(attendNum);
					} else {
						attendNum += 0.3;
						// Double型の誤差補正
						attendNum = Math.floor(attendNum * 10) / 10;
					}
				}
				// データ格納用にひとつ前のデータを退避
				pastAttend = attend;
			}
			if(attendNum >= 10){
				// 欠席情報を格納
				alertAttendList.add(pastAttend);
				totalAttendNum[counter] = attendNum;
				for(Student student: students){
					if(student.getStudentId() == Integer.parseInt(pastAttend.getStudentId())){
						alertStudentName[counter] = student.getStudentName();
					}
				}
			}

			// メニューに表示する情報の格納
			req.setAttribute("alertAttendList", alertAttendList);
			req.setAttribute("totalAttendNum", totalAttendNum);
			req.setAttribute("alertStudentName", alertStudentName);
			req.setAttribute("gradeClass", users.getGradeClassName());
			// リダイレクト
			req.getRequestDispatcher("menu.jsp").forward(req, res);
		} else {
			// 認証失敗の場合
			// エラーメッセージをセット
			List<String> errors = new ArrayList<>();
			errors.add("IDまたはパスワードが確認できませんでした");
			req.setAttribute("errors", errors);
			// 入力されたユーザIDをセット
			req.setAttribute("id", id);

			//フォワード
			url = "login.jsp";
			req.getRequestDispatcher(url).forward(req, res);
		}
	}
}
