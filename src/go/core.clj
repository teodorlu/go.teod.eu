(ns go.core
  (:require
   [clojure.string :as str]
   [hiccup2.core :as hiccup]
   [org.httpkit.server :as httpkit]
   [go.path :as path]))

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
     [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]]
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

(defn page-index      [req] (page req theme-main))

(defn icon-web [_]
  {:status 200 :body "icon web"})

(def routes
  {path/icon-web #'icon-web
   path/index #'page-index})

(defn render [content]
  (cond
    ;; Vectors are Hiccup HTML
    (vector? content)
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>") content))}

    ;; Maps are ring responses
    (map? content)
    map))

(defn not-found [description]
  {:status 404
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>")
                [:html [:body "Page not found: " description]]))})

(defn root-handler [req]
  (when (not= "/clerk_service_worker.js" (:uri req))
    (tap> req))
  (or (when-let [handler (get routes (:uri req))]
        (-> req handler render))
      (not-found (:uri req))))

(defonce server (atom nil))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn serve! [_]
  (swap! server
         (fn [old-server]
           (when old-server
             (httpkit/server-stop! old-server))
           (httpkit/run-server #'root-handler
                               {:port 7777
                                :legacy-return-value? false}))))

#_ (deref server)
#_ (httpkit/server-status @server)
#_ (serve! {})
#_ (httpkit/server-stop! @server)
