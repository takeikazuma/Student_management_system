package action;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class AllAttendRegistAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		List<String> results = new ArrayList<>();
		List<String> errors = new ArrayList<>();

		String url = "";
		int admissionYear = 0;
		int year = 0;
		int month = 0;
		String className = "";

		StudentDao studentDao = new StudentDao();
		List<Student> studentList = null;
		LinkedHashMap<Integer, HashMap<String, String>> studentFieldsMap = new LinkedHashMap<>();		// 学生の順番が重要なため、LinkedHashMapを使用(HashMapだと順番が入れ替わる場合がある
		HashMap<Integer, HashMap<Integer, Integer>> attendMap = new HashMap<>();						// HashMap:出席状況<Integer:学生番号, HashMap<Integer:出欠日, Integer:出欠状況>>
		HashMap<Integer, Double> studentAttendSumMap = new HashMap<>();	// 欠席件数合計

		//リクエストパラメータ―の取得 2
		try {	// int化できなければ0
			admissionYear = Integer.parseInt(req.getParameter("admission_year"));	// 入学年度
		} catch (Exception e) {
			admissionYear = 0;
		}
		try {	// int化できなければ0
			year = Integer.parseInt(req.getParameter("year"));	// 検索年
		} catch (Exception e) {
			year = 0;
		}
		try {	// int化できなければ0
			month = Integer.parseInt(req.getParameter("month"));	// 検索月
		} catch (Exception e) {
			month = 0;
		}

		className = req.getParameter("class_name");	// クラス名

		if(admissionYear > 0 && year > 0 && month > 0 && className.length() > 0) {	// パラメーターチェック
			// 有効な年月日か検証
			boolean isValidDate = false;
			try {
				// LocalDateを生成して検証
				LocalDate.of(year, month, 1);
				isValidDate = true;
			} catch (Exception e) {
				isValidDate = false;
			}
			if(isValidDate) {
				//DBからデータ取得 3
				studentList = studentDao.getStudent(admissionYear, className);	// 学生データ取得

				for(Student student: studentList) {
					HashMap<String, String> studentFields = new HashMap<String, String>();

					studentFields.put("student_id", String.valueOf(student.getStudentId()));	// 学生番号
					studentFields.put("student_name", String.valueOf(student.getStudentName()));	// 学生氏名

					studentFieldsMap.put(student.getStudentId(), studentFields);

					// 学生別欠席数を初期化
					studentAttendSumMap.put(student.getStudentId(), 0.0);
				}
				// 検索結果をセット
				req.setAttribute("studentFieldsMap", studentFieldsMap);

				// 出席状況マップを作成
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
					if(withdrawalDate != null) {
						int startDay = YearMonth.of(year, month).lengthOfMonth() + 1;
						// withdrawalDateが指定年月と同じ場合、退学日の次の日から -1
						if (YearMonth.from(withdrawalDate.toLocalDate()).equals(YearMonth.of(year, month))) {
							startDay = withdrawalDate.getDayOfMonth() + 1;
						}
						// 指定年月がwithdrawalDateを経過している場合、全日 -1
						else if(withdrawalDate.toLocalDate().isBefore(YearMonth.of(year, month).atEndOfMonth())) {
							startDay = 1;
						}
						// 指定日から月末まで -1
						for(int i = startDay; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
							attendMap.get(student.getStudentId()).put(i, -1);
						}
					}
				}

				// 長期休暇は全学生 -2 に設定
				GradeClassDao gradeClassDao = new GradeClassDao();
				int gradeClassId = gradeClassDao.getClassId(className);	// クラス名からクラスIDを取得
				AbsenceDao absenceDao = new AbsenceDao();
				List<Absence> absenceList = absenceDao.getAbsence(			// 指定月の長期休暇を取得
						gradeClassId,
						Date.valueOf(LocalDate.of(year, month, 1)),
						Date.valueOf(YearMonth.of(year, month).atEndOfMonth()) );
				// 長期休暇があったら、全学生を-2に設定
				for(Absence absence: absenceList) {		// 全件の長期休暇
					for(int i = absence.getStartDate().toLocalDate().getDayOfMonth(); i <= absence.getEndDate().toLocalDate().getDayOfMonth(); i++) {	// 開始日～終了日まで繰り返す
						for(Student student: studentList) {	// (指定クラスの)全学生
							attendMap.get(student.getStudentId()).put(i, -2);	// 指定学生の出欠の値を -2
						}
					}
				}

				req.setAttribute("attendMap", attendMap);

				// 合計欠席数を計算
				for(Map.Entry<Integer, HashMap<Integer, Integer>> attends: attendMap.entrySet()) {
					for(Map.Entry<Integer, Integer> attend: attends.getValue().entrySet()) {
						Double attendSum = studentAttendSumMap.get(attends.getKey());
						if(attend.getValue() == 1) {	// 欠席
							attendSum += 1.0;
						} else if(attend.getValue() == 2 || attend.getValue() == 3) {	// 遅刻・早退
							attendSum += 0.3;
						}
						// x.9以上の場合は切り上げ
						if((attendSum - Math.floor(attendSum)) > 0.899) {	// >= 0.9 は、Double型の誤差が生じるので近似値で比較
							attendSum = Math.ceil(attendSum);
						}
						studentAttendSumMap.put(attends.getKey(), attendSum);
					}
				}

				// 学生別欠席数をセット
				req.setAttribute("studentAttendSumMap", studentAttendSumMap);

				req.setAttribute("length_of_month", YearMonth.of(year, month).lengthOfMonth());
			}
			else {
				errors.add("年月が正しくありません");
			}
		}

		// 入力された項目をセット
		req.setAttribute("admission_year", req.getParameter("admission_year"));
		req.setAttribute("class_name", req.getParameter("class_name"));
		req.setAttribute("year", req.getParameter("year"));
		req.setAttribute("month", req.getParameter("month"));

		req.setAttribute("errors", errors);
		req.setAttribute("results", results);

		//フォワード
		url = "all_attend.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
