<%-- ログインJSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">
        学生番号検索
    </c:param>

    <c:param name="content">

	    <!-- 未入力チェック処理を追加実装する -->

        <section class="w-75 text-center m-auto border pb-3">
            <form action="SelectStudentId.action" method="post">
                <div id="wrap_box">
                    <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">学生番号検索</h2>
                    <div>
                        <!-- 学生氏名 -->
                        <div class="form-floating mx-5">
                        	<label>学生氏名</label>
                            <input class="form-control px-5 fs-5 no-spinner" name="name"
                                   type="text" value="${name}"/>
                        </div>
                        <!-- 学生カナ -->
                        <div class="form-floating mx-5 mt-3">
	                        <label>学生カナ</label>
                            <input class="form-control px-5 fs-5 no-spinner" name="kana"
                                   type="text" value="${kana}"/>
                        </div>
                        <!-- クラス -->
                        <div class="form-floating mx-5 mt-3">
                        	<label>クラス</label>
                            <input class="form-control px-5 fs-5 no-spinner" name="gradeClass"
                                   style="ime-mode: disabled" type="text" value="${gradeClass}"/>
                        </div>
                    </div>

                    <div class="mt-4">
                        <input class="w-25 btn btn-lg btn-primary" type="submit" name="search" value="検索" />
                    </div>

                </div>
            </form>

            <c:if test="${errors.size() > 0}">
            	<br>
                <div>
                    <c:forEach var="error" items="${errors}">
                        ${error}
                    </c:forEach>
                </div>
            </c:if>

			<br>
            <table class="table table-hover" >
            	<thead>
	            	<tr>
	            		<th>学生番号</th>
	            		<th>クラス</th>
	            		<th>氏名</th>
	            	</tr>
            	</thead>
            	<tbody class="table-group-divider">
	            	<c:forEach var="list" items="${student}">
	            		<tr>
	            			<td><a href="">${list.getStudentId()}</a></td>
	            			<td>${list.getGradeClassName()}</td>
	            			<td>${list.getStudentName()}</td>
	            		</tr>
	            	</c:forEach>
            	</tbody>
            </table>
            <br>

            <div class="mt-4">
				<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="閉じる" />
            </div>

        </section>

        <!-- 呼び出し元に選択した学生番号を返却する処理を追加実装する -->

    </c:param>
</c:import>