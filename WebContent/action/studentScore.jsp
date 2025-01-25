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
    <script type="text/javascript">
            $(function() {
                // カスタムバリデーションメッセージを設定
                $('#id-input').on('invalid', function() {
                    this.setCustomValidity('学生番号を入力してください。');
                });
                $('#id-input').on('input', function() {
                    this.setCustomValidity('');
                });

                // 学生番号の桁数制限（7桁以内）
                $('#id-input').on('input', function() {
                    const maxDigits = 7;
                    if ($(this).val().length > maxDigits) {
                        $(this).val($(this).val().slice(0, maxDigits));
                    }
                });
                //検索ボタン押下時学生検索画面を開く処理を追加
                const immediate = () => {
                	window.open(
    			            'searchStudentId.jsp',
    			            'searchStudentId',
    			            'width=600,height=600,scrollbars=yes,resizable=yes'
    			        );
			    };
			    document.getElementById('search').addEventListener('click', immediate);
            });
        </script>
	</c:param>

	<c:param name="content">
	  <style>
            /* 数値の上下ボタンを非表示にするCSS */
            .no-spinner::-webkit-outer-spin-button,
            .no-spinner::-webkit-inner-spin-button {
                -webkit-appearance: none;
                margin: 0;
            }

            .no-spinner {
                -moz-appearance: textfield;
            }
        </style>
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績入力</h2>
			<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter"
					style="width: 100%; /* デフォルトは親要素に合わせる */
				    max-width: 600px;   /* 最大幅を600pxに制限 */
				    min-width: 150px;   /* 最小幅を150pxに制限 */
				    box-sizing: border-box; /* パディングやボーダーを含めた幅調整 */;">
				    <form method="post" action="StudentScore.action" class="d-flex align-items-center w-100">
				        <!-- 学生番号入力 -->
				        <div class="flex-grow-1 me-3">
				            <label class="form-label" for="student_id">学生番号</label>
				            <input
				                class="form-control px-5 fs-5 no-spinner"
				                autocomplete="off"
				                id="student_id"
				                maxlength="7"
				                name="student_id"
				                placeholder="7桁の整数で入力してください"
				                style="ime-mode: disabled"
				                type="text"
				                pattern="\d{7}"
				                title="7桁の整数を入力してください"
				                value="${student_id}"
				                required
				            />
				        </div>
				        <!-- 絞込みボタン -->
				        <div>
				            <button class="btn btn-secondary" id="filter-button">絞込み</button>
				        </div>

				         <!-- 学生番号検索ボタン -->
				        <div class="me-3">
				            <input
				                class="btn btn-primary"
				                id="search"
				                type="submit"
				                name="search"
				                value="学生番号検索"
				            />
				        </div>

				    </form>
				</div>
			<c:choose>
		    <c:when test="${score_list.size()>0}">
		       <form method="post" id="test-form" action="StudentRegister.action">
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
		                        <td>${student_info_id}</td>
		                        <td>
		                            <!-- 個別月入力フィールド -->
			                        <input type="number" name="month_${score.subjectId}" min="0" max="12" placeholder="月" class="month-input"
			                        <c:choose>
			                        	<c:when test="${input_months.containsKey(score.studentId)}">
			                        		value="${input_months.get(score.studentId)}"
			                        	</c:when>
			                        	<c:when test="${score.scoreMonth != -1}">
			                        		value="${score.scoreMonth}"
			                        	</c:when>
			                        </c:choose> />
		                        </td>
		                        <td>${student_info_name}</td>
		                        <td>${score.subject.subjectName}</td>
		                        <td>${score.subject.subjectCode}</td>
		                        <td>
		                            <!-- 得点入力フィールド -->
		                            <input type="number" name="value_${score.subjectId}" min="0" max="100" placeholder="得点" class="score-input"
		                             <c:choose>
		                             	<c:when test="${input_points.containsKey(score.studentId)}">
		                             		value="${input_points.get(score.studentId)}"
		                             	</c:when>
		                             	<c:when test="${score.scoreValue != -1}">
		                             		value="${score.scoreValue}"
		                             	</c:when>
		                             </c:choose> />
		                            <div class="mt-2 text-warning">${errors.get(score.studentId)}</div>
		                            <!-- 登録する成績IDを一覧として送る -->
		                            <input type="hidden" name="scoreid_${score.subjectId}" value="${score.scoreId}" />
		                             <!-- 登録する科目IDを一覧として送る -->
		                            <input type="hidden" name="score_subjectId_set[]" value="${score.subjectId}" />
		                        </td>
		                    </tr>
		                </c:forEach>
		            </table>
		            <input type="hidden" name="student_id" value="${score_list[0].getStudentId()}" />
		            <input class="btn btn-secondary" type="submit" value="登録" name="end" />
		        </form>
		    </c:when>
		    <c:when test="${student_info.size()==0}">
		        <div><h3>学生情報が存在しませんでした</h3></div>
		    </c:when>
			</c:choose>
		</section>
	</c:param>
</c:import>

