(ns go.ffi2)

;; # Estimating Pi with dtype-next and Clojure vectors

;; In Clojure, we often reach for sequences of maps to store data.
;; This approach works well for small datasets - we say within Clojure happy-land, and can always inspect our data.
;; Just print it, it's data.
;;
;; At some point, computing with sequences of maps feels worse: we start having to wait.
;; In those cases, we might consider storing our data column oriented, and practicing [array programming].
;; The Clojure data science toolbox gives us some great tools here.
;; `dtype-next` provides high-performance containers, and `scicloj/tablecloth` provides a nice high-level interface.
;;
;; [array programming]: http://www.appliedscience.studio/articles/array-programming-for-clojurists.html

;; TODO FIGURE

;; We will explore these two approaches by estimating Pi with monte carlo simulation.
;; There smarter ways of estimating Pi, but this one will suffice for our needs.


(comment
  (.invoke (com.sun.jna.Function/getFunction "c" "printf")
           Long
           (clojure.core/to-array ["My number: %d\n" 5]))

  (require 'clojure.repl.deps)

  (clojure.repl.deps/add-lib 'cnuernber/dtype-next {:mvn/version "10.131"})


  )
