let form = document.getElementById("registerForm")
let password = document.getElementById("password")
let rePassword = document.getElementById("rePassword")

form.addEventListener("submit", function (e) {
    if (password.value !== rePassword.value) {
        e.preventDefault(); // フォーム送信をキャンセル
        message.style.display = "block";
    } else {
        message.style.display = "none"; // 一致しているなら非表示
    }
});
