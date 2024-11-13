<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">指導表入力</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="container mt-4">
        	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
            <!-- 学生検索エリア -->
            <div class="search-section mb-4">
                <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生検索</h2>
                <div class="d-flex align-items-center">
                    <label for="studentNumber" class="form-label me-3">学生番号</label>
                    <input type="text" id="studentNumber" name="studentNumber" class="form-control me-3" style="width: 40px;">
                    <button type="button" class="btn btn-primary me-3">検索</button>
                    <label id="studentNameLabel" class="fw-bold me-3"></label>
                    <button type="button" class="btn btn-secondary">指導表表示</button>
                </div>
            </div>
            <section class="me-4">

            <!-- 指導表リストエリア -->
            <div class="instruction-list-section">
                <h2 class="">指導表リスト</h2>
                <table class="table table-hover">
                    <thead class="table-secondary">
                        <tr>
                            <th>連番</th>
                            <th>日付</th>
                            <th>入力者</th>
                            <th>指導表内容</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="instruction" items="${inscructionList}">
                            <tr>
                                <td>${instruction.seq}</td>
                                <td>${instruction.date}</td>
                                <td>${instruction.user_id}</td>
                                <td>${instruction.instructions}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </c:param>
</c:import>
