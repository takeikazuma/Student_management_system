<%-- ログインJSP --%>
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
                        $('#password-input').attr('type', 'text');
                    } else {
                        $('#password-input').attr('type', 'password');
                    }
                });

                // カスタムバリデーションメッセージを設定
                $('#id-input').on('invalid', function() {
                    this.setCustomValidity('ユーザIDを入力してください。');
                });
                $('#id-input').on('input', function() {
                    this.setCustomValidity('');
                });

                $('#password-input').on('invalid', function() {
                    this.setCustomValidity('パスワードを入力してください。');
                });
                $('#password-input').on('input', function() {
                    this.setCustomValidity('');
                });

                // ユーザIDの桁数制限（10桁以内）
                $('#id-input').on('input', function() {
                    const maxDigits = 9;
                    if ($(this).val().length > maxDigits) {
                        $(this).val($(this).val().slice(0, maxDigits));

                    }
                });
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
        <section class="w-75 text-center m-auto border pb-3">
            <form action="GetUser.action" method="post">
                <div id="wrap_box">
                    <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">ログイン</h2>
                    <c:if test="${errors.size() > 0}">
                        <div>
                            <c:forEach var="error" items="${errors}">
                                ${error}
                            </c:forEach>
                        </div>
                    </c:if>
                    <div>
                        <!-- ユーザID -->
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="id-input" maxlength="9" name="id" placeholder="最大10桁の整数を入力してください"
                                   style="ime-mode: disabled" type="number" value="${id}" required />
                            <label>ユーザID</label>
                        </div>
                        <!-- パスワード -->
                        <div class="form-floating mx-5 mt-3">
                            <input class="form-control px-5 fs-5" autocomplete="off"
                                   id="password-input" maxlength="20" name="password"
                                   placeholder="20文字以内の半角英数字でご入力下さい" style="ime-mode: disabled"
                                   type="password" required />
                            <label>パスワード</label>
                        </div>
                        <div class="form-check mt-3">
                            <label class="form-check-label" for="password-display">
                                <input class="form-check-input" id="password-display" name="chk_d_ps" type="checkbox" />
                                パスワードを表示
                            </label>
                        </div>
                    </div>

                    <div class="mt-4">
                        <input class="w-25 btn btn-lg btn-primary" type="submit" name="login" value="ログイン" />
                    </div>
                </div>
            </form>
        </section>
    </c:param>
</c:import>