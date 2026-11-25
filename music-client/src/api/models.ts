/** 列表类接口的通用返回结构 */
export interface HomeCardItem {
  id: number;
  pic: string;
  [key: string]: unknown;
}

/** 轮播图，对应 banner 表；title 与 url 可能为空。 */
export interface BannerItem {
  id: number;
  pic: string;
  title?: string;
  url?: string;
  [key: string]: unknown;
}

/** 歌单曲风，对应 song_sheet_style 表。 */
export interface SongStyleItem {
  id: number;
  name: string;
}

/** 歌手曲风，对应 singer_style 表；type 为后端定义的分类编码。 */
export interface SingerStyleItem {
  id: number;
  name: string;
  type: number;
}
