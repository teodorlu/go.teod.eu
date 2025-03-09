(ns go.flexing)

(require '[go.notes :refer [notes]])

notes

(require 'hiccup2.core)
(require 'hiccup.page)

(def view
  (hiccup.page/html5
   {:lang "en" :style {:min-height "100%"
                       :color "white"
                       :background-color "black"}}
   [:head
    [:title "flexing with teal"]
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body {:style {:margin 0
                   }}
    [:div {:style {:margin "10px"
                   :border "1px solid #00EAFF"
                   :border-radius "10px"
                   :padding "5px 7px 5px 7px"}}
     [:pre {:style {:margin 0}}
      (->>
       (for [[head more] notes]
         (list [:strong head] "\n"
               (when more
                 (for [line more]
                   (str "  " line "\n")))))
       (interpose "\n"))]]]))
