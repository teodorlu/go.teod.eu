(ns go.bretroulette
  (:require
   [hickory.core :as hickory]
   [lookup.core :as lookup]))

;; https://worrydream.com/refs/ is a treasure trove!

(defn find-reference-elements [hiccup]
  (->> hiccup
       (lookup/select 'a)
       ;; .lunchbox links are navigation, not references
       (remove (set (lookup/select '[.lunchbox a] hiccup)))))

(defn refs-url->references [url]
  (->> url slurp hickory/parse hickory/as-hiccup
       find-reference-elements
       (map (fn [ref-hiccup]
              {:text (lookup/text ref-hiccup)
               :uri (str url (:href (lookup/attrs ref-hiccup)))}))))

(def refs-url "https://worrydream.com/refs/")

(def references (future (refs-url->references refs-url)))

(defn random-ref []
  (rand-nth @references))

(comment
  (random-ref)
  (def hiccup (hickory/as-hiccup (hickory/parse (slurp refs-url))))
  (find-reference-elements hiccup)

  )
