<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">指導表入力</c:param>

    <c:param name="scripts">
        <script type="text/javascript">
            $(function() {

             	// 行選択時の処理
                window.selectRow = function (row) {
                    const uniqueKey = $(row).data('key');
                    console.log(uniqueKey);
                    $('#instructionId').val(uniqueKey);

                    // 値を編集エリアに転記
                    const cells = $(row).find('td');
                    $('input[name="input_date"]').val(cells.eq(1).text()); // 日付
                    $('#input_instructions').val(cells.eq(3).text().trim()); // 指導表内容
                };

                //検索ボタン押下時学生検索画面を開く処理を追加
                const immediate = () => {
                	window.open(
    			            'searchStudentId.jsp',
    			            'searchStudentId',
    			            'width=600,height=600,scrollbars=yes,resizable=yes'
    			        );
			    };
			    document.getElementById('search').addEventListener('click', immediate);


             	// フォーム送信処理
                window.submitForm = function(operation) {
                    // 操作の種類に応じてフォームを送信
                    document.getElementById("operation").value = operation;
                    console.log(operation);
					//現在入力している学生番号を取得
                    let student_id  = document.getElementById('student_id_hidden');
                    student_id.value =  document.getElementById("student_id").value;
                    console.log(student_id.value);
					//サブミット
                    document.getElementById("instructionForm").submit();
                };

            });
        </script>
    </c:param>
    <c:param name="content">
        <section class="container mt-4">

        	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生指導表入力</h2>

            <!-- 学生検索エリア -->
			<div class="search-section mb-4">
			    <div class="form-row">
			        <form action="SelectInstruction.action" method="post">
			            <div class="form-row d-flex">
			                <div class="col-3 mx-1">
			                    <input type="text" class="form-control" placeholder="学生番号" maxlength="8" id="student_id" name="student_id" value="${studentId}" />
			                </div>
			                <div class="col-auto mx-1">
			                    <input class="btn btn-primary" id="search" type="button" name="search" value="検索" />
			                </div>
			                <div class="col-5 mx-1">
			                    <input class="form-control" type="text" placeholder="学生氏名" maxlength="20" value="${studentName}" aria-label="" readonly />
			                </div>
			                <div class="col-auto mx-1">
			                    <input class="btn btn-primary" type="submit" name="showInstruction" value="指導表表示" />
			                </div>
			            </div>
			        </form>
			    </div>
			</div>

			<hr noshade>
            <!-- 指導表リストエリア -->
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
                    <tbody  class="table-group-divider">
                        <c:forEach var="instruction" items="${instruction_list}" varStatus="status">
                            <tr data-key="${instruction.instructionId}" onclick="selectRow(this)">
                                <td>
                                	<a href="javascript:void(0);" onclick="selectRow(this.closest('tr'))"> ${status.index + 1}</a>
                                </td>
                                <td><c:out value="${instruction.inputDate}"></c:out></td>
                                <td><c:out value="${instruction.usersName}"></c:out></td>
                                <td><c:out value="${instruction.instructions}"></c:out></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

			<form action="InstructionHandler.action" method="post" id="instructionForm">
			    <!-- 入力エリア -->
			    <label for="for_inputdate" class="form-label">入力日</label>
			    <input class="form-control" name="input_date" type="text" value="${input_date}" placeholder="YYYY-MM-DD" required />
			    <label for="for_instruction" class="form-label">指導内容</label>
			    <textarea class="form-control" id="input_instructions" name="input_instructions" rows="4" required>${input_instructions}</textarea>

			    <!-- hiddenエリア -->
			    <input type="hidden" id="instructionId" name="instructionId" value="${instructionId}" />
			    <input type="hidden" id="operation" name="operation" value="" />
			    <input type="hidden" id="student_id_hidden" name="student_id_hidden" value="${student_id_hidden}" />

				<!-- メッセージ表示エリア -->
	            <div class="my-2 text-danger">${message}</div>

			    <!-- ボタンエリア -->
			    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
			        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('delete')">行削除</button>
			        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('register')">登録</button>
			        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('update')">修正</button>
			        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('export')">一覧出力</button>
			        <button type="button" class="w-25 btn btn-primary" onclick="window.close()">閉じる</button>
			    </div>
			</form>

        </section>
    </c:param>
</c:import>
