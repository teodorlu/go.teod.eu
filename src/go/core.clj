(ns go.core
  (:require
   [org.httpkit.server :as httpkit]
   [hiccup2.core :as hiccup]))

(defn principle [title details]
  [:p [:strong title] (when details (list  " " details))])

(def page
  [:html {:lang "en"}
   [:head [:title "go!"]]
   [:body
    (principle "Balance." "Body ↔ Mind ↔ Emotions.")
    (principle "Habits for action" "get you started.")
    (principle "Creation & curiosity" "over consumption & passivity.")
    (principle "Techne ≠ episteme." "Not the same thing.")
    (principle "Rest or focus?" "Search for a balance.")]])

(defn handler [_req]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>") page))})

(defonce server (atom nil))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
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
