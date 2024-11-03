(ns go.path)

(def index "/")
(def add-weeknote "/add-weeknote")
(def add-weeknote-prompt "/add-weeknote-prompt")
(def view-weeknotes (when-let [code (System/getenv "WEEKNOTE_ACCESSCODE")] (str "/" code)))
(def play-teod-eu "https://play.teod.eu")
