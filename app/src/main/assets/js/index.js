console.log("Hello JavaScript");

let x;
for (x = 0; x < 5; x++) {
    console.log(`Forward = ${x + 1}, Backward = ${5 - x}`);
}

document.getElementById("index_header").textContent = "The things I love"
document.getElementById("change_text_button").onclick = set_changed_text;
document.getElementById("page_title").textContent = "Home Page (Kemo)";

function set_changed_text() {
    text_edit = document.getElementById("text_edit").value;
    document.getElementById("changed_text").textContent = text_edit;
}
