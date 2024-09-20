(ns go.core
  (:require
   [clojure.string :as str]
   [go.path :as path]
   [go.framework :as framework]))

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
    (for [[principle-core principle-extras]
          (partition 2 ["Balance." "Body ↔ Mind ↔ Emotions."
                        "Habits for action" "get you started."
                        "Creation & curiosity" "over consumption & passivity."
                        ;; "Techne ≠ episteme." "Not the same thing."
                        "Rest or focus?" (str "Search for balance."
                                              " Body ↔ Mind ↔ Emotions.")])]
      [:div [:span {:style {:color (:theme/emphasis-color theme )}}
             (str/upper-case principle-core)]
       " " principle-extras])
    [:div {:style {:font-size "1.2rem"
                   :margin-top "1em"
                   :color (:theme/unobtrusive-color theme)}}
     [:a {:href path/play-teod-eu
          :style {:color (:theme/unobtrusive-color theme)}}
      "play.teod.eu"]]

    [:div {:style {:font-size "1.2rem" :color (:theme/unobtrusive-color theme)}}
     [:details
      [:summary [:em "TODO "]]
      (into [:ul] (map #(vector :li %)
                       ["Consider adding weeknote text field"
                        "Consider adding an interesting video roulette"]))]]]))

(defn page2 [req]
  (let [title (str (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊"))]
    (principles-page title theme-blumoon)))

(def routes
  [[path/index #'page2]])

(defn start! [opts]
  (framework/start! #'routes opts))
