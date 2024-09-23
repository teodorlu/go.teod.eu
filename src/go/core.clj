(ns go.core
  (:require
   [clojure.string :as str]
   [go.framework :as framework]
   [go.path :as path]
   [hiccup2.core :as hiccup2])
  (:import
   [java.time Instant]))

(comment
  (reset! framework/state {})
  @framework/state
  ,)

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
       [:div {:style {:css.prop/margin-left "0.8rem"
                      :css.prop/color (:theme/primary-color theme)}}
        "↝ " idea])]]])

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
        "Rest or focus?" (str "Strive for balance."
                              " Body ↔ Mind ↔ Emotions.")]
       (partition 2)
       (map (fn [[principle-core principle-extras]]
              {:principle/core principle-core
               :principle/extras principle-extras}))))

(defn view-principle [theme principle]
  [:div
    {:style {:css.prop/font-size "1.8rem"}}
   [:span {:style {:css.prop/color (:theme/emphasis-color theme )}}
    (str/upper-case (:principle/core principle))]
   " "
   [:span {:style {:css.prop/color (:theme/primary-color theme)}}
    (:principle/extras principle)]])

(defn req->title [req]
  (cond (= "localhost" (:server-name req))
        "🩵 local"

        :else
        "🌊 🌊 🌊"))

(defn add-weeknote-component [_req]
  (let [theme theme-blumoon]
    {:status 200
     :body
     (str
      (hiccup2/html
          [:form {:id :id/weeknote-editor}
           (identity [:div {:style {:css.prop/color (:theme/primary-color theme)}}
                      "Write your weeknote:"])
           (identity
            [:textarea {:style {:css.prop/width "100%"
                                :css.prop/resize "vertical"
                                :css.prop/height "7rem"
                                :css.prop/border "1px solid white"
                                :css.prop/color (:theme/emphasis-color theme)
                                :css.prop/background-color (:theme/secondary-color theme)}
                        :name "weeknote"
                        :placeholder "text …"}])
           [:button {:hx-post path/add-weeknote
                     :hx-target (str "#" (name :id/weeknote-editor))
                     :hx-swap :htmx/outerHTML}
            "Save"]]))}))

(comment

  @framework/last-request
  :form-params {"weeknote" "Hei :)"},
  ,)

(defn add-weeknote [req]
  (let [theme theme-blumoon]
    (when-let [weeknote (get-in req [:params "weeknote"])]
      (swap! framework/state update :weeknotes (fnil conj [])
             {:text weeknote
              :uuid (random-uuid)
              :timestamp (str (Instant/now))})
      {:status 200
       :body
       (str
        (hiccup2/html
            [:div {:id :id/weeknote-editor}
             (identity
              [:div {:style {:css.prop/color (:theme/primary-color theme)}}
               "Weeknote added!"])
             [:a {:hx-get path/add-weeknote
                  :hx-target (str "#" (name :id/weeknote-editor))
                  :hx-swap :htmx/outerHTML
                  :style {:css.prop/color (:theme/primary-color theme)
                          :css.prop/text-decoration :css.val/underline}}
              "If you wish, add another."]]))})))

(defn add-weeknote-button [theme]
  [:div {:id :id/weeknote-editor}
   [:a {:hx-get path/add-weeknote
        :hx-target (str "#" (name :id/weeknote-editor))
        :hx-swap :htmx/outerHTML
        :style {:css.prop/color (:theme/primary-color theme)
                :css.prop/text-decoration :css.val/underline}}
    "Add weeknote"]])

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
                        :css.prop/line-height "2rem"
                        }}
      (map (partial view-principle theme) principles)
      (view-links theme)
      (view-future-plans theme)
      (when (= "localhost" (:server-name req))
        (add-weeknote-button theme))])))

(def routes
  [[path/index #'principles-page]
   [path/add-weeknote {:get #'add-weeknote-component
                       :post #'add-weeknote}]])

(defn start! [opts]
  (framework/start! #'routes opts))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; COMMENTARY AND EXPLORATION

(comment
  @framework/last-request
  ,)
