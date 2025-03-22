(ns go.mork
  (:require [clojure.string :as str]))

(defn el-pred [tag]
  #(and (vector? %) (= tag (first %))))

(def para? (el-pred :p))

#_(para? [:p "lol"])

(defn normalize-list [ast]
  (cond-> ast
    (and (seq? ast)
         (= 1 (count ast)))
    first))

#_(normalize-list '("hei"))

(defn normalize-para [ast]
  (cond
    (and (para? ast)
         (= 1 (count (rest ast))))
    (second ast)

    (para? ast)
    (rest ast)

    :else
    ast))

(defn normalize-1 [ast]
  (-> ast normalize-list normalize-para))

(defn normalize [ast]
  (loop [ast' ast]
    (let [ast'' (normalize-1 ast')]
      (if (= ast' ast'')
        ast'
        (recur ast'')))))

(comment
  (-> (list [:p "Hei"])
      normalize-list
      normalize-para)

  (-> (list [:p "Hei"])
      normalize)

  )

(defn parse* [mork-str]
  (->> (str/split mork-str #"\n\n")
       (map #(vector :p %))))

(defn parse [mork-str]
  (->> mork-str
       parse*
       normalize))

(comment
  (def x (parse "hei"))

  (normalize x)
  )
