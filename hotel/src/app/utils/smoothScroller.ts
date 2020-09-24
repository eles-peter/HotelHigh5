export function scrollToTheTop(speed: number) {
  let scrollToTop = window.setInterval(() => {
    let pos = window.pageYOffset;
    if (pos > 0) {
      window.scrollTo(0, pos - speed);
    } else {
      window.clearInterval(scrollToTop);
    }
  }, 16);
}
