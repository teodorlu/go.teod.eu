(ns go.bretroutlette
  (:require
   [hickory.core :as hickory]
   [lookup.core :as lookup]))

;; https://worrydream.com/refs/ is a treasure trove!

(def refs-url "https://worrydream.com/refs/")

(defn refs-url->references [url]
  (->> (slurp url)
       hickory/parse hickory/as-hiccup
       (filter vector?)
       first
       (lookup/select 'a)
       (map (fn [ref-hiccup]
              {:text (lookup/text ref-hiccup)
               :uri (str url (:href (lookup/attrs ref-hiccup)))}))))

(def references (future (refs-url->references refs-url)))

(defn random-ref []
  (rand-nth @references))

#_(random-ref)
