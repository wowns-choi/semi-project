const lecturer = document.getElementById("lecturer-faq");
const student = document.getElementById("student-faq");

if( location.pathname == '/faq/lecturer') {
    lecturer.classList.add("addColor");
    student.classList.remove("addColor");
    console.log(lecturer.classList);
} else {
    lecturer.classList.remove("addColor");
    student.classList.add("addColor");
}