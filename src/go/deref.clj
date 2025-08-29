(ns go.deref)

(require '[clojure.data.xml :as xml])
(require '[babashka.http-client :as http])
(require '[clojure.string :as str])

(defn fetch-feed []
  (-> "https://clojure.org/feed.xml"
      http/get
      :body
      xml/parse-str))

(def feed (fetch-feed))

(defn feed->entries [feed]
  (->> feed
       :content
       (mapcat :content)))

(defn newsletter-entry? [element]
  (->> element
       :content
       (mapcat :content)
       (some #(str/starts-with? % "Clojure Deref ("))))

(def aug28
  (->> feed
       :content
       (mapcat :content)
       (filter newsletter-entry?)
       first))

(defn string-attr [entry tag]
  (some->> entry
           :content
           (filter (comp #{tag} :tag))
           first
           :content
           str/join))

(defn entry->title [entry] (string-attr entry :title))
(entry->title aug28)

(defn entry->description [entry] (string-attr entry :description))
(entry->description aug28)
