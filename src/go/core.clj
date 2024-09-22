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
  [:div {:style {:css.prop/font-size "1.2rem" :css.prop/color (:theme/unobtrusive-color theme)}}
   [:details
    [:summary [:em {:style {:css.prop/font-variant :css.val/small-caps}} "Future plans?"]]
    [:div {:style {:margin-top "0.5rem"}}
     (for [idea ["add weeknote text field?"
                 "add video roulette?"]]
       [:div {:style {:margin-left "0.8rem"}} "↝ " idea])]]])

(defn view-links [theme]
  [:div {:style {:css.prop/font-size "1.2rem"
                 :css.prop/margin-top "1em"
                 :css.prop/color (:theme/unobtrusive-color theme)}}
   [:a {:href path/play-teod-eu
        :style {:css.prop/color (:theme/unobtrusive-color theme)}}
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
  [:div [:span {:style {:css.prop/color (:theme/emphasis-color theme )}}
         (str/upper-case (:principle/core principle))]
   " " (:principle/extras principle)])

(defn req->title [req]
  (cond (= "localhost" (:server-name req))
        "🩵 local"

        :else
        "🌊 🌊 🌊"))

(defn principles-page
  [req]
  (let [theme theme-blumoon]
    (assert (valid-theme? theme))
    (framework/page
     {:title (req->title req) :theme theme}
     [:section {:style {:css.prop/height "100%"
                        :css.prop/display :css.val/flex
                        :css.prop/flex-direction :css.val/column
                        :css.prop/gap "2rem"
                        :css.prop/justify-content :css.val/center
                        :css.prop/line-height "100%"
                        :css.prop/color (:theme/primary-color theme)}}
      (map (partial view-principle theme) principles)
      (view-links theme)
      (view-future-plans theme)])))

(def routes
  [[path/index #'principles-page]])

(defn start! [opts]
  (framework/start! #'routes opts))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; COMMENTARY AND EXPLORATION

(comment
  (-> @framework/last-request
      (dissoc :reitit.core/match)
      (dissoc :reitit.core/router))

  ,)
