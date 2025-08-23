(ns go.beautiful
  (:require
   [clojure.java.io :as io]))

(defn resource [path]
  (-> path io/resource io/input-stream))

(defn html [path]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body-path path})

(defn css [path]
  {:status 200
   :headers {"Content-Type" "text/css; charset=utf-8"}
   :body-path path})

(def routes
  {"/beautiful/index.html" (html "go/beautiful/index.html")
   "/beautiful/styles.css" (css "go/beautiful/styles.css")})

(def ring-routes
  (->> routes
       (mapv (fn [[path {:keys [status headers body-path]}]]
               [path (fn [_] {:status status
                              :headers headers
                              :body (resource body-path)})]))))
