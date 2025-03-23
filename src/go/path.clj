(ns go.path)

;; paths
(def index "/")
(def add-weeknote "/add-weeknote")
(def add-weeknote-prompt "/add-weeknote-prompt")
(def view-weeknotes (when-let [code (System/getenv "WEEKNOTE_ACCESSCODE")] (str "/" code)))
(def play-teod-eu "https://play.teod.eu")
(def bretroulette "/bretroulette")
(def notes "/notes")
(def flexing "/flexing")
(def c2 "/c2")
(def refs "/refs")

;; roots
(def tplay "https://playt.teod.eu")
(def tgo "")

;; navigation
(def navigation
  [{:root tplay :path "/" :text "play.teod.eu"}
   {:root tgo :path "/" :text "go.teod.eu"}

   {:root tgo :path "/" :text "go.teod.eu"}
   {:root tgo :path "/bretroulette" :text "Bret Roulette"}
   {:root tgo :path refs :text "references"}
   {:root tgo :path "/notes" :text "Notes"}
   {:root tgo :path "/flexing" :text "Flexing"}
   {:root tgo :path c2 :text "2"}])
