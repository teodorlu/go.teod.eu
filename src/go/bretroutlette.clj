(ns go.bretroutlette
  (:require
   [hickory.core :as hickory]
   [lookup.core :as lookup]))

;; https://worrydream.com/refs/ is a treasure trove!

(def refs-url "https://worrydream.com/refs/")
(def refs (slurp refs-url))
(def refs-hiccup (-> refs hickory/parse hickory/as-hiccup))
(def references
  (->> (first (filter vector? refs-hiccup))
       (lookup/select '[:a])
       (map (fn [ref-hiccup]
              {:text (lookup/text ref-hiccup)
               :uri (str refs-url (:href (lookup/attrs ref-hiccup)))}))))

(defn random-ref []
  (rand-nth references))
