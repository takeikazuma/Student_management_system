<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">指導表入力</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section class="container mt-4">

        	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">学生指導表入力</h2>

            <!-- 学生検索エリア -->
            <div class="search-section mb-4">
                <div class="form-row">

                	<form action="SelectInstruction.action" method="post">
                		<div class="form-row">
	                		<div class="col">
		                    	<input type="text" class="form-control" placeholder="学生番号" id="student_id" name="student_id" class="form-control me-3">
		                    </div>
		                    <div class="col">
		                    	<input class="w-25 btn btn-lg btn-primary" type="submit" name="search" value="検索" />
		                    </div>
							<div class="col">
		                    	<input class="form-control" type="text"  placeholder="学生氏名"  value="${studentName}" aria-label="" readonly>
							</div>
                    		<div class="col">
                       			<input class="w-25 btn btn-lg btn-primary" type="submit" name="showInstruction" value="指導表表示" />
                       		</div>
                    	</div>
	            	</form>
                </div>
            </div>
			<hr noshade>
            <!-- 指導表リストエリア -->
            <form action="SelectInstruction.action" method="post">
	            <div class="instruction-list-section">
	                <table class="table table-hover">
	                    <thead class="table-secondary">
	                        <tr>
	                            <th><a href="">No</a></th>
	                            <th>日付</th>
	                            <th>入力者</th>
	                            <th>指導表内容</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <c:forEach var="instruction" items="${instruction_list}">
	                            <tr>
	                                <td><a href=""></a></td>
	                                <td>${instruction.inputDate}</td>
	                                <td>${instruction.usersName}</td>
	                                <td>${instruction.instructions}</td>
	                            </tr>
	                        </c:forEach>
	                    </tbody>
	                </table>
	            </div>
	        </form>

            <!-- 指導表情報入力エリア -->
			<label for="for_inputdate" class="form-label">入力日</label>
            <input class="form-control" name="input_date" type="text" value="${input_date}"/>
            <label for="for_instruction" class="form-label">指導内容</label>
            <textarea class="form-control" id="input_instructions" rows="3" value="${input_instructions}"></textarea>

            <!-- ページ下部操作ボタンエリア -->
            <div class="mt-4">
            	<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="行削除" />
            	<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="登録" />
            	<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="修正" />
            	<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="一覧出力" />
				<input class="w-25 btn btn-lg btn-primary" type="button" onclick="window.close()" value="閉じる" />
            </div>

        </section>
    </c:param>
</c:import>
