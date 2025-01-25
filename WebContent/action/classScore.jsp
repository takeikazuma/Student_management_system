<%-- 成績入力(クラス別) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
<c:param name="head">
	<%-- 独自CSSの追加 --%>
	<link href="../common/assets/css/classScore_css.css" rel="stylesheet">
</c:param>
	<c:param name="title">
		成績入力(クラス別)
	</c:param>

	<c:param name="scripts">
	<section class="me-4">
        <!-- ここにフォームや内容を記載 -->

        <!-- ダイアログ表示用スクリプト -->
         <c:choose>
         	<c:when test="${registercheck_flag == true}">
         		 <script>
                alert("登録が完了しました！");
            	</script>
         	</c:when>
         	<c:when test="${registercheck_flag == false}">
         		 <script>
                alert("登録に失敗しました！");
            	</script>
         	</c:when>
         </c:choose>
    </section>
	</c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績入力</h2>
			<form method="post" action="ClassScore.action">
				<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
					<div class="col-2">
						<label class="form-label" for="student-f1-select">入学年度 </label>
						<select class="form-select " id="student-f1-select" name="courseYear">
							<option value="0">--------</option>
							<option value="2021" <c:if test="${courseYear==2021}">selected</c:if>>2021</option>
							<option value="2022" <c:if test="${courseYear==2022}">selected</c:if>>2022</option>
							<option value="2023" <c:if test="${courseYear==2023}">selected</c:if>>2023</option>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">クラス</label>
						<select class="form-select " id="student-f2-select" name="class_id">
							<option value="0">--------</option>
							<c:forEach var="list" items="${class_list}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${list.gradeClassId}" <c:if test="${list.gradeClassId==class_id}">selected</c:if>>${list.gradeClassName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">科目名</label>
						<select class="form-select " id="student-f2-select" name="subject_id">
							<option value="0">--------</option>
							<c:forEach var="subject" items="${subject_list}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${subject.subjectId}" <c:if test="${subject.subjectId==subject_id}">selected</c:if>>${subject.subjectName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">絞込み</button>
					</div>
				</div>
			</form>
			<c:choose>
		    <c:when test="${score_list.size()>0}">
		        <form method="post" action="ClassScore.action">
		            <!-- 一括月入力 -->
		            <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="bulk-month-input" style="width: 302.66666px;">
		                <div class="col-8" style="width: 124px;">
		                    <label class="form-label" for="bulk-month-input-field">一括月入力</label>
		                    <input type="number" name="bulk_month" min="0" max="12" class="form-control" />
		                </div>
		                <div class="col-4 text-center" style="width: 124px;">
		                    <button class="btn btn-primary mt-4" id="apply-bulk-month">適用</button>
		                </div>
		            </div>
		            <input type="hidden" name="courseYear" value="${courseYear}" />
		            <input type="hidden" name="class_id" value="${class_id}" />
		            <input type="hidden" name="subject_id" value="${subject_id}" />
		       </form>
		       <form method="post" id="test-form" action="ClassScoreRegister.action">
		            <table class="table table-hover">
		                <tr>
		                    <th>学生番号</th>
		                    <th>月</th>
		                    <th>氏名</th>
		                    <th>科目名</th>
		                    <th>科目コード</th>
		                    <th>点数</th>
		                </tr>
		                <c:forEach var="score" items="${score_list}">
		                    <tr>
		                        <td>${score.studentId}</td>
		                        <td>
		                            <!-- 個別月入力フィールド -->
			                        <input type="number" name="month_${score.studentId}" min="0" max="12" placeholder="月" class="month-input"
			                        <c:choose>
			                        	<c:when test="${input_months.containsKey(score.studentId)}">
			                        		value="${input_months.get(score.studentId)}"
			                        	</c:when>
			                        	<c:when test="${score.scoreMonth != -1}">
			                        		value="${score.scoreMonth}"
			                        	</c:when>
			                        </c:choose> />
		                        </td>
		                        <td>${score.student.studentName}</td>
		                        <td>${subject_name}</td>
		                        <td>${subject_code}</td>
		                        <td>
		                            <!-- 得点入力フィールド -->
		                            <input type="number" name="value_${score.studentId}" min="0" max="100" placeholder="得点" class="score-input"
		                             <c:choose>
		                             	<c:when test="${input_points.containsKey(score.studentId)}">
		                             		value="${input_points.get(score.studentId)}"
		                             	</c:when>
		                             	<c:when test="${score.scoreValue != -1}">
		                             		value="${score.scoreValue}"
		                             	</c:when>
		                             </c:choose> />
		                            <div class="mt-2 text-warning">${errors.get(score.studentId)}</div>
		                            <!-- 登録する学生番号を一覧として送る -->
		                            <input type="hidden" name="score_studentId_set[]" value="${score.studentId}" />
		                        </td>
		                    </tr>
		                </c:forEach>
		            </table>
		             <input type="hidden" name="courseYear" value="${courseYear}" />
		             <input type="hidden" name="class_id" value="${class_id}" />
		             <input type="hidden" name="subject_id" value="${subject_id}" />
		             <input type="hidden" name="subject_id_in" value="${subject_id}" />
		            <input class="btn btn-secondary" type="submit" value="登録" name="end" />
		        </form>
		    </c:when>
		    <c:when test="${score_list.size()==0}">
		        <div>学生情報が存在しませんでした</div>
		    </c:when>
			</c:choose>
		</section>
	</c:param>
</c:import>

