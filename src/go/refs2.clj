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
   #{{:title "Creative Computation"
      :author "Jack Rusher"
      :year "2019"
      :source "https://www.youtube.com/watch?v=TeXCvh5X5w0"
      :novelty "An argument that design and engineering are and should be closely tied together"}
     {:title "Designerly ways of knowing"
      :author "Nigel Cross"
      :year "1982"
      :source "https://oro.open.ac.uk/39253/8/Designerly%20Ways%20of%20Knowing%20DS.pdf"
      :novelty "Explains what design IS"}
     {:title "Taste"
      :author "Peter Seibel"
      :year "2015"
      :source "https://gigamonkeys.com/taste/"}
     {:title "The niche design zine [Public draft]"
      :author "Itay Dreyfus"
      :year "2025"
      :source "https://docs.google.com/document/d/1Qjoy-JYwS6GKXaa7O828lT5kwfycDr9RHKrDCG2dXuk/edit?tab=t.0"
      :novelty "Makes a case for caring about and experimenting with design"}
     }})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; LOGIC

(defn refs->content [{:keys [title subtitle refs]}]
  [::rounded/bordered
   (into [::rounded/vbox
          [::rounded/text
           [::rounded/core title]
           subtitle]]
         (for [{:keys [title author year source novelty]} (sort-by :title refs)]
           [::rounded/text [::rounded/link source title]
            (str "(" year ", " author ")")
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
