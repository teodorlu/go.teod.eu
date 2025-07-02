(ns go.repl
  (:refer-clojure :exclude [read])
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]))

(def f (fs/file (System/getenv "GARDEN_STORAGE") "todo.txt"))

(def read #(->> f slurp str/split-lines))
(def write #(do (spit f (str %1 "\n") :append true) (read)))
(def reset #(spit f ""))

#_(reset)
#_(read)
#_(write "Alan Kay https://www.tele-task.de/lecture/video/2772/")
#_(write "The Bazaar https://www.youtube.com/watch?app=desktop&v=xNhwET9VZ5Y")
