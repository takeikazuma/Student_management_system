package action;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Absence;
import dao.AbsenceDao;
import dao.GradeClassDao;

@WebServlet("/action/registerAbsence")
public class AbsenceAction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // リクエストからパラメータを取得
            String className = request.getParameter("class_name");
            int admissionYear = Integer.parseInt(request.getParameter("admission_year"));
            Date startDate = Date.valueOf(request.getParameter("start_date"));
            Date endDate = Date.valueOf(request.getParameter("end_date"));

            // Absenceインスタンスを作成しデータをセット


            Absence absence = new Absence();

            GradeClassDao setGradeClassDao = new GradeClassDao();
            int classId = setGradeClassDao.getClassId(className);
            absence.setGradeClassId(classId);

            absence.setAdmissionYear(admissionYear);
            absence.setStartDate(startDate);
            absence.setEndDate(endDate);

            // AbsenceDaoで登録処理を実行
            AbsenceDao dao = new AbsenceDao();
            dao.insertAbsence(absence);

            // 成功メッセージをリクエストに追加
            request.setAttribute("message", "登録が完了しました。");

            // クラス情報を取得
            GradeClassDao gradeClassDao = new GradeClassDao();
            HashMap<Integer, String> classMap = gradeClassDao.getClassMap();

            // クラス情報をリクエストにセット
            request.setAttribute("classMap", classMap);

        } catch (Exception e) {
            // エラーメッセージをリクエストに追加
            request.setAttribute("message", "登録中にエラーが発生しました: " + e.getMessage());
        }

        // 結果を absence.jsp にフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("absence.jsp");
        dispatcher.forward(request, response);
    }
}

