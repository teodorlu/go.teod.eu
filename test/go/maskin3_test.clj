(ns go.maskin3-test
  (:require [clojure.test :refer [deftest is testing]]
            [go.maskin3 :as m]))

#_(set! *print-namespace-maps* false)

(deftest går
  (testing "ett steg av gangen"
    (is (= 2
           (let [maskin (m/ny inc #{4})]
             (->> 0
                  (m/gå maskin)
                  (m/gå maskin)))))))

(deftest kjører
  (testing "til den er ferdig"
    (is (= 4
           (-> (m/ny inc #{4})
               (m/kjør 0)
               (m/vent))))))

(deftest avbryter
  (testing "når det bes om"
    (is (<= 1
            (let [treig-pluss (fn [x] (Thread/sleep 5) (inc x))
                  maskin (m/ny treig-pluss #{10})
                  jobb (m/kjør maskin 0)]
              (Thread/sleep 5)
              (m/stopp! jobb)
              (m/vent jobb))
            9))))

(deftest rapporterer
  (testing "når den gjør noe"
    (is (= [0 1 2 3 4 5 6 7 8 9 10]
           (let [!logg (atom [])
                 maskin (m/ny inc #{10} (partial swap! !logg conj))
                 jobb (m/kjør maskin 0)]
             (m/vent jobb)
             @!logg)))))
