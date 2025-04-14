(ns go.refs
  "Start collecting my own references. For now, just link. In the future, consider
  storing PDFs locally.")

(def my-refs
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
    })

(def the-why
  {:title "Why references?"
   :lines
   ["We learn collectively by briding knowledge."
    "We bridge knowledge with explicit references."
    "Therefore, I refer to my sources."]})

(def technology-leadership
  {:title "Technology leadership ..."
   :lines
   ["→ sets a strategic vision."
    "→ scopes tactical work."
    "→ sets the bar for quality."]})

(def the-how
  {:title "References - How?"
   :lines
   ["objective → reify a trust network of references."
    "near goal 1 → promote Christopher Alexander's *liveness*"
    "quality → so that its pieces are beautify, and working the pieces is a joy."]})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRESENTATION

(require '[go.rounded :as rounded])

(defn title+lines->content [{:keys [title lines]}]
  [::rounded/bordered
   [::rounded/vbox
    (into [::rounded/text
           [::rounded/core title]]
          lines)]])

(defn refs->content [refs]
  [::rounded/bordered
   (into [::rounded/vbox]
         (for [{:keys [title author year source novelty]} (sort-by :title refs)]
           [::rounded/text [::rounded/link source title]
            (str "(" year ", " author ")")
            novelty]))])

(require '[go.path :as path])

(defn navitation->content [navigation]
  [::rounded/bordered
   (into [::rounded/hbox]
         (for [{:keys [root path text]} navigation]
           [::rounded/text [::rounded/link (str root path) text]]))])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HTTP HANDLER

(require 'hiccup.page)

(def ENGINEERING-FOR-VARIATION
  [::rounded/bordered
   [::rounded/hbox
    [::rounded/text
     [::rounded/core "ENGINEERING FOR VARIATION"]
     "Space for variation is a design constraint."
     "Science 🌀 Art"
     "Engineering 🌀 Design"]]])

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
      [ENGINEERING-FOR-VARIATION
       (interpose
        [:div {:style {:height "15px"}}]
        (map title+lines->content
             [the-why
              technology-leadership
              the-how]))
       (refs->content my-refs)
       (navitation->content path/navigation)])])))
