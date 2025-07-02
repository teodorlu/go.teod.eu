(ns go.linalg-test
  (:require [clojure.test :refer [deftest is]]
            [go.linalg :as linalg]
            [uncomplicate.neanderthal.core :refer [mm]]))

(deftest my-understanding
  (is
   (= (linalg/scalar->matrix 18.0)
      (mm
       linalg/ones-row3
       (mm linalg/k linalg/ones-col3)))))
