(ns go.mork-test
  (:require [clojure.test :refer [deftest is testing]]
            [go.mork :as mork]))

(deftest normalize-list
  (testing "leaves primitives unchanged"
    (is (= "hei"
           (mork/normalize-list "hei"))))
  (testing "joins up lists of one element"
    (is (= "hei"
           (mork/normalize-list (list "hei"))))))

(deftest normalize-para
  (testing "leaves a list of paragraphs as a paragraph"
    (is (= (list [:p "hei"] [:p "du"])
           (mork/normalize-para (list [:p "hei"] [:p "du"])))))

  (testing "unpacks paragraphs of a single element"
    (is (= "hei"
           (mork/normalize-para [:p "hei"]))))

  (testing "unpacks paragraphs of multiple elements to a list"
    (is (= (list "hei" "du")
           (mork/normalize-para [:p "hei" "du"])))))

(deftest normalize-1
  (is (= "hei"
         (-> '([:p "hei"])
             mork/normalize-1))))

(deftest parse
  (testing "Understands paragraphs"
    (is (= (list [:p "Hei"]
                 [:p "Hoi"])
           (mork/parse "Hei

Hoi"))))

  (testing "yet, doesn't insist that everything is made out of paragraphs"
    (is (= "Hei"
           (mork/parse "Hei")))
    )
  )
