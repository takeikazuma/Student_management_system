<%-- 成績入力(クラス別) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		成績入力(クラス別)
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績入力</h2>
			<form action="ClassScoreIn.action" method="post">
				<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
					<div class="col-2">
						<label class="form-label" for="student-f1-select">履修学年 </label>
						<select class="form-select " id="student-f1-select" name="courseYear">
							<option value="0">--------</option>
							<option value="1">1年生</option>
							<option value="2">2年生</option>
							<option value="3">3年生</option>
							<option value="4">4年生</option>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">クラス</label>
						<select class="form-select " id="student-f2-select" name="class_id">
							<option value="0">--------</option>
							<c:forEach var="list" items="${class_list}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${list.getGradeClassId()}" <c:if test="${list.getGradeClassId()==id}">selected</c:if>>${list.getGradeClassName()}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="student-f2-select">科目名</label>
						<select class="form-select " id="student-f2-select" name="subject_id">
							<option value="0">--------</option>
							<c:forEach var="subject_name" items="${subject_list}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${subject_name.getSubjectId()}" <c:if test="${subject_name.getSubjectId()==id}">selected</c:if>>${subject_name.getSubjectName()}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">絞込み</button>
					</div>
				</div>
			</form>
		</section>
	</c:param>
</c:import>

