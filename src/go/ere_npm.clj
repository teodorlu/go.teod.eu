(ns go.ere-npm)

(require '[babashka.process :as p]
         '[clojure.string :as str])

(defn ls [dir]
  (->> (p/shell {:out :string} "ls" dir)
       :out
       str/trim
       str/split-lines))

(p/shell {:out :string} "npm --version")
(p/shell {:out :string} "ls")

(System/getenv "GARDEN_STORAGE")

(ls (System/getenv "GARDEN_STORAGE"))
(-> (System/getenv "GARDEN_STORAGE")
    (str "/duratom.edn")
    slurp
    read-string)

(System/getenv "PATH")

(ls ".")
