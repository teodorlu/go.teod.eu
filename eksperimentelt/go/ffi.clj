(ns go.ffi)

;; Exploring FFI in Clojure

(comment
  (import '(com.sun.jna Native)
          '(com.sun.jna Function))
  (def printf (Function/getFunction "c" "printf"))
  (.invoke printf String (to-array ["TEH NUMBER IS %d" 5]))
  (.invoke printf String (to-array ["TEH NUMBER IS %d" 5]))

  )

(comment
  (require '[go.clojure-jna :as jna])
  (jna/invoke Integer c/printf  "My number: %d\n"  5)
  ;; => 22

  (let [c-printf (jna/to-fn Integer c/printf)]
    (c-printf "My number: %d\n"  5))
  ;; => 22

  (macroexpand '(jna/invoke Integer c/printf  "My number: %d\n"  5))

  (.
   (com.sun.jna.Function/getFunction "c" "printf")
   invoke
   Integer
   (clojure.core/to-array ["My number: %d\n" 5]))

  (let [c-printf (com.sun.jna.Function/getFunction "c" "printf")]
    (.invoke c-printf Integer
             (clojure.core/to-array ["My number: %d\n" 5])))
  ;; => 22

  (def c-printf (com.sun.jna.Function/getFunction "c" "printf"))
  (.invoke c-printf Integer (clojure.core/to-array ["My number: %d\n" 5]))
  ;; => 22

  (.invoke c-printf Integer (clojure.core/to-array ["My number: %d\n" (int 5)]))




  )
