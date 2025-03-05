(ns go.bretroulette
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [hickory.core :as hickory]
   [lookup.core :as lookup]))

(s/def ::text string?)
(s/def ::uri string?)

(s/def ::ref (s/keys :req [::text ::uri]))

;; https://worrydream.com/refs/ is a treasure trove!

(defn find-reference-elements [hiccup]
  (->> hiccup
       (lookup/select 'a)
       ;; .lunchbox links are navigation, not references
       (remove (set (lookup/select '[.lunchbox a] hiccup)))))

(s/fdef refs-url->references
  :args (s/cat :url string?)
  :ret ::ref)
(defn refs-url->references [url]
  (->> url slurp hickory/parse hickory/as-hiccup
       find-reference-elements
       (map (fn [ref-hiccup]
              {::text (lookup/text ref-hiccup)
               ::uri (str url (:href (lookup/attrs ref-hiccup)))}))))

(def refs-url "https://worrydream.com/refs/")

(def references (future (refs-url->references refs-url)))

(defn spin []
  (rand-nth @references))

(comment
  (spin)
  (def hiccup (hickory/as-hiccup (hickory/parse (slurp refs-url))))
  (find-reference-elements hiccup)

  )

(defn pdf? [ref] (some-> (::uri ref) (str/ends-with? ".pdf")))

(defn refs->wget-download-command [refs]
  (str "wget -q --input-file=- << EOF"
       "\n"
       (str/join "\n"
                 (map ::uri refs))
       "\n"
       "EOF"))

(comment
  (def pdf-refs (filter pdf? @references))
  (spit "command.txt" (refs->wget-download-command pdf-refs))

  )
