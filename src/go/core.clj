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
           :theme/unobtrusive
           :theme/emphasis]))

(def theme-main
  {:theme/primary-color bright-green
   :theme/secondary-color black
   :theme/unobtrusive greyish
   :theme/emphasis bright-blue})

(def section-style-left-adjust
  "Left adjust text in sections"
  {})

(defn principles-page
  ([title theme]
   (principles-page title theme {}))
  ([title theme opts]
   (assert (valid-theme? theme))
   [:html {:lang "en" :style {:height "100%"}}
    [:head
     [:title title]
     [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]
     [:meta {:charset "utf-8"}]]
    [:body {:style {:height "100%" :margin 0
                    :font-size "1.8rem"
                    :padding-left "1rem"
                    :padding-right "1rem"
                    :background-color (:theme/secondary-color theme)
                    :font-family "serif"}}
     [:section {:style (merge {:height "100%"
                               :display :flex
                               :flex-direction :column
                               :gap "2rem"
                               :justify-content :center
                               :line-height "100%"
                               :color (:theme/primary-color theme)}
                              (:section-style/overrides opts section-style-left-adjust))}
      (for [[principle-core principle-extras]
            (partition 2 ["Balance." "Body ↔ Mind ↔ Emotions."
                          "Habits for action" "get you started."
                          "Creation & curiosity" "over consumption & passivity."
                          ;; "Techne ≠ episteme." "Not the same thing."
                          "Rest or focus?" (str "Search for balance."
                                                " Body ↔ Mind ↔ Emotions.")])]
        [:div [:span {:style {:color (:theme/emphasis theme )}}
               (str/upper-case principle-core)]
         " " principle-extras])
      [:div {:style {:font-size "1.2rem"
                     :margin-top "1em"
                     :color (:theme/unobtrusive theme)}}
       [:a {:href path/play-teod-eu
            :style {:color (:theme/unobtrusive theme)}}
        "play.teod.eu"]]

      [:div {:style {:font-size "1.2rem" :color (:theme/unobtrusive theme)}}
       [:details
        [:summary [:em "TODO "]]
        (into [:ul] (map #(vector :li %)
                         ["Consider adding weeknote text field"
                          "Consider adding an interesting video roulette"]))]]]]]))

(defn page
  ([req theme] (page req theme {}))
  ([req theme opts]
   (let [title (str (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊"))]
     (principles-page title theme opts))))

(defn page-index [req]
  (page req theme-main))

(def routes
  [[path/index #'page-index]])

(defn start! [opts]
  (framework/start! #'routes opts))
