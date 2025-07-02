(ns go.explore.watchers)

(require '[nextjournal.beholder :as beholder])

(defonce events (atom []))

(defn callback [event]
  (swap! events conj event))

(defonce watcher (beholder/watch #'callback "resources"))

(spit "resources/stuff.txt" (rand-int 100) :append true)
