(ns go.rounded)

(require 'hiccup.page)
(require '[clojure.walk :refer [postwalk]])

(defn el-pred [tag]
  (fn [el] (and (vector? el)
                (= tag (first el)))))
(def core? (el-pred ::core))
(def text? (el-pred ::text))
(def vbox? (el-pred ::vbox))
(def hbox? (el-pred ::hbox))
(def link? (el-pred ::link))
(def bordered? (el-pred ::bordered))

(defn rounded [content]
  (postwalk
   (fn [el]
     (cond
       (core? el)
       (into [:strong]
             (rest el))

       (text? el)
       (interpose "\n"
                  (cons (second el)
                        (map #(str "  " %) (rest (rest (filter some? el))))))

       (vbox? el)
       (into [:pre {:style {:margin 0}}]
             (interpose "\n\n" (rest el)))

       (hbox? el)
       (into [:pre {:style {:margin 0}}]
             (interpose " · " (rest el)))

       (link? el)
       [:a {:style {:color "white"} :href (nth el 1)} (nth el 2)]

       (bordered? el)
       [:div {:style {:height "100%"
                      ;; :margin "10px"
                      :border "1px solid #00EAFF"
                      :border-radius "10px"
                      :padding "5px 7px 5px 7px"}}
        (into [:pre {:style {:margin 0}}] (rest el))]


       :else el))
   content))

(defn ^{:deprecated "2025-03-24"} rounded-legacy
  [content]
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
                          (map #(str "  " %) (rest (rest (filter some? el))))))

         (vbox? el)
         (into [:pre {:style {:margin 0}}]
               (interpose "\n\n" (rest el)))

         (hbox? el)
         (into [:pre {:style {:margin 0}}]
               (interpose " · " (rest el)))

         (link? el)
         [:a {:style {:color "white"} :href (nth el 1)} (nth el 2)]

         :else el))
     content)]])
