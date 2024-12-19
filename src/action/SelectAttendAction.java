package action;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Absence;
import bean.Attend;
import bean.Student;
import dao.AbsenceDao;
import dao.AttendDao;
import dao.GradeClassDao;
import dao.StudentDao;
import tool.Action;

public class SelectAttendAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		List<String> results = new ArrayList<>();
		List<String> errors = new ArrayList<>();

		String url = "";
		int admissionYear = 0;
		int year = 0;
		Integer month = 0;
		Integer day = 0;
		String className = "";
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = null;
		LinkedHashMap<Integer, HashMap<String, String>> studentFieldsMap = new LinkedHashMap<>();	// 学生の順番が重要なため、LinkedHashMapを使用(HashMapだと順番が入れ替わる場合がある

		//リクエストパラメータ―の取得 2
		try {	// int化できなければ0
			admissionYear = Integer.parseInt(req.getParameter("admission_year"));	// 入学年度
		} catch (Exception e) {
			admissionYear = 0;
		}
		try {	// int化できなければ0
			year = Integer.parseInt(req.getParameter("year"));	// 編集年
		} catch (Exception e) {
			year = 0;
		}
		try {	// int化できなければ0
			month = Integer.parseInt(req.getParameter("month"));	// 編集月
		} catch (Exception e) {
			month = null;
		}
		try {	// int化できなければ0
			day = Integer.parseInt(req.getParameter("day"));	// 編集日
		} catch (Exception e) {
			day = null;
		}
		className = req.getParameter("class_name");	// クラス名


		// クラス マップを取得
		GradeClassDao gradeClassDao = new GradeClassDao();
		HashMap<Integer, String> classMap = gradeClassDao.getClassMap();

		// 出席状況マップを作成
		HashMap<Integer, HashMap<Integer, Integer>> attendMap = new HashMap<>();	// HashMap:出席状況<Integer:学生番号, HashMap<Integer:出欠日, Integer:出欠状況>>


		// パラメーターが入力されている場合、一覧を取得
		if(admissionYear > 0 && className != null && year > 0 && month != null && day != null) {
			// 有効な年月日か検証
			boolean isValidDate = false;
			try {
				// LocalDateを生成して検証
				LocalDate.of(year, month, day);
				isValidDate = true;
			} catch (Exception e) {
				isValidDate = false;
			}
			if(isValidDate) {
				//DBからデータ取得 3
				studentList = studentDao.getStudent(admissionYear, className);	// 学生データ取得

				for(Student student: studentList) {
					HashMap<String, String> studentFields = new HashMap<String, String>();

					studentFields.put("student_id",		String.valueOf(student.getStudentId()));	// 学生番号
					studentFields.put("grade_class_name",	classMap.get(student.getGradeClassId()));	// クラス名
					studentFields.put("admission_year",	String.valueOf(student.getAdmissionYear()));	// 入学年度
					studentFields.put("student_name",		String.valueOf(student.getStudentName()));	// 学生氏名
					studentFields.put("school_year",		String.valueOf(student.getSchoolYear()));	// 学年
					studentFieldsMap.put(student.getStudentId(), studentFields);
				}


				AttendDao attendDao = new AttendDao();
				List<Attend> attendList = attendDao.getAttend(year, month, null, studentList);

				// 指定学生分の出席状況一覧(出席状況は空)を作成しておく
				for(Student student: studentList) {
					attendMap.put(student.getStudentId(), new HashMap<>());
				}
				for(Attend attend: attendList) {
					attendMap.get(Integer.valueOf(attend.getStudentId())).put(
							attend.getAttendDate().toLocalDate().getDayOfMonth(),	// 出欠日
						    attend.getAttendStatus()	// 出欠状況
					);
				}

				// 退学者の出席状況を -1 に設定
				for(Student student: studentList) {
					ZonedDateTime withdrawalDate = student.getWithdrawalDate();	// 退学年月日
					// 指定年月がwithdrawalDateを経過している場合 -1
					if(withdrawalDate != null && withdrawalDate.toLocalDate().isBefore(LocalDate.of(year, month, day))) {
						attendMap.get(student.getStudentId()).put(day, -1);
					}
				}
				// 長期休暇は全学生 -2 に設定
				int gradeClassId = gradeClassDao.getClassId(className);	// クラス名からクラスIDを取得
				AbsenceDao absenceDao = new AbsenceDao();
				List<Absence> absenceList = absenceDao.getAbsence(			// 指定月の長期休暇を取得
						gradeClassId,
						Date.valueOf(LocalDate.of(year, month, day)),
						Date.valueOf(LocalDate.of(year, month, day)));
				// 長期休暇があったら、全学生を-2に設定
				if(absenceList.size() > 0) {		// 指定月の長期休暇が存在する場合
					for(Student student: studentList) {		// (指定クラスの)全学生
						attendMap.get(student.getStudentId()).put(day, -2);	// 指定学生の出欠の値を -2
					}
				}
			}
			else {
				errors.add("年月日が正しくありません");
			}
		}

		// 出席状況を更新後ページ遷移してきた場合、メッセージ表示
		if(req.getRequestURL().toString().endsWith("RegistrationAttend.action")) {
			results.add("出席状況を更新しました");
		}

		// 検索結果をセット
		req.setAttribute("studentFieldsMap", studentFieldsMap);
		req.setAttribute("attendMap", attendMap);

		// 入力された検索項目をセット
		req.setAttribute("admission_year", req.getParameter("admission_year"));
		req.setAttribute("class_name", req.getParameter("class_name"));
		req.setAttribute("year", req.getParameter("year"));
		req.setAttribute("month", req.getParameter("month"));
		req.setAttribute("day", req.getParameter("day"));

		req.setAttribute("errors", errors);
		req.setAttribute("results", results);

		//フォワード
		url = "input_attend.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
