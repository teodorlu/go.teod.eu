(ns go.munit.si-test
  (:require [clojure.test :refer [deftest is]]
            [go.munit.si :as si]))

(deftest tautology (is true))

(comment
  ;; direct
  1
  si/m
  [1 si/m]
  [1 si/m si/m]
  [1 {si/m 2}]

  )

(comment
  ;; symbolic
  ;; TODO
  ;; (si/load-symbolic 'm)
  ;; (si/load-symbolic '[1 m])
  ;; (si/load-symbolic '[1 {m 2}])

  )
