const title = document.body.getAttribute("data-title");
console.log(title);
document.addEventListener("DOMContentLoaded", () => {
    axios.get("/blogList/data?title="+title)
        .then(response => {
            const blogs = response.data;
            const blogList = document.getElementById("blogList");

            blogs.forEach(blog => {
                const blogDiv = document.createElement("div");
                blogDiv.className = "blog";

                blogDiv.innerHTML = `
                    <div onclick="location.href='/blog/${blog.blogId}';" style="cursor: pointer;">
                        <span>${blog.title}</span>
                        <span>${blog.author}</span>
                        <span>${blog.createTm}</span>
                    </div>
                    <div>${blog.content}</div>
                `;

                blogList.appendChild(blogDiv);
            });
        })
        .catch(error => {
            console.error("ブログデータの取得に失敗しました:", error);
        });
});
