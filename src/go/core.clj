(ns go.core
  (:require
   [clojure.string :as str]
   [hiccup2.core :as hiccup]
   [org.httpkit.server :as httpkit]
   [go.path :as path]))

(def bright-green "hsl(124, 100%, 88%)")
(def blackish "black")

(def theme-1 {:theme/primary-color bright-green
              :theme/secondary-color blackish})

(def theme-2 {:theme/primary-color blackish
              :theme/secondary-color bright-green})

(defn principles-page [title theme]
  (assert (:theme/primary-color theme))
  (assert (:theme/secondary-color theme))
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
       [:div (str/upper-case principle-core) " " principle-extras])
     [:div
      [:a {:href path/other
           :style {:color bright-green}}
       path/other]
      " · "
      [:a {:href path/play-teod-eu
           :style {:color bright-green}}
       "play.teod.eu"]]]]])

(defn page-index [req]
  (principles-page (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊")
                   theme-1))

(defn page-other [req]
  (principles-page (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊")
                   theme-1))

(def routes
  {path/page #'page-index
   path/other #'page-other})

(defn root-handler [req]
  (when (not= "/clerk_service_worker.js" (:uri req))
    (tap> req))
  (if-let [handler (get routes (:uri req))]
    (if-let [response (handler req)]
      {:status 200
       :headers {"Content-Type" "text/html; charset=utf-8"}
       :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>") response))}
      {:status 500
       :headers {"Content-Type" "text/html; charset=utf-8"}
       :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>") "server error"))})
    {:status 404
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>")
                  [:html [:body "Page not found: " (:uri req)]]))}))

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
