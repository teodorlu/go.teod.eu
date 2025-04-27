(ns go.path)

;; paths
(def add-weeknote "/add-weeknote")
(def add-weeknote-prompt "/add-weeknote-prompt")
(def bretroulette "/bretroulette")
(def core2 "/core2")
(def flexing "/flexing")
(def index "/")
(def notes "/notes")
(def play-teod-eu "https://play.teod.eu")
(def refs "/refs")
(def refs2 "/refs2")
(def view-weeknotes (when-let [code (System/getenv "WEEKNOTE_ACCESSCODE")] (str "/" code)))

;; roots
(def tplay "https://play.teod.eu")
(def tgo "")

;; navigation
(def navigation
  [{:root tplay :path "/" :text "play.teod.eu"}
   {:root tgo :path "/" :text "go.teod.eu"}
   {:root tgo :path "/bretroulette" :text "Bret Roulette"}
   {:root tgo :path refs :text "refs"}
   {:root tgo :path refs2 :text "refs2"}
   {:root tgo :path "/notes" :text "Notes"}
   {:root tgo :path "/flexing" :text "Flexing"}
   {:root tgo :path core2 :text "core2"}])
