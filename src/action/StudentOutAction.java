package action;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Score;
import bean.Student;
import dao.ScoreDao;
import dao.StudentDao;
import tool.Action;

public class StudentOutAction extends Action {

    private static final String COMMA = ",";
    private static final String NEWLINE = "\r\n";
    private static final String DQ = "\"";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String url = "studentScoreOut.jsp";
        String studentIdStr = req.getParameter("csv_student_id");

        int studentId = 0;
        if (studentIdStr != null) {
            studentId = Integer.parseInt(studentIdStr);
        }

        StudentDao studentDao = new StudentDao();
        ScoreDao scoreDao = new ScoreDao();
        List<Score> scoreList = null;
        List<Student> studentInfo = null;

        if (studentId >= 1_000_000 && studentId <= 9_999_999) {
            studentInfo = studentDao.getStudent(studentId);
            if (studentInfo != null) {
                scoreList = scoreDao.getScore(studentId,"out");
            }
        }

        if (scoreList != null && !scoreList.isEmpty()) {
            generateCsv(res, scoreList);
        } else {
            req.setAttribute("error_message", "成績データが見つかりませんでした。");
        }

        req.setAttribute("score_list", scoreList);
        req.setAttribute("student_info", studentInfo);
        req.setAttribute("student_id", studentId == 0 ? "" : studentId);

        req.getRequestDispatcher(url).forward(req, res);
    }

    private void generateCsv(HttpServletResponse res, List<Score> scoreList) throws Exception {
        String filename = "成績評価チェックリスト("+scoreList.get(0).getStudent().getStudentName()+").csv";
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.JAPAN);
        String formattedDate = currentDate.format(formatter);

        res.setHeader("Content-Type", "text/csv; charset=Shift_JIS");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(formattedDate + "_" + filename, "UTF-8") + "\"");

        //抽出日
        formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.JAPAN);
        formattedDate = currentDate.format(formatter);
        //在籍状況
        String enrollment = "";
        if(scoreList.get(0).getStudent().getIsEnrollment()){enrollment="在学";}
        else{enrollment="退学";}

        try (PrintWriter out = res.getWriter()) {
            // ヘッダー
            out.append("学生番号").append(":"+scoreList.get(0).getStudentId()).append(COMMA)
            	.append("氏名").append(":"+scoreList.get(0).getStudent().getStudentName()).append(COMMA)
            	.append("クラス").append(":"+scoreList.get(0).getStudent().getGradeClassName()).append(COMMA).append(NEWLINE);
            out.append("在籍状況").append(":"+enrollment).append(COMMA)
            .append("抽出日").append(":"+formattedDate).append(COMMA).append(NEWLINE);
            out.append("入力月").append(COMMA).append("年次").append(COMMA).append("科目コード").append(COMMA)
            	.append("科目名").append(COMMA).append("得点").append(COMMA).append("認定評価").append(NEWLINE);

            // 行データ
           String certification="";
            for (Score s : scoreList) {
            	if(s.getScoreValue()>=90 ){certification="秀";}
            	else if(s.getScoreValue()>=80){certification="優";}
            	else if(s.getScoreValue()>=70){certification="良";}
            	else if(s.getScoreValue()>=60){certification="可";}
            	else{certification="不可";}

                out.append(addDoubleQuotation(s.getScoreMonth()))
                   .append(COMMA)
                   .append(addDoubleQuotation(s.getSubject().getCourseYear()))
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
