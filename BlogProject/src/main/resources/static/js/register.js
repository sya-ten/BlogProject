const form = document.getElementById("registerForm")
const password = document.getElementById("password")
const rePassword = document.getElementById("rePassword")

form.addEventListener("submit", function (e) {
    if (password.value !== rePassword.value) {
        e.preventDefault(); // フォーム送信をキャンセル
        message.style.display = "block";
    } else {
        message.style.display = "none"; // 一致しているなら非表示
    }
});
