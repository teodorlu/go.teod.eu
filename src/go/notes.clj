(ns go.notes)

(def notes
  [["I want to switch between different designs"
    ["Just read [Public draft] The niche design zine"
     "original: https://docs.google.com/document/d/1Qjoy-JYwS6GKXaa7O828lT5kwfycDr9RHKrDCG2dXuk/edit?tab=t.0"
     "copy, PDF: https://nextcloud.teod.eu/f/163606"
     "Why not throw it around? Try something different?"]]
   ["Portfolio on Mikrobloggeriet?"]
   ["Different designs on go.teod.eu?"
    ["and not just different styles, different _designs_."
     "where portfolio shows completely different variations."
     "that's where I want to go."]]])

(require 'hiccup2.core)
(require 'hiccup.page)

(def view
  (hiccup.page/html5
   {:lang "en" :style {:min-height "100%"
                       :color "white"
                       :background-color "black"}}
   [:head
    [:title "notes"]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body
    [:pre
     (for [[head more] notes]
       (list head "\n"
             (when more
               (for [line more]
                 (str "  " line "\n")))))]]))
