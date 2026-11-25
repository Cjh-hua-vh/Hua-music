import { defineStore } from "pinia";
import { useSongStore } from "@/store/song";
import { getSingerName, getSongTitle } from "@/utils";

type SongId = string | number;

const LAST_PLAYED_ID_KEY = "lastPlayedId";
const LAST_PLAYED_SONG_KEY = "lastPlayedSong";

export interface PlaySongItem {
  id: SongId;
  url: string;
  pic: string;
  lyric: unknown;
  name: string;
}

export interface PlaySongPayload {
  id: SongId;
  url: string;
  pic: string;
  lyric: unknown;
  index: number;
  name: string;
  currentSongList: PlaySongItem[];
}

export const usePlayerStore = defineStore("player", {
  state: () => ({
    lastPlayedId: (localStorage.getItem(LAST_PLAYED_ID_KEY) || "") as SongId,
    lastPlayedSong: JSON.parse(localStorage.getItem(LAST_PLAYED_SONG_KEY) || "null") as PlaySongPayload | null,
  }),

  actions: {
    setLastPlayedId(id: SongId) {
      this.lastPlayedId = id;
      localStorage.setItem(LAST_PLAYED_ID_KEY, String(id));
    },

    playSong(payload: PlaySongPayload) {
      const { id, url, pic, lyric, index, name, currentSongList } = payload;

      const songStore = useSongStore();

      this.setLastPlayedId(id);
      this.lastPlayedSong = payload;
      localStorage.setItem(LAST_PLAYED_SONG_KEY, JSON.stringify(payload));

      songStore.playMusic({
        id,
        url,
        pic,
        index,
        songTitle: getSongTitle(name),
        singerName: getSingerName(name),
        lyric,
        currentSongList,
      });
    },

    restoreLastPlayed() {
      if (this.lastPlayedSong) {
        const songStore = useSongStore();
        const { id, url, pic, lyric, index, name, currentSongList } = this.lastPlayedSong;
        songStore.setSongId(id);
        songStore.setSongUrl(url);
        songStore.setSongPic(pic);
        songStore.setCurrentPlayIndex(index);
        songStore.setSongTitle(getSongTitle(name));
        songStore.setSingerName(getSingerName(name));
        songStore.setLyric(lyric);
        songStore.setCurrentPlayList(currentSongList);
        songStore.setIsPlay(false);
      }
    },
  },
});
