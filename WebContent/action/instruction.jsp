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
                    console.log("ここ通ってるよ");
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

                    //登録・修正の場合は入力チェックを行う
                    if (operation === "register" || operation === "update") {
                    	//入力日
                    	const inputDate = document.getElementById("inputDate").value;
                    	//指導内容
                    	const inputInstructions = document.getElementById("inputInstructions").value;

                    	//日付の必須チェック
                        if (!inputDate) {
                            alert("日付を入力してください。");
                            return false;
                        }

                    	//指導内容の必須チェック
                    	if (!inputInstructions){
                    		alert("指導内容を入力してください。");
                    		return false;
                    	}

                     	//日付形式の正規表現 (yyyy-MM-dd)
                        const datePattern = /^\d{4}-\d{2}-\d{2}$/;

                        // 入力形式をチェック
                        if (!datePattern.test(inputDate)) {
                            alert("日付はyyyy-MM-dd形式で入力してください。");
                            return false;
                        }

                        // 実際に有効な日付かチェック（isNaN=trueは不正な日付）
                        const targetDate = new Date(inputDate);

                        console.log("isNaN(targetDate.getDate())", isNaN(targetDate.getDate()));

                        if (isNaN(targetDate.getDate())) {
                            alert("無効な日付が入力されています。正しい日付を入力してください。");
                            return false;
                        }
                    }

                    //削除の場合は行選択がされているか(hiddenのinstructionIdに値が入っているか)チェック
                    if (operation === "delete") {
                    	const instructionId = document.getElementById("instructionId").value;
                    	if (!instructionId){
                    		alert("削除対象の行を選択してください。");
                    		return false;
                    	}
                    }

                  	//CSV出力の場合は学生番号が入力されているかチェック
                    if (operation === "export") {
                    	const studentId = document.getElementById("student_id").value;
                    	if (!studentId){
                    		alert("学生番号を入力してください。");
                    		return false;
                    	}
                    }
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

			<div class="border" style="width: 100%; overflow-x: auto;">
			    <table class="table table-hover" style="width: 100%; border-collapse: collapse;border-spacing: 0;margin: 0;">
			        <!-- ヘッダ部分 -->
			        <c:if test="${not empty instruction_list}">
			            <thead class="table-secondary" style="display: table; width: 100%; table-layout: fixed; padding: 0;">
			                <tr style="padding: 0;">
			                    <th style="width:5%;">No</th>
			                    <th style="width:15%;">日付</th>
			                    <th style="width:15%;">入力者</th>
			                    <th style="width:65%;">指導表内容</th>
			                </tr>
			            </thead>
			        </c:if>
			    </table>
			    <!-- テーブルデータ部分 -->
			    <div style="max-height: 265px; overflow-y: auto; margin: 0;">
			        <table class="table table-hover" style="width: 100%; border-collapse: collapse; border-spacing: 0; margin: 0;">
			            <tbody style="display: table; width: 100%; table-layout: fixed;">
			                <c:forEach var="instruction" items="${instruction_list}" varStatus="status">
			                    <tr data-key="${instruction.instructionId}" onclick="selectRow(this)" style="padding: 0;">
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
			<div class="py-1"></div>

			<!-- ボタン使用不可/可の切り替えのためのdiv -->
			<div id=instructionFormArea" class="py-2">
				<form action="InstructionHandler.action" method="post" id="instructionForm">
				    <!-- 入力エリア -->
<!-- 					<div class="mb-3 row"> -->
<!-- 						<label for="inputDate" class="col-sm-1 col-form-label">入力日</label> -->
<!-- 						<div class="col-sm-2"> -->
<%-- 							<input class="form-control" id = "inputDate" name="inputDate" type="date" value="${inputDate}" required /> --%>
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="mb-3 row"> -->
<!-- 						<label for="inputInstructions" class="col-sm-1 col-form-label">指導内容</label> -->
<!-- 						<div class="col-sm-11"> -->
<%-- 							<textarea class="form-control" id="inputInstructions" name="inputInstructions" rows="3" required>${inputInstructions}</textarea> --%>
<!-- 						</div> -->
<!-- 					</div> -->

					<!-- 入力エリア -->
					<div class="mb-2 d-flex align-items-start">
					    <!-- 入力日 -->
					    <div class="me-4">
					        <label for="inputDate" class="form-label">入力日</label>
					        <input class="form-control" id="inputDate" name="inputDate" type="date" value="${inputDate}" style="width: 200px;" required />
					    </div>

					    <!-- 指導内容 -->
					    <div style="flex: 1;">
					        <label for="inputInstructions" class="form-label">指導内容</label>
					        <textarea class="form-control" id="inputInstructions" name="inputInstructions" rows="2" required>${inputInstructions}</textarea>
					    </div>
					</div>

				    <!-- hiddenエリア -->
				    <input type="hidden" id="instructionId" name="instructionId" value="${instructionId}" />
				    <input type="hidden" id="operation" name="operation" value="" />
				    <input type="hidden" id="studentIdHidden" name="studentIdHidden" value="${studentIdHidden}" />

					<!-- メッセージ表示エリア -->
		            <div class="my-2 text-danger" id="message">${message}</div>

				    <!-- ボタンエリア -->
				    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-3">
				    	<button type="button" class="w-25 btn btn-primary" onclick="submitForm('register')">登録</button>
				    	<button type="button" class="w-25 btn btn-primary" onclick="submitForm('update')">修正</button>
				        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('delete')">行削除</button>
				        <button type="button" class="w-25 btn btn-primary" onclick="submitForm('export')">CSV出力</button>
<!-- 				        <button type="button" class="w-25 btn btn-primary" onclick="window.close()">閉じる</button> -->
				    </div>
				</form>
			</div>
        </section>
    </c:param>
</c:import>
