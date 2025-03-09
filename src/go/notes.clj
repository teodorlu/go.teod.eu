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
     "that's where I want to go."]]
   ["Short feedback loops considered essential"
    ["When tweaking designs towards perfection, a long feedback loop is death."
     "I feel the deadness approaching as I write this. I evaluate, alt+tab and refresh. It's too slow."
     "Is Portfolio the tool I want to use? 🤔"]]])

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
   [:body {:style {:margin 0}}
    [:div {:style {:margin "2px" :border "1px solid #00EAFF"}}
     [:div {:style {:margin "2px" :border "1px solid #FF9E37"}}
      [:div {:style {:margin "2px" :border "1px solid #D7FF0F"}}
       [:div {:style {:margin "2px" :border "1px solid #0FFF6F"}}
        [:div {:style {:margin "2px" :border "1px solid #E8CD00"}}
         [:div {:style {:margin "2px" :border "px solid #00EAFF"}}
          [:pre {:style {:margin 0}}
           (->>
            (for [[head more] notes]
              (list [:strong head] "\n"
                    (when more
                      (for [line more]
                        (str "  " line "\n")))))
            (interpose "\n"))]]]]]]]]))
