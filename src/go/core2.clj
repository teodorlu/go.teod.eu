(ns go.core2)

;; a second take at core

;; this time, separate design from content.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; content

(def principles
  [::box
   [::text [::core "Balance."] "Body ↔ Mind ↔ Emotions."]
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

(require '[go.path :as path])

(def linkroll
  (into [::box]
        (for [{:keys [root path text]} path/navigation]
          [::link (str root path) text])))

(require '[clojure.walk :refer [postwalk]])
(require '[go.rounded :as rounded])

(defn postwalk-some [f form] (postwalk #(or (f %) %) form))

(def rebase-to-rounded-1
  {::box ::rounded/hbox
   ::text ::rounded/text
   ::core ::rounded/core
   ::link ::rounded/link})

(defn rebase-to-rounded [content]
  (postwalk-some rebase-to-rounded-1 content))

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
(def link? (el-pred ::link))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; designs

(defn rounded [content]
  [:div {:style {:height "100%"
                 ;; :margin "10px"
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
               (interpose "\n\n" (rest el)))

         (link? el)
         [:a {:style {:color "white"} :href (nth el 1)} (nth el 2)]

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
   [:body {:style {:margin 0 :padding "15px"}}
    (rounded notes)
    [:div {:style {:height "15px"}}]
    (rounded principles)
    [:div {:style {:height "15px"}}]
    (rounded/rounded-legacy (rebase-to-rounded linkroll))]))
