export function getPublicId(url: string) {
  if (url) {
    return url.substring(61, url.length - 4);
  } else {
    return "image_not_found"
  }
}
