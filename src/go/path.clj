(ns go.path)

(def index "/")
(def add-weeknote "/add-weeknote")
(def add-weeknote-prompt "/add-weeknote-prompt")
(def view-weeknotes (str "/" (System/getenv "WEEKNOTE_ACCESSCODE")))
(def play-teod-eu "https://play.teod.eu")
