(ns go.core
  (:require
   [org.httpkit.server :as httpkit]
   [hiccup2.core :as hiccup]))

(defn principle [title details]
  [:p [:strong title] (when details (list  " " details))])

(defn page [_]
  [:html {:lang "en"}
   [:head
    [:title "go!"]
    [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]]
   [:body
    (principle "Balance." "Body ↔ Mind ↔ Emotions.")
    (principle "Habits for action" "get you started.")
    (principle "Creation & curiosity" "over consumption & passivity.")
    (principle "Techne ≠ episteme." "Not the same thing.")
    (principle "Rest or focus?" "Search for a balance.")]])

(defn other [_]
  [:html {:lang "en"
          :style {:height "100%"}}
   [:head
    [:title "🌊"]
    [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]]
   [:body {:style {:height "100%" :margin 0}}
    [:section {:style {:height "100%"
                       :display :flex
                       :flex-direction :column
                       :gap "1em"
                       :align-items :center
                       :justify-content :center}}
     [:div "PRINCIPLE"]
     [:div "ANOTHER PRINCIPLE"]]]])

(def routes
  {"/" #'page
   "/other" #'other})

(defn root-handler [req]
  (when (not= "/clerk_service_worker.js" (:uri req))
    (tap> req))
  (if-let [handler  (get routes (:uri req))]
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
