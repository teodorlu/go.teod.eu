(ns go.core
  (:require
   [org.httpkit.server :as httpkit]
   [hiccup2.core :as hiccup]))

(hiccup/html [:div "It runs!"])

(def page
  [:html {:lang "en"}
   [:head [:title "go.teod.eu"]]
   [:body
    [:h1 "Go"]
    [:div "It runs."]]])

(defn handler [_req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (hiccup/html
                  (hiccup/raw "<!DOCTYPE html>")
                page))})

(defn serve! [_]
  (swap! server
         (fn [old-server]
           (when old-server
             (httpkit/server-stop! old-server))
           (httpkit/run-server #'handler
                               {:port 7777
                                :legacy-return-value? false}))))

#_ (deref server)
#_ (httpkit/server-status @server)
#_ (serve! {})
#_ (httpkit/server-stop! @server)
