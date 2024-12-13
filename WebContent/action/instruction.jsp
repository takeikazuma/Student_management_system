<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">指導表入力</c:param>

    <c:param name="scripts">
        <script type="text/javascript">
            $(function() {

             	// 行選択時の処理
                window.selectRow = function (row) {
             		//指導表番号をhiddenに転記
                    const instructionUniqueId = $(row).data('key');
                    console.log(instructionUniqueId);
                    $('#instructionId').val(instructionUniqueId);

                    // 値を編集エリアに転記
                    const cells = $(row).find('td');
                    $('input[name="inputDate"]').val(cells.eq(1).text()); // 日付
                    $('#inputInstructions').val(cells.eq(3).text().trim()); // 指導表内容
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
					//現在入力している学生番号を取得
                    let student_id  = document.getElementById("studentIdHidden");
                    student_id.value =  document.getElementById("student_id").value;
                    console.log(student_id.value);
					//サブミット
                    document.getElementById("instructionForm").submit();
                };

            });
        </script>
    </c:param>
    <c:param name="content">
        <section class="container mt-2">

        	<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生指導表入力</h2>

            <!-- 学生検索エリア -->
			<div class="search-section mb-2">
			    <div class="form-row">
			        <form action="SelectInstruction.action" method="post">
			            <div class="form-row d-flex">
			                <div class="col-3 mx-1">
			                    <input type="text" class="form-control" placeholder="学生番号" maxlength="8" id="student_id" name="student_id" value="${student_id}" required/>
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

<!-- 			<hr noshade> -->

			<!-- 指導表リストエリア -->

			<div style="width: 100%; overflow-x: auto;">
			    <table class="table table-hover" style="width: 100%; border-collapse: collapse;">
			        <!-- ヘッダ部分 -->
			        <c:if test="${not empty instruction_list}">
			            <thead class="table-secondary" style="display: table; width: 100%; table-layout: fixed;">
			                <tr>
			                    <th style="width:5%;">No</th>
			                    <th style="width:15%;">日付</th>
			                    <th style="width:15%;">入力者</th>
			                    <th style="width:65%;">指導表内容</th>
			                </tr>
			            </thead>
			        </c:if>
			    </table>
			    <!-- テーブルデータ部分 -->
			    <div style="max-height: 250px; overflow-y: auto;">
			        <table class="table table-hover" style="width: 100%; border-collapse: collapse;">
			            <tbody style="display: table; width: 100%; table-layout: fixed;">
			                <c:forEach var="instruction" items="${instruction_list}" varStatus="status">
			                    <tr data-key="${instruction.instructionId}" onclick="selectRow(this)">
			                        <td style="width:5%;">
			                            <a href="javascript:void(0);" onclick="selectRow(this.closest('tr'))">${status.index + 1}</a>
			                        </td>
			                        <td style="width:15%;"><c:out value="${instruction.inputDate}"></c:out></td>
			                        <td style="width:15%;"><c:out value="${instruction.usersName}"></c:out></td>
			                        <td style="width:65%;"><c:out value="${instruction.instructions}"></c:out></td>
			                    </tr>
			                </c:forEach>
			            </tbody>
			        </table>
			    </div>
			</div>


			<form action="InstructionHandler.action" method="post" id="instructionForm">
			    <!-- 入力エリア -->
			    <label for="for_inputdate" class="form-label">入力日</label>
			    <input class="form-control" id = "inputDate" name="inputDate" type="text" value="${inputDate}" placeholder="YYYY/MM/DD" required />
			    <label for="for_instruction" class="form-label">指導内容</label>
			    <textarea class="form-control" id="inputInstructions" name="inputInstructions" rows="3" required>${inputInstructions}</textarea>

			    <!-- hiddenエリア -->
			    <input type="hidden" id="instructionId" name="instructionId" value="${instructionId}" />
			    <input type="hidden" id="operation" name="operation" value="" />
			    <input type="hidden" id="studentIdHidden" name="studentIdHidden" value="${studentIdHidden}" />

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
