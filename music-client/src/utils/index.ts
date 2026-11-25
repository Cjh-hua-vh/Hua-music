import { getBaseURL } from "@/utils/request";

export function attachImageUrl(url) {
  const fallbackPath = "/img/singerPic/tubiao.jpg";

  let normalizedPath = url || fallbackPath;

  if (!normalizedPath || normalizedPath === "" || normalizedPath === "null" || normalizedPath === "undefined") {
    normalizedPath = fallbackPath;
  }

  if (/^https?:\/\//i.test(normalizedPath)) {
    return normalizedPath;
  }

  const baseUrl = (getBaseURL() || "").replace(/\/+$/, "");

  let path = normalizedPath;
  if (!path.startsWith("/")) {
    path = `/${path}`;
  }

  return `${baseUrl}${path}`;
}

export function getSongTitle(name: string) {
  if (typeof name !== "string") return "";

  const splitName = name.split("-");

  return splitName.length > 1 ? splitName[1].trim() : splitName[0].trim();
}

export function getSingerName(name: string) {
  if (typeof name !== "string") return "";
  const splitName = name.split("-");
  return splitName[0] ? splitName[0].trim() : "";
}

export function getUserSex(sex: number) {
  if (sex === 0) return "女";
  if (sex === 1) return "男";
  return "";
}

export function getBirth(value) {
  if (value == null || value == "") return "";
  const date = new Date(value);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  return year + "-" + month + "-" + day;
}

export function formatDate(cellValue) {
  if (cellValue == null || cellValue == "") return "";
  const date = new Date(cellValue);
  const year = date.getFullYear();
  const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  const day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  const hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  const minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  const seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

export function parseLyric(text) {
  let lines = text.split("\n");

  const pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;

  const result = [];

  if (!/\[.+\]/.test(text)) {
    return [[0, text]];
  }

  while (!pattern.test(lines[0])) {
    lines = lines.slice(1);
  }

  lines[lines.length - 1].length === 0 && lines.pop();

  for (const item of lines) {
    const time = item.match(pattern);
    const value = item.replace(pattern, "");

    for (const item1 of time) {
      const t = item1.slice(1, -1).split(":");

      if (value !== "") {
        result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
      }
    }
  }

  result.sort((a, b) => a[0] - b[0]);

  return result;
}

export function formatSeconds(value) {
  let theTime = parseInt(value);
  let theTime1 = 0;
  let theTime2 = 0;

  if (theTime > 60) {
    theTime1 = parseInt((theTime / 60).toString());
    theTime = parseInt((theTime % 60).toString());
    if (theTime1 > 60) {
      theTime2 = parseInt((theTime1 / 60).toString());
      theTime1 = 60;
    }
  }

  let result = "";
  if (parseInt(theTime.toString()) < 10) {
    result = "0:0" + parseInt(theTime.toString());
  } else {
    result = "0:" + parseInt(theTime.toString());
  }

  if (theTime1 > 0) {
    if (parseInt(theTime.toString()) < 10) {
      result = "0" + parseInt(theTime.toString());
    } else {
      result = parseInt(theTime.toString()).toString();
    }
    result = parseInt(theTime1.toString()) + ":" + result;
  }

  if (theTime2 > 0) {
    if (parseInt(theTime.toString()) < 10) {
      result = "0" + parseInt(theTime.toString());
    } else {
      result = parseInt(theTime.toString()).toString();
    }
    result = parseInt(theTime2.toString()) + ":" + parseInt(theTime1.toString()) + ":" + result;
  }

  return result;
}
