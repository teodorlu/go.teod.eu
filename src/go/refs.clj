(ns go.refs
  "
In the spirit of Bret Victor's references, I too want to keep my own references.

- I want to store PDFs here - with Garden Storage
- I want to link to other sources
- I want this to be only the good stuff - whereas play.teod.eu is \"everything\"

Not sure if \"refs\" is quite the right word.
The sense in which I use it is a bit different.

But let's leave that discussion for later.

1. Get a start going
2. Continue later.")

(def my-refs
  #{{:title "Designerly ways of knowing"
     :author "Nigel Cross"
     :year "1982"
     :source "https://oro.open.ac.uk/39253/8/Designerly%20Ways%20of%20Knowing%20DS.pdf"}})

(defn refs->content [refs]
  (into [::box]
        (for [{:keys [title author year source]} refs]
          [::text [::link source title]
           (str "(" year ", " author ")")
           "Explains what design IS"])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DESIGN

(require 'hiccup.page)
(require '[clojure.walk :refer [postwalk]])

(defn el-pred [tag]
  (fn [el] (and (vector? el)
                (= tag (first el)))))
(def core? (el-pred ::core))
(def text? (el-pred ::text))
(def box? (el-pred ::box))
(def link? (el-pred ::link))

(defn rounded [content]
  [:div {:style {:height "100%"
                 ;; :margin "10px"
                 :border "1px solid #00EAFF"
                 :border-radius "10px"
                 :padding "5px 7px 5px 7px"}}
   [:pre {:style {:margin 0}}
    (postwalk
     (fn [el]
       (cond
         (core? el)
         (into [:strong]
               (rest el))

         (text? el)
         (interpose "\n"
                    (cons (second el)
                          (map #(str "  " %) (rest (rest el)))))

         (box? el)
         (into [:pre {:style {:margin 0}}]
               (interpose "\n\n" (rest el)))

         (link? el)
         [:a {:style {:color "white"} :href (nth el 1)} (nth el 2)]

         :else el))
     content)]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HTTP HANDLER

(defn view [_]
  (hiccup.page/html5
      {:lang "en" :style {:min-height "100%"
                          :color "white"
                          :background-color "black"}}
      [:head
       [:title "exploring taste"]
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
      [:body {:style {:margin 0 :padding "15px"}}
       (rounded (refs->content my-refs))]))
