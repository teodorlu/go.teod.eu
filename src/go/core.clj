(ns go.core
  (:require
   [clojure.string :as str]
   [hiccup2.core :as hiccup]
   [org.httpkit.server :as httpkit]
   [go.path :as path]))

(def bright-green "hsl(124, 100%, 88%)")
(def brighter-green "hsl(122.67 89% 94%)")
(def blackish "black")
(def greyish "hsl(108, 5%, 40%);")
(def bright-blue "rgb(109 219 253)")
(def dark-blue "rgb(0, 91, 119)")
(def crimson "hsl(19, 100%, 44%)")

(defn valid-theme? [theme]
  (every? #(contains? theme %)
          [:theme/primary-color
           :theme/secondary-color
           :theme/unobtrusive
           :theme/emphasis]))

(def theme-main
  {:theme/primary-color bright-green
   :theme/secondary-color blackish
   :theme/unobtrusive greyish
   :theme/emphasis bright-blue})

(def theme-other
  {:theme/primary-color blackish
   :theme/secondary-color bright-green
   :theme/unobtrusive greyish
   :theme/emphasis crimson})

(def theme-other-crimson
  {:theme/primary-color blackish
   :theme/secondary-color brighter-green
   :theme/unobtrusive greyish
   :theme/emphasis crimson})

(def theme-other-brighter
  {:theme/primary-color blackish
   :theme/secondary-color brighter-green
   :theme/unobtrusive greyish
   :theme/emphasis dark-blue})

(def theme-bw
  {:theme/primary-color "rgba(0, 0, 0, 80%)"
   :theme/secondary-color "rgba(0, 0, 0, 0%)"
   :theme/unobtrusive "rgba(0, 0, 0, 60%)"
   :theme/emphasis "rgba(0, 0, 0, 100%)"})

(assert (every? valid-theme? [theme-main theme-other theme-other-crimson]))

(def section-style-center
  "Center text in sections"
  {:text-align :center :justify-content :center})
(def section-style-left-adjust
  "Left adjust text in sections"
  {})
(def section-style-paragraph-indented-text
  "Left adjust text in sections, indenting text after first line"
  {:text-indent "3em hanging"})

(defn principles-page
  ([title theme]
   (principles-page title theme {}))
  ([title theme opts]
   (assert (valid-theme? theme))
   [:html {:lang "en"
           :style {:height "100%"}}
    [:head
     [:title title]
     [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]]
    [:body {:style {:height "100%" :margin 0
                    :font-size "1.8rem"
                    :padding-left "1rem"
                    :padding-right "1rem"
                    :background-color (:theme/secondary-color theme)
                    :font-family "serif"}}
     [:section {:style (merge {:height "100%"
                               :display :flex
                               :flex-direction :column
                               :gap "2rem"
                               :justify-content :center
                               :line-height "100%"
                               :color (:theme/primary-color theme)}
                              (:section-style/overrides opts section-style-center))}
      (for [[principle-core principle-extras]
            (partition 2 ["Balance." "Body ↔ Mind ↔ Emotions."
                          "Habits for action" "get you started."
                          "Creation & curiosity" "over consumption & passivity."
                          "Techne ≠ episteme." "Not the same thing."
                          "Rest or focus?" (str "Search for balance."
                                                " Body ↔ Mind ↔ Emotions.")])]
        [:div [:span {:style {:color (:theme/emphasis theme )}}
               (str/upper-case principle-core)]
         " " principle-extras])
      [:div {:style {:font-size "1.2rem"
                     :margin-top "1em"
                     :color (:theme/unobtrusive theme)}}
       (->> [{:linktext path/index :href path/index}
             ;; {:linktext path/other :href path/other}
             ;; {:linktext path/other2 :href path/other2}
             ;; {:linktext path/other3 :href path/other3}
             {:linktext path/other4 :href path/other4}
             {:linktext path/other6 :href path/other6}
             {:linktext "play.teod.eu" :href path/play-teod-eu}]
            (map (fn [{:keys [linktext href]}]
                   [:a {:href href
                        :style {:color (:theme/unobtrusive theme)}}
                    linktext]))
            (interpose " · "))]]]]))

(defn page
  ([req title-suffix theme] (page req title-suffix theme {}))
  ([req title-suffix theme opts]
   (principles-page (str (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊")
                         title-suffix)
                    theme
                    opts)))

(defn page-index [req]
  (page req "" theme-main
        {:section-style/overrides section-style-left-adjust}))

(defn page-other [req]
  (page req " other" theme-other))

(defn page-other2 [req]
  (page req " other 2" theme-other-crimson))

(defn page-other3 [req]
  (page req " other 3" theme-other-brighter))

(defn page-other4 [req]
  (page req " other 4" theme-other-brighter
        {:section-style/overrides section-style-left-adjust}))

(defn page-other5 [req]
  (page req " other 5" theme-other-brighter
        {:section-style/overrides section-style-paragraph-indented-text}))

(defn page-other6 [req]
  (page req " other 6" theme-bw
        {:section-style/overrides section-style-left-adjust}))

(defn icon-web [_]
  {:status 200 :body "icon web"})

(def routes
  {path/icon-web #'icon-web
   path/index #'page-index
   path/other #'page-other
   path/other2 #'page-other2
   path/other3 #'page-other3
   path/other4 #'page-other4
   path/other5 #'page-other5
   path/other6 #'page-other6})

(defn render [content]
  (cond
    ;; Vectors are Hiccup HTML
    (vector? content)
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>") content))}

    ;; Maps are ring responses
    (map? content)
    map))

(defn not-found [description]
  {:status 404
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hiccup/html (hiccup/raw "<!DOCTYPE html>")
                [:html [:body "Page not found: " description]]))})

(defn root-handler [req]
  (when (not= "/clerk_service_worker.js" (:uri req))
    (tap> req))
  (if-let [handler (get routes (:uri req))]
    (let [response (-> req handler render)]
      (if response
        response
        (not-found (:uri req))))
    (not-found (:uri req))))

(defonce server (atom nil))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn serve! [_]
  (swap! server
         (fn [old-server]
           (when old-server
             (httpkit/server-stop! old-server))
           (httpkit/run-server #'root-handler
                               {:port 7777
                                :legacy-return-value? false}))))

#_ (deref server)
#_ (httpkit/server-status @server)
#_ (serve! {})
#_ (httpkit/server-stop! @server)
