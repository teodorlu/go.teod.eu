(ns go.datastar
  (:require [babashka.http-client :refer [patch request]]))

(defn fetch [url]
  (-> (patch url {:throw false})
      (select-keys [:status])))

(def res (atom nil))

(comment
  @res
  (fetch "https://data-star.dev/examples/templ_counter/global")

  (future
    (reset! res
            (doall (->>
                    (repeat 160000 "https://data-star.dev/examples/templ_counter/global")
                    (pmap fetch)
                    (map :status)
                    frequencies
                    ))))

  ;; 4344
  ;; 5144

  ;; 790 204s, 10 500s.

  ;; 144

  )
