(ns go.refs
  "Start collecting my own references. For now, just link. In the future, consider
  storing PDFs locally.")

(def motivation
  ["We learn collectively by briding knowledge."
   "We bridge knowledge with explicit references."
   "Therefore, I refer to my sources."])

(def my-refs
  #{{:title "Designerly ways of knowing"
     :author "Nigel Cross"
     :year "1982"
     :source "https://oro.open.ac.uk/39253/8/Designerly%20Ways%20of%20Knowing%20DS.pdf"
     :novelty "Explains what design IS"}
    {:title "The niche design zine [Public draft]"
     :author "Itay Dreyfus"
     :year "2025"
     :source "https://docs.google.com/document/d/1Qjoy-JYwS6GKXaa7O828lT5kwfycDr9RHKrDCG2dXuk/edit?tab=t.0"
     :novelty "Makes a case for caring about and experimenting with design"}
    {:title "Creative Computation"
     :author "Jack Rusher"
     :year "2019"
     :source "https://www.youtube.com/watch?v=TeXCvh5X5w0"
     :novelty "An argument that design and engineering are and should be closely tied together"}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRESENTATION

(require '[go.rounded :as rounded])

(defn motivation->content [motivation]
  [::rounded/vbox
   (into [::rounded/text
          [::rounded/core "Why?"]]
         motivation)])

(defn refs->content [refs]
  (into [::rounded/vbox]
        (for [{:keys [title author year source novelty]} (sort-by :title refs)]
          [::rounded/text [::rounded/link source title]
           (str "(" year ", " author ")")
           novelty])))

(comment
  [(refs->content my-refs)
   motivation]
  )

(require '[go.path :as path])

(defn navitation->content [navigation]
  (into [::rounded/hbox]
        (for [{:keys [root path text]} navigation]
          [::rounded/text [::rounded/link (str root path) text]])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HTTP HANDLER

(require 'hiccup.page)

(def ENGINEERING-FOR-VARIATION
  (rounded/rounded
   [::rounded/hbox
    [::rounded/text
     [::rounded/core "ENGINEERING FOR VARIATION"]
     "Space for variation is a design constraint."
     "Science 🌀 Art"
     "Engineering 🌀 Design"]]))

(defn view [_]
  (hiccup.page/html5
      {:lang "en" :style {:min-height "100%"
                          :color "white"
                          :background-color "black"}}
      [:head
       [:title "exploring taste 🌀 engineering art"]
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
      [:body {:style {:margin 0 :padding "15px"}}
       ENGINEERING-FOR-VARIATION
       [:div {:style {:height "15px"}}]
       (rounded/rounded (motivation->content motivation))
       [:div {:style {:height "15px"}}]
       (rounded/rounded (refs->content my-refs))
       [:div {:style {:height "15px"}}]
       (rounded/rounded (navitation->content path/navigation))
       ]))
