(ns go.core
  (:require
   [clojure.string :as str]
   [hiccup2.core :as hiccup]
   [org.httpkit.server :as httpkit]
   [go.path :as path]))

(def bright-green "hsl(124, 100%, 88%)")
(def brighter-green "hsl(122.67 89% 94%)")
(def black "rgba(0,0,0,1.00)")
(def cautious-black "rgba(0,0,0,0.95)")
(def greyish "hsl(108, 5%, 40%);")
(def bright-blue "rgb(109 219 253)")
(def dark-blue "rgb(0, 91, 119)")
(def crimson "hsl(19, 100%, 44%)")
(def julian-yellow "rgb(255,203,107)")
(def neno-blue "rgb(130,170,255)")
(def line-grønn "rgb(179,213,130)")

(defn valid-theme? [theme]
  (every? #(contains? theme %)
          [:theme/primary-color
           :theme/secondary-color
           :theme/unobtrusive
           :theme/emphasis]))

(def theme-main
  {:theme/primary-color bright-green
   :theme/secondary-color black
   :theme/unobtrusive greyish
   :theme/emphasis bright-blue})

(def theme-code
  {:theme/emphasis neno-blue
   :theme/secondary-color cautious-black
   :theme/unobtrusive greyish
   :theme/primary-color julian-yellow})

(def theme-line
  {:theme/emphasis julian-yellow
   :theme/secondary-color black
   :theme/unobtrusive greyish
   :theme/primary-color neno-blue})

(def theme-other
  {:theme/primary-color black
   :theme/secondary-color bright-green
   :theme/unobtrusive greyish
   :theme/emphasis crimson})

(def theme-other-crimson
  {:theme/primary-color black
   :theme/secondary-color brighter-green
   :theme/unobtrusive greyish
   :theme/emphasis crimson})

(def theme-other-brighter
  {:theme/primary-color black
   :theme/secondary-color brighter-green
   :theme/unobtrusive greyish
   :theme/emphasis dark-blue})

(def theme-bw
  {:theme/primary-color "rgba(0, 0, 0, 80%)"
   :theme/secondary-color "rgba(0, 0, 0, 0%)"
   :theme/unobtrusive "rgba(0, 0, 0, 60%)"
   :theme/emphasis "rgba(0, 0, 0, 100%)"})

(assert (every? valid-theme? [theme-main theme-other theme-other-crimson]))

(def section-style-left-adjust
  "Left adjust text in sections"
  {})

(def section-style-center
  "Center text in sections"
  {:text-align :center :justify-content :center})

(def section-style-paragraph-indented-text
  "Left adjust text in sections, indenting text after first line"
  {:text-indent "3em hanging"})

(defn principles-page
  ([title theme]
   (principles-page title theme {}))
  ([title theme opts]
   (assert (valid-theme? theme))
   [:html {:lang "en" :style {:height "100%"}}
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
                              (:section-style/overrides opts section-style-left-adjust))}
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
             {:linktext path/theme-code :href path/theme-code}
             {:linktext path/theme-line :href path/theme-line}
             {:linktext "play.teod.eu" :href path/play-teod-eu}]
            (map (fn [{:keys [linktext href]}]
                   [:a {:href href
                        :style {:color (:theme/unobtrusive theme)}}
                    linktext]))
            (interpose " · "))]
      [:div {:style {:font-size "1.2rem" :color (:theme/unobtrusive theme)}}
       [:em "TODO "]
       (interpose " · " ["Consider adding weeknote text field"
                         "Consider adding an interesting video roulette"])]]]]))

(defn page
  ([req theme] (page req theme {}))
  ([req theme opts]
   (let [title (str (get {"localhost" "🩵"} (:server-name req) "🌊 🌊 🌊"))]
     (principles-page title theme opts))))

;; Deprecated design options

(defn page-other [req]
  (page req theme-other
        {:section-style/overrides section-style-center}))

(defn page-other2 [req]
  (page req theme-other-crimson
        {:section-style/overrides section-style-center}))

(defn page-other3 [req]
  (page req theme-other-brighter
        {:section-style/overrides section-style-center}))

(defn page-other5 [req]
  (page req theme-other-brighter
        {:section-style/overrides section-style-paragraph-indented-text}))

;; Open design options

(defn page-index      [req] (page req theme-main))
(defn page-other4     [req] (page req theme-other-brighter))
(defn page-other6     [req] (page req theme-bw))
(defn page-theme-code [req] (page req theme-code))
(defn page-theme-line [req] (page req theme-line))

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
   path/other6 #'page-other6
   path/theme-code #'page-theme-code
   path/theme-line #'page-theme-line})

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
  (or (when-let [handler (get routes (:uri req))]
        (-> req handler render))
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
