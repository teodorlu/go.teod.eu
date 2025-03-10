(ns go.core2)

;; a second take at core

;; this time, separate design from content.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; content

(def principles
  [[::text [::core "Balance."] "Body ↔ Mind ↔ Emotions."]
   [::text [::core "Habits for action"] "get you started."]
   [::text [::core "Creation & curiosity"] "over consumption & passivity."]
   [::text [::core "Rest or focus?"] "Strive for balance. Body ↔ Mind ↔ Emotions."]])

(def notes
  [::box
   [::text
    [::core "I want to switch between different designs"]
    "Just read [Public draft] The niche design zine"
    "original: https://docs.google.com/document/d/1Qjoy-JYwS6GKXaa7O828lT5kwfycDr9RHKrDCG2dXuk/edit?tab=t.0"
    "copy, PDF: https://nextcloud.teod.eu/f/163606"
    "Why not throw it around? Try something different?"]
   [::text [::core "Portfolio on Mikrobloggeriet?"]]
   [::text
    [::core "Different designs on go.teod.eu?"]
    "and not just different styles, different _designs_."
    "where portfolio shows completely different variations."
    "that's where I want to go."]
   [::text
    [::core "Short feedback loops considered essential"]
    "When tweaking designs towards perfection, a long feedback loop is death."
    "I feel the deadness approaching as I write this. I evaluate, alt+tab and refresh. It's too slow."
    "Is Portfolio the tool I want to use? 🤔"]])

(def linkroll
  [[::link "https://play.teod.eu" "play.teod.eu"]
   [::link "/" "go.teod.eu"]
   [::link "/bretroulette" "Bret Roulette"]
   [::link "/notes" "Notes"]
   [::link "/flexing" "Flexing"]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; design helpers

(require 'hiccup.page)
(require '[clojure.walk :refer [postwalk]])

(defn el-pred [tag]
  (fn [el] (and (vector? el)
                (= tag (first el)))))
(def core? (el-pred ::core))
(def text? (el-pred ::text))
(def box? (el-pred ::box))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; designs

(defn rounded [content]
  [:div {:style {:height "100%"
                 :margin "10px"
                 :border "1px solid #00EAFF"
                 :border-radius "10px"
                 :padding "5px 7px 5px 7px"}}
   [:pre {:style {:margin 0}}
    (postwalk
     (fn [el]
       (cond
         (core? el)
         (into [:strong]
               (rest el))

         (text? el)
         (interpose "\n"
                    (cons (second el)
                          (map #(str "  " %) (rest (rest el)))))

         (box? el)
         (into [:pre {:style {:margin 0}}]
               (rest el))

         :else el))
     content)]])

#_(rounded notes)

(comment
  ;; would be cool to take the core design (blugreen)
  ;; but it's more complex, maybe later.
  (defn blugreen [content])
  )

(defn view [_]
  (hiccup.page/html5
   {:lang "en" :style {:min-height "100%"
                       :color "white"
                       :background-color "black"}}
   [:head
    [:title "exploring taste"]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body {:style {:margin 0}}
    (rounded notes)]))
