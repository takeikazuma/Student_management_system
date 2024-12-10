<%-- 学生検索JSP --%>
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
            		<% int id = 0; %>
	            	<c:forEach var="list" items="${student}">
	            		<tr>
	            			<% id += 1; %>
	            			<td class="students">
	            				<a href="javascript:void(0);"  id = <%= id %>>
	            					${list.getStudentId()}
	            				</a>
	            			</td>
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
    </c:param>
</c:import>

<script>
	// クリックした要素の取得
	let students = document.getElementsByClassName("students");
	let student = Array.from(students);

	// クリックした要素の学生番号を取得
	student.forEach(function(target){
		target.addEventListener("click", function(){
			// 親ウィンドウの存在チェック
			if(!window.opener || window.opener.closed){
				window.alert('親ウィンドウがありません。');
				return false;
			}
			// 子ウィンドウから親ウィンドウへ学生番号を渡す
			let callback = window.opener.document.getElementById("student_id");
			callback.value = target.innerText;
			// 子ウィンドウを閉じる
			window.close();
		})
	})
</script>