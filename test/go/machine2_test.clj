(ns go.machine2-test
  (:require [clojure.test :refer [deftest is testing]]
            [go.machine2 :as m]))

#_(set! *print-namespace-maps* false)

(deftest done
  (is (m/done? (m/create 4 inc #{4}))))

(deftest steps
  (is (= 2
         (-> (m/create 0 inc #{4})
             m/step
             m/step
             ::m/state))))

(deftest runs
  (is (= 4
         (-> (m/create 0 inc #{4})
             m/run
             m/state))))

(deftest halts
  (testing "gets further than zero and stops before 10"
    (is (<= 1
            (let [slow-inc (fn [x] (Thread/sleep 5) (inc x))
                  machine (m/create 0 slow-inc #{10})
                  process (future (m/run machine))]
              (Thread/sleep 20)
              (m/halt! machine)
              (m/state @process))
            9))))

(deftest reports
  (is (= [0 1 2 3 4 5 6 7 8 9 10]
         (let [machine (m/create 0 inc #{10})
               !log (atom [])]
           (m/add-reporter machine
                           (partial swap! !log conj))
           (m/run machine)
           @!log))))
