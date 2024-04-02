const teacherWrapper = document.querySelector(".teachers_wrapper");
const teacherCarousel = document.querySelector(".teachers_carousel");
const teacherFirstCardWidth = teacherCarousel.querySelector(".card").offsetWidth;
const teacherArrowBtns = document.querySelectorAll(".teachers_wrapper i");
const teacherCarouselChildrens = [...teacherCarousel.children];
let isTeacherDragging = false, teacherStartX, teacherStartScrollLeft, teacherTimeoutId;
let teacherCardPerView = Math.round(teacherCarousel.offsetWidth / teacherFirstCardWidth);
teacherCarouselChildrens.slice(-teacherCardPerView).reverse().forEach(card => {
    teacherCarousel.insertAdjacentHTML("afterbegin", card.outerHTML);
});
teacherCarouselChildrens.slice(0, teacherCardPerView).forEach(card => {
    teacherCarousel.insertAdjacentHTML("beforeend", card.outerHTML);
});

teacherCarousel.classList.add("no-transition");
teacherCarousel.scrollLeft = teacherCarousel.offsetWidth;
teacherCarousel.classList.remove("no-transition");

teacherArrowBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        teacherCarousel.scrollLeft += btn.classList.contains("left") ? -teacherFirstCardWidth : teacherFirstCardWidth;
    });
});
const teacherDragStart = (e) => {
    isTeacherDragging = true;
    teacherCarousel.classList.add("dragging");
    teacherStartX = e.pageX;
    teacherStartScrollLeft = teacherCarousel.scrollLeft;
}
const teacherDragging = (e) => {
    if(!isTeacherDragging) return;
    teacherCarousel.scrollLeft = teacherStartScrollLeft - (e.pageX - teacherStartX);
}
const teacherDragStop = () => {
    isTeacherDragging = false;
    teacherCarousel.classList.remove("dragging");
}
const teacherInfiniteScroll = () => {
    if(teacherCarousel.scrollLeft === 0) {
        teacherCarousel.classList.add("no-transition");
        teacherCarousel.scrollLeft = teacherCarousel.scrollWidth - (2 * teacherCarousel.offsetWidth);
        teacherCarousel.classList.remove("no-transition");
    }

    else if(Math.ceil(teacherCarousel.scrollLeft) === teacherCarousel.scrollWidth - teacherCarousel.offsetWidth) {
        teacherCarousel.classList.add("no-transition");
        teacherCarousel.scrollLeft = teacherCarousel.offsetWidth;
        teacherCarousel.classList.remove("no-transition");
    }

    clearTimeout(teacherTimeoutId);
}

teacherCarousel.addEventListener("mousedown", teacherDragStart);
teacherCarousel.addEventListener("mousemove", teacherDragging);
document.addEventListener("mouseup", teacherDragStop);
teacherCarousel.addEventListener("scroll", teacherInfiniteScroll);
teacherWrapper.addEventListener("mouseenter", () => clearTimeout(teacherTimeoutId));