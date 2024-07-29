(ns go.core
  (:require
   [clojure.string :as str]
   [hiccup2.core :as hiccup]
   [org.httpkit.server :as httpkit]
   [go.path :as path]))

(def bright-green "hsl(124, 100%, 88%)")
(def blackish "black")
(def greyish "hsl(108, 5%, 40%);")
(def bright-blue "rgb(109 219 253)")
(def dark-blue "rgb(0, 91, 119)")
(def crimson "hsl(19, 100%, 44%)")

(defn valid-theme? [theme]
  (every? #(contains? theme %)
          [:theme/primary-color
           :theme/secondary-color
           :theme/unobtrusive
           :theme/emphasis]))

(def theme-1 {:theme/primary-color bright-green
              :theme/secondary-color blackish
              :theme/unobtrusive greyish
              :theme/emphasis bright-blue})

(def theme-2 {:theme/primary-color blackish
              :theme/secondary-color bright-green
              :theme/unobtrusive greyish
              :theme/emphasis crimson})

(assert (every? valid-theme? [theme-1 theme-2]))

(defn principles-page [title theme]
  (assert (valid-theme? theme))
  [:html {:lang "en"
          :style {:height "100%"}}
   [:head
    [:title title]
    [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]]
   [:body {:style {:height "100%" :margin 0
                   :font-size "1.8rem"
                   :padding-left "1rem"
                   :padding-right "1rem"
                   :background-color (:theme/secondary-color theme)
                   :font-family "serif"}}
    [:section {:style {:height "100%"
                       :text-align "center"
                       :display :flex
                       :flex-direction :column
                       :gap "2rem"
                       :align-items :center
                       :justify-content :center
                       :color (:theme/primary-color theme)}}
     (for [[principle-core principle-extras]
           (partition 2 ["Balance." "Body ↔ Mind ↔ Emotions."
                         "Habits for action" "get you started."
                         "Creation & curiosity" "over consumption & passivity."
                         "Techne ≠ episteme." "Not the same thing."
                         "Rest or focus?" "Search for a balance between body, mind and emotions."])]
       [:div [:span {:style {:color (:theme/emphasis theme )}}
              (str/upper-case principle-core)]
        " " principle-extras])
     [:div {:style {:font-size "1.2rem"
                    :color (:theme/unobtrusive theme)}}
      (->> [{:linktext path/page :href path/page}
            {:linktext path/other :href path/other}
            {:linktext "play.teod.eu" :href path/play-teod-eu}]
           (map (fn [{:keys [linktext href]}]
                  [:a {:href href
                       :style {:color (:theme/unobtrusive theme)}}
                   linktext]))
           (interpose " · "))]]]])

(defn page-index [req]
  (principles-page (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊")
                   theme-1))

(defn page-other [req]
  (principles-page (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊")
                   theme-2))

(defn icon-web [_]
  {:status 200 :body "icon web"})

(def routes
  {path/page #'page-index
   path/other #'page-other
   path/icon-web #'icon-web})

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
  (if-let [handler (get routes (:uri req))]
    (let [response (-> req handler render)]
      (if response
        response
        (not-found (:uri req))))
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
