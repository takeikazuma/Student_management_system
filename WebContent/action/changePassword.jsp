<%-- パスワード変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">
        経理本科教務システム
    </c:param>

    <c:param name="scripts">
        <script type="text/javascript">
            $(function() {
                // 「パスワードを表示」が変更された時の処理
                $('#password-display').change(function() {
                    if ($(this).prop('checked')) {
                        $('#password-input1').attr('type', 'text');
                    } else {
                        $('#password-input1').attr('type', 'password');
                    }
                    if ($(this).prop('checked')) {
                        $('#password-input2').attr('type', 'text');
                    } else {
                        $('#password-input2').attr('type', 'password');
                    }
                    if ($(this).prop('checked')) {
                        $('#password-input3').attr('type', 'text');
                    } else {
                        $('#password-input3').attr('type', 'password');
                    }
                });

                // カスタムバリデーションメッセージを設定
                $('#password-input1').on('invalid', function() {
                    this.setCustomValidity('現在のパスワードを入力してください。');
                });
                $('#password-input1').on('input', function() {
                    this.setCustomValidity('');
                });
                $('#password-input2').on('invalid', function() {
                    this.setCustomValidity('新しいパスワードを入力してください。');
                });
                $('#password-input2').on('input', function() {
                    this.setCustomValidity('');
                });

                // 新しいパスワードと新しいパスワード（確認用）が異なる場合、エラー
                $('#password-input3').on('input', function() {
                	if ($('#password-input2').val() !== $('#password-input3').val()) {
                		this.setCustomValidity('新しいパスワードが一致しません');
                	} else {
                		this.setCustomValidity('');
                	}
                });


            });
        </script>
    </c:param>

    <c:param name="content">
        <section class="w-75 text-center m-auto border pb-3">
            <form action="ChangePassword.action" method="post">
                <div id="wrap_box">
                    <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">パスワード変更</h2>
                    <div>

                        <!-- 現在のパスワード -->
                        <div class="form-floating mx-5 mt-3">
                        	<label>現在のパスワード</label>
                            <input class="form-control px-5 fs-5 no-spinner" name="nowPassword" id="password-input1"
                                   style="ime-mode: disabled" type="password" value="${nowPassword}" required />
                        </div>
                        <!-- 新しいパスワード -->
                        <div class="form-floating mx-5 mt-3">
                        	<label>新しいパスワード</label>
                            <input class="form-control px-5 fs-5 no-spinner" name="newPassword" id="password-input2"
                                   style="ime-mode: disabled" type="password" value="${newPassword}" required />
                        </div>
                        <!-- 新しいパスワード（確認用） -->
                        <div class="form-floating mx-5 mt-3">
                        	<label>新しいパスワード（確認用）</label>
                            <input class="form-control px-5 fs-5" name="newPasswordRe" id="password-input3"
                                   style="ime-mode: disabled" type="password" value="${newPasswordRe}" required />
                        </div>
                        <!-- チェックボックス -->
                        <div class="form-check mt-3">
                            <label class="form-check-label" for="password-display">
                                <input class="form-check-input" id="password-display" name="chk_d_ps" type="checkbox" />
                                パスワードを表示
                            </label>
                        </div>
                    </div>

                    <div class="mt-4">
                        <input class="w-25 btn btn-lg btn-primary" type="submit" name="change" value="変更" />
                    </div>
                </div>
            </form>
            <div class="form-floating mx-5 mt-3">${message}</div>
        </section>
    </c:param>
</c:import>