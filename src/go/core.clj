(ns go.core
  (:require
   [org.httpkit.server :as httpkit]))

(defn handler [_req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "It runs!"})

(defonce server (atom nil))

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
