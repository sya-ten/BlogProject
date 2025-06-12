document.addEventListener("DOMContentLoaded", () => {
    const introduction = document.getElementById("introduction");
	console.log(introduction.textContent)
    if (introduction && introduction.textContent.trim() === "") {
        introduction.textContent = "自己紹介がありません。";
    }
});