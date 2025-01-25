package action;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.GradeClass;
import bean.Score;
import bean.Subject;
import dao.ScoreDao;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;

public class SubjectOutAction extends Action {

    private static final String COMMA = ",";
    private static final String NEWLINE = "\r\n";
    private static final String DQ = "\"";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
      //ローカル変数の宣言 1
      		String url = "subjectScoreOut.jsp";
      		String class_id_str = "";
      		String subject_id_str = "";
      		String courseYear_str = "";
      		String subject_name ="";
      		String subject_code ="";
      		int class_id = 0;
      		int subject_id =0;
      		int courseYear =0;
      		StudentDao studentDao = new StudentDao();
      		List<GradeClass> class_list = null;
      		SubjectDao subjectDao = new SubjectDao();
      		List<Subject> subject_list = null;
      		ScoreDao scoreDao = new ScoreDao();
      		List<Score> scoreList = null;
      		Subject subject = new Subject();

      	//リクエストパラメータ―の取得 2
      		class_id_str = req.getParameter("class_id");// クラスID
      		subject_id_str = req.getParameter("subject_id");//科目ID
      		courseYear_str = req.getParameter("courseYear");//入学年度

      		//クラスIDが送信されていた場合
      		if (class_id_str != null ) {
      			// 数値に変換
      			class_id = Integer.parseInt(class_id_str);
      		}
      		//科目IDが送信されていた場合
      		if (subject_id_str != null) {
      			// 数値に変換
      			subject_id = Integer.parseInt(subject_id_str);
      		}
      		//入学年度が送信されていた場合
      		if (courseYear_str != null) {
      			// 数値に変換
      			courseYear = Integer.parseInt(courseYear_str);
      		}
      //シーケンスNo.21：メソッドNo.3(プルダウンに表示するクラスの一覧を取得)


      		//DBからデータ取得 3
      		class_list = studentDao.getClassList();//ユーザデータ取得

      //シーケンスNo.22：メソッドNo.7(プルダウンに表示する科目の一覧を取得)

      		//DBからデータ取得 3
      		subject_list = subjectDao.getSubject();//ユーザデータ取得


      //シーケンスNo.23：メソッドNo.5(クラス、科目に一致する成績情報の取得)

      		//DBからデータ取得 3
      		if (class_id  != 0 && subject_id != 0 && courseYear != 0) {
      			scoreList = scoreDao.getScore(class_id,subject_id,courseYear,"out");//成績データ取得
      			subject = subjectDao.getSubjectNameCode(subject_id);//科目名、科目コード取得
      		    subject_name = subject.getSubjectName();
      		    subject_code = subject.getSubjectCode();
      		}
      //シーケンスNo.23：メソッドNo.5(クラス、科目に一致する成績情報の取得)
      		if (scoreList != null && !scoreList.isEmpty()) {
                generateCsv(res, scoreList);
            } else {
                req.setAttribute("error_message", "成績データが見つかりませんでした。");
            }


      //レスポンス値をセット6
      		// リクエストにクラス一覧をセット
      		req.setAttribute("class_list", class_list);
      		// リクエストに科目一覧をセット
      		req.setAttribute("subject_list", subject_list);
      		// リクエストに成績一覧をセット
      		req.setAttribute("score_list", scoreList);

      		req.setAttribute("class_id", class_id);
      		req.setAttribute("subject_id",subject_id);
      		req.setAttribute("courseYear", courseYear);
      		req.setAttribute("subject_name", subject_name);
      		req.setAttribute("subject_code", subject_code);

      	//フォワード
      		req.getRequestDispatcher(url).forward(req, res);
    }

    private void generateCsv(HttpServletResponse res, List<Score> scoreList) throws Exception {
        String filename = "成績評価チェックリスト("+scoreList.get(0).getSubject().getSubjectName()+").csv";
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.JAPAN);
        String formattedDate = currentDate.format(formatter);

        res.setHeader("Content-Type", "text/csv; charset=Shift_JIS");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(formattedDate + "_" + filename, "UTF-8") + "\"");

        //抽出日
        formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.JAPAN);
        formattedDate = currentDate.format(formatter);

        try (PrintWriter out = res.getWriter()) {
            // ヘッダー
            out.append("入学年度").append(":"+scoreList.get(0).getStudent().getAdmissionYear()).append(COMMA)
            	.append("学年").append(":"+scoreList.get(0).getStudent().getSchoolYear()).append(COMMA)
            	.append("クラス").append(":"+scoreList.get(0).getStudent().getGradeClassName()).append(COMMA).append(NEWLINE);
            out.append("抽出日").append(":"+formattedDate).append(COMMA).append(NEWLINE);
            out.append("学生番号").append(COMMA).append("月").append(COMMA).append("氏名").append(COMMA).append("科目コード").append(COMMA)
            	.append("科目名").append(COMMA).append("得点").append(COMMA).append("認定評価").append(NEWLINE);

            // 行データ
           String certification="";
            for (Score s : scoreList) {
            	if(s.getScoreValue()>=90 ){certification="秀";}
            	else if(s.getScoreValue()>=80){certification="優";}
            	else if(s.getScoreValue()>=70){certification="良";}
            	else if(s.getScoreValue()>=60){certification="可";}
            	else{certification="不可";}

                out.append(addDoubleQuotation(s.getStudentId()))
                   .append(COMMA)
                   .append(addDoubleQuotation(s.getScoreMonth()))
                   .append(COMMA)
                   .append(addDoubleQuotation(s.getStudent().getStudentName()))
                   .append(COMMA)
                   .append(addDoubleQuotation(s.getSubject().getSubjectCode()))
                   .append(COMMA)
                   .append(addDoubleQuotation(s.getSubject().getSubjectName()))
                   .append(COMMA)
                   .append(addDoubleQuotation(Integer.toString(s.getScoreValue())))
                   .append(COMMA)
                   .append(addDoubleQuotation(certification))
                   .append(NEWLINE);
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV生成中にエラーが発生しました。", e);
        }
    }

    private String addDoubleQuotation(String content) {
        return DQ + content + DQ;
    }
    private String addDoubleQuotation(int content) {
        return DQ + content + DQ;
    }
}
