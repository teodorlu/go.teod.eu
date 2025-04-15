(ns go.refs2)

(require '[go.rounded :as rounded]
         'hiccup.page
         '[go.path :as path])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CONTENT

(def why
  {:title "Why manage references?"
   :lines
   ["Because it forms you."
    "To change yourself, change what influences you."
    "Managing your references is taking resposibility for your influences."]})

(def refs-design-and-quality
  {:title "Design and quality"
   :subtitle "On how to make things, what makes things good, and how to make good things."
   :refs
   #{{:text/title "Creative Computation"
      :text/author "Jack Rusher"
      :text/published "2019"
      :text/source "https://www.youtube.com/watch?v=TeXCvh5X5w0"
      :novelty "An argument that design and engineering are and should be closely connected"}
     {:text/title "Designerly ways of knowing"
      :text/author "Nigel Cross"
      :text/published "1982"
      :text/source "https://oro.open.ac.uk/39253/8/Designerly%20Ways%20of%20Knowing%20DS.pdf"
      :novelty "Explains what design IS"}
     {:text/title "Taste"
      :text/author "Peter Seibel"
      :text/published "2015"
      :text/source "https://gigamonkeys.com/taste/"}
     {:text/title "The niche design zine [Public draft]"
      :text/author "Itay Dreyfus"
      :text/published "2025"
      :text/source "https://docs.google.com/document/d/1Qjoy-JYwS6GKXaa7O828lT5kwfycDr9RHKrDCG2dXuk/edit?tab=t.0"
      :novelty "Makes a case for caring about and experimenting with design"}
     }})

(def refs-clojure-europe-easter-2025
  {:title "Clojure Europe Easter 2025 reading recommendations"
   :refs
   #{{:text/title "The Illiad"
      :text/author "Homer"
      :text/published "8th century BC"
      :text/comments
      #{{:ref/by "Matthias"
         :ref/commentary "If you can find good modern translations, and can deal with a story written in verse, they are great."}}}
     {:text/title "The Odyssey"
      :text/author "Homer"
      :text/published "8th century BC"}}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LOGIC

(defn refs->content [{:keys [title subtitle refs]}]
  [::rounded/bordered
   (into [::rounded/vbox
          [::rounded/text
           [::rounded/core title]
           subtitle]]
         (for [{:as text :keys [novelty]} (sort-by :text/title refs)]
           [::rounded/text [::rounded/link (:text/source text) (:text/title text)]
            (str "(" (:text/published text) ", " (:text/author text) ")")
            novelty]))])

(defn navitation->content [navigation]
  [::rounded/bordered
   (into [::rounded/hbox]
         (for [{:keys [root path text]} navigation]
           [::rounded/text [::rounded/link (str root path) text]]))])

(defn title+lines->content [{:keys [title lines]}]
  [::rounded/bordered
   [::rounded/vbox
    (into [::rounded/text
           [::rounded/core title]]
          lines)]])

(defn view [_]
  (hiccup.page/html5
   {:lang "en" :style {:min-height "100%"
                       :color "white"
                       :background-color "black"}}
   [:head
    [:title "exploring taste 🌀 engineering art"]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   (rounded/rounded
    [:body {:style {:margin 0 :padding "15px"}}
     (interpose
      [:div {:style {:height "15px"}}]
      [(title+lines->content why)
       (refs->content refs-design-and-quality)
       (navitation->content path/navigation)])])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FOOTNOTES

(comment
  ;; Challenge:
  ;;
  ;; - define a data format that works for different kinds of references
  ;; - present the different kinds of references neatly.

  ;; Ref example: paper, with novelty and source
  {:title "Designerly ways of knowing"
   :author "Nigel Cross"
   :text/published "1982"
   :source "https://oro.open.ac.uk/39253/8/Designerly%20Ways%20of%20Knowing%20DS.pdf"
   :novelty "Explains what design IS"}

  ;; Ref example: history book, published year and recommended by
  {:title "Guns of August"
   :text/published 1962
   :author "Barbara W. Tuchman"
   :type "book"
   :genre "history"
   :recommended-by "Mario Trost and Jack Rusher"}

  ;; (I find the data modeling part of this job to be challenging!)
  )
