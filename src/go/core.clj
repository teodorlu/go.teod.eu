(ns go.core
  (:require
   [clojure.string :as str]
   [go.path :as path]
   [go.framework :as framework]))

(set! *print-namespace-maps* false)

(def bright-green "hsl(124, 100%, 88%)")
(def black "rgba(0,0,0,1.00)")
(def greyish "hsl(108, 5%, 40%);")
(def bright-blue "rgb(109 219 253)")

(defn valid-theme? [theme]
  (every? #(contains? theme %)
          [:theme/primary-color
           :theme/secondary-color
           :theme/unobtrusive-color
           :theme/emphasis-color]))

(def theme-blumoon
  {:theme/primary-color bright-green
   :theme/secondary-color black
   :theme/unobtrusive-color greyish
   :theme/emphasis-color bright-blue})

(defn view-future-plans [theme]
  [:div {:style {:font-size "1.2rem" :color (:theme/unobtrusive-color theme)}}
   [:details
    [:summary [:em {:style {:css/font-variant :css/small-caps}} "Future plans?"]]
    [:div
     (for [idea ["add weeknote text field?"
                 "add video roulette?"]]
       [:div {:style {:margin-left "0.5rem"}} idea])]]])

(defn view-links [theme]
  [:div {:style {:font-size "1.2rem"
                 :margin-top "1em"
                 :color (:theme/unobtrusive-color theme)}}
   [:a {:href path/play-teod-eu
        :style {:color (:theme/unobtrusive-color theme)}}
    "play.teod.eu"]])

(def principles
  (->> ["Balance." "Body ↔ Mind ↔ Emotions."
        "Habits for action" "get you started."
        "Creation & curiosity" "over consumption & passivity."
        ;; "Techne ≠ episteme." "Not the same thing."
        "Rest or focus?" (str "Search for balance."
                              " Body ↔ Mind ↔ Emotions.")]
       (partition 2)
       (map (fn [[principle-core principle-extras]]
              {:principle/core principle-core
               :principle/extras principle-extras}))))

(defn view-principle [theme principle]
  [:div [:span {:style {:color (:theme/emphasis-color theme )}}
         (str/upper-case (:principle/core principle))]
   " " (:principle/extras principle)])

(defn principles-page
  [title theme]
  (assert (valid-theme? theme))
  (framework/page
   {:title title :theme theme}
   [:section {:style {:height "100%"
                      :display :flex
                      :flex-direction :column
                      :gap "2rem"
                      :justify-content :center
                      :line-height "100%"
                      :color (:theme/primary-color theme)}}
    (map (partial view-principle theme) principles)
    (view-links theme)
    (view-future-plans theme)]))

(defn page2 [req]
  (let [title (str (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊"))]
    (principles-page title theme-blumoon)))

(def routes
  [[path/index #'page2]])

(defn start! [opts]
  (framework/start! #'routes opts))
