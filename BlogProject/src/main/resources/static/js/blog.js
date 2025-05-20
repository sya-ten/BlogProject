let blogLike = document.body.getAttribute("data-blogLike") == "true";
const liker_id = document.body.getAttribute("data-liker_id");
const blog_id = document.body.getAttribute("data-blog_id");
const likeImg = document.querySelector(".like-img");

window.addEventListener("DOMContentLoaded", () => {
    if (blogLike) {
        likeImg.style.backgroundColor = "rgb(255, 0, 0)";
    } else {
        likeImg.style.backgroundColor = "rgb(142, 141, 141)";
    }
});


function like() {
	axios.post("/blogLike?liker_id="+Number(liker_id)+"&blog_id="+Number(blog_id)+"&add="+!blogLike)
			.finally(
				blogLike = !blogLike
			)
	        .catch(error => {
	            console.error("ブログデータの取得に失敗しました:", error);
	        });
			
			if (blogLike) {
			        likeImg.style.backgroundColor = "rgb(255, 0, 0)";
			    } else {
			        likeImg.style.backgroundColor = "rgb(142, 141, 141)";
			    }
}
