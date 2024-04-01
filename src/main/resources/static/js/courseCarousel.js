const courseWrapper = document.querySelector(".courses_wrapper");
const courseCarousel = document.querySelector(".courses_carousel");
const courseFirstCardWidth = courseCarousel.querySelector(".card").offsetWidth;
const courseArrowBtns = document.querySelectorAll(".courses_wrapper i");
const courseCarouselChildrens = [...courseCarousel.children];
let isCourseDragging = false, courseStartX, courseStartScrollLeft, courseTimeoutId;
let courseCardPerView = Math.round(courseCarousel.offsetWidth / courseFirstCardWidth);
courseCarouselChildrens.slice(-courseCardPerView).reverse().forEach(card => {
    courseCarousel.insertAdjacentHTML("afterbegin", card.outerHTML);
});
courseCarouselChildrens.slice(0, courseCardPerView).forEach(card => {
    courseCarousel.insertAdjacentHTML("beforeend", card.outerHTML);
});

courseCarousel.classList.add("no-transition");
courseCarousel.scrollLeft = courseCarousel.offsetWidth;
courseCarousel.classList.remove("no-transition");

courseArrowBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        courseCarousel.scrollLeft += btn.classList.contains("left") ? -courseFirstCardWidth : courseFirstCardWidth;
    });
});
const courseDragStart = (e) => {
    isCourseDragging = true;
    courseCarousel.classList.add("dragging");
    courseStartX = e.pageX;
    courseStartScrollLeft = courseCarousel.scrollLeft;
}
const courseDragging = (e) => {
    if(!isCourseDragging) return;
    courseCarousel.scrollLeft = courseStartScrollLeft - (e.pageX - courseStartX);
}
const courseDragStop = () => {
    isCourseDragging = false;
    courseCarousel.classList.remove("dragging");
}
const courseInfiniteScroll = () => {
    if(courseCarousel.scrollLeft === 0) {
        courseCarousel.classList.add("no-transition");
        courseCarousel.scrollLeft = courseCarousel.scrollWidth - (2 * courseCarousel.offsetWidth);
        courseCarousel.classList.remove("no-transition");
    }

    else if(Math.ceil(courseCarousel.scrollLeft) === courseCarousel.scrollWidth - courseCarousel.offsetWidth) {
        courseCarousel.classList.add("no-transition");
        courseCarousel.scrollLeft = courseCarousel.offsetWidth;
        courseCarousel.classList.remove("no-transition");
    }

    clearTimeout(courseTimeoutId);
}

courseCarousel.addEventListener("mousedown", courseDragStart);
courseCarousel.addEventListener("mousemove", courseDragging);
document.addEventListener("mouseup", courseDragStop);
courseCarousel.addEventListener("scroll", courseInfiniteScroll);
courseWrapper.addEventListener("mouseenter", () => clearTimeout(courseTimeoutId));