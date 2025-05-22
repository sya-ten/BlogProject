document.addEventListener("DOMContentLoaded", () => {
    const introduction = document.getElementById("introduction");
	
    if (introduction.textContent.trim() === "") {
        introduction.textContent = "自己紹介がありません。";
    }
});