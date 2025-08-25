(ns go.core
  (:require
   [clojure.string :as str]
   [go.bretroulette :as bretroulette]
   [go.framework :as framework]
   [go.path :as path]
   [replicant.string])
  (:import
   [java.time Instant]))

(set! *print-namespace-maps* false)

(def bright-green "hsl(124, 100%, 88%)")
(def black "rgba(0,0,0,1.00)")
(def greyish "hsl(108, 5%, 40%);")
(def bright-blue "rgb(109 219 253)")

(defn valid-theme? [theme]
  (every? #(contains? theme %)
          [:theme/primary-color
           :theme/secondary-color
           :theme/unobtrusive-color
           :theme/emphasis-color]))

(def theme-blumoon
  {:theme/primary-color bright-green
   :theme/secondary-color black
   :theme/unobtrusive-color greyish
   :theme/emphasis-color bright-blue})

(defn linkroll [theme tplay-root tgo-root]
  [:div {:style {:css.prop/font-size "1.2rem"
                 :css.prop/margin-top "1em"
                 :css.prop/color (:theme/unobtrusive-color theme)}}

   (interpose
    " · "
    (for [{:keys [root path text]} path/navigation]
      [:a {:href (str root path)
           :style {:css.prop/color (:theme/unobtrusive-color theme)}}
       text]))])
;; I don't like that this function mixes both style, content and logic
;; I'd rather have a def
#_(linkroll {} "" "")

(defn linkroll-from-tgo [theme]
  (linkroll theme "https://play.teod.eu" ""))

(def principles
  (->> ["Balance." "Body ↔ Mind ↔ Emotions."
        "Habits for action" "get you started."
        "Creation & curiosity" "over consumption & passivity."
        ;; "Techne ≠ episteme." "Not the same thing."
        "Rest or focus?" (str "Strive for balance."
                              " Body ↔ Mind ↔ Emotions.")]
       (partition 2)
       (map (fn [[principle-core principle-extras]]
              {:principle/core principle-core
               :principle/extras principle-extras}))))

(defn view-principle [theme principle]
  [:div
   {:style {:css.prop/font-size "1.8rem"}}
   [:span {:style {:css.prop/color (:theme/emphasis-color theme )}}
    (str/upper-case (:principle/core principle))]
   " "
   [:span {:style {:css.prop/color (:theme/primary-color theme)}}
    (:principle/extras principle)]])

(defn req->title [req]
  (cond (= "localhost" (:server-name req))
        "🩵 go principled"

        :else
        "🌊 go principled"))

(defn write-weeknote-fragment [_req]
  (let [theme theme-blumoon]
    {:status 200
     :body
     (replicant.string/render
      [:form {:id :id/weeknote-editor}
       [:div {:style {:css.prop/color (:theme/primary-color theme)
                      :css.prop/font-size "1.2rem"}}
        "Write your weeknote:"]
       [:textarea {:style {:css.prop/width "100%"
                           :css.prop/resize "vertical"
                           :css.prop/height "10rem"
                           :css.prop/border "1px solid white"
                           :css.prop/color (:theme/emphasis-color theme)
                           :css.prop/background-color (:theme/secondary-color theme)
                           :css.prop/font-size "0.9rem"
                           :css.prop/padding "0.4rem"}
                   :name "weeknote"
                   :placeholder "text …"}]
       [:button {:hx-post path/add-weeknote
                 :hx-target (str "#" (name :id/weeknote-editor))
                 :hx-swap :htmx/outerHTML}
        "Submit weeknote"]
       [:span " "]
       [:button {:hx-get path/add-weeknote-prompt
                 :hx-target (str "#" (name :id/weeknote-editor))
                 :hx-swap :htmx/outerHTML}
        (str "I will never submit!"
             " Instead, I, elect to toss this weeknote into the ether!")]])}))

(comment
  ;; View weeknotes
  (->> framework/state
       deref
       :weeknotes
       (map :text)
       (str/join "\n---\n")
       println)

  (keys @framework/state)
  (-> @framework/state :weeknotes count)

  ,)



(defn add-weeknote [req]
  (let [theme theme-blumoon]
    (when-let [weeknote (get-in req [:params "weeknote"])]
      (swap! framework/state update :weeknotes (fnil conj [])
             {:text weeknote
              :uuid (random-uuid)
              :timestamp (str (Instant/now))})
      {:status 200
       :body
       (replicant.string/render
        [:div {:id :id/weeknote-editor}
         [:div {:style {:css.prop/color (:theme/primary-color theme)}}
          "Weeknote added!"]
         [:a {:hx-get path/add-weeknote
              :hx-target (str "#" (name :id/weeknote-editor))
              :hx-swap :htmx/outerHTML
              :style {:css.prop/color (:theme/primary-color theme)
                      :css.prop/text-decoration :css.val/underline}}
          "If you wish, add another."]])})))

(defn add-weeknote-button [theme]
  [:div {:id :id/weeknote-editor}
   [:a {:hx-get path/add-weeknote
        :hx-target (str "#" (name :id/weeknote-editor))
        :hx-swap :htmx/outerHTML
        :style {:css.prop/color (:theme/primary-color theme)
                :css.prop/text-decoration :css.val/underline}}
    "Add weeknote"]])

(defn add-weeknote-prompt [_req]
  (let [theme theme-blumoon]
    {:status 200
     :body
     (replicant.string/render (add-weeknote-button theme))}))

(defn principles-page
  [req]
  (let [theme theme-blumoon]
    (assert (valid-theme? theme))
    (framework/page
     {:title (req->title req) :theme theme}
     [:section {:style {:css.prop/height "100%"
                        :css.prop/display :css.val/flex
                        :css.prop/flex-direction :css.val/column
                        :css.prop/gap "2rem"
                        :css.prop/justify-content :css.val/center
                        :css.prop/line-height "2rem"
                        }}
      (map (partial view-principle theme) principles)
      (linkroll-from-tgo theme)
      (add-weeknote-button theme)])))

(defn bretroulette-page
  [_req]
  (let [theme theme-blumoon
        ref (bretroulette/spin)]
    (assert (valid-theme? theme))
    (framework/page
     {:title "roll for serendipity in Bret's refs" :theme theme}
     [:section {:style {:css.prop/min-height "100%"
                        :css.prop/display :css.val/flex
                        :css.prop/flex-direction :css.val/column
                        :css.prop/gap "2rem"
                        :css.prop/justify-content :css.val/center
                        :css.prop/line-height "2rem"}}

      [:div [:a {:style {:css.prop/font-size "1.8rem"
                         :css.prop/color (:theme/emphasis-color theme)}
                 :href (::bretroulette/uri ref)}
             (::bretroulette/text ref)]]

      [:div {:style {:css.prop/color (:theme/primary-color theme)}}
       "Picked at random from Bret Victor's treasure trove of references at "
       [:a {:href bretroulette/refs-url
            :style {:css.prop/color (:theme/primary-color theme)}}
        bretroulette/refs-url] "."]

      [:div {:style {:css.prop/color (:theme/primary-color theme)}}
       [:a {:href path/bretroulette
            :style {:css.prop/color (:theme/primary-color theme)}}
        "Reroll"] "."]

      [:div {:style {:css.prop/color (:theme/primary-color theme)}}
       [:details [:summary "Download PDFs with wget"]
        [:pre {:style {:css.prop/font-size "0.8rem"
                       :css.prop/line-height "0.9rem"
                       :css.prop/margin-left "1rem"}}
         (bretroulette/refs->wget-download-command
          (filter bretroulette/pdf? @bretroulette/references))]]]

      (linkroll-from-tgo theme)])))

(defn view-weeknote [theme weeknote]
  [:div
   [:div [:em {:style {:css.prop/color (:theme/unobtrusive-color theme)
                       :css.prop/font-size "0.8em"}}
          (:timestamp weeknote)]]
   (->> weeknote :text str/split-lines (interpose [:br]))])

(defn weeknotes [_req]
  (let [theme theme-blumoon]
    (assert (valid-theme? theme))
    (framework/page
     {:title "Weeknotes" :theme theme}
     [:section {:style {:css.prop/min-height "100%"
                        :css.prop/display :css.val/flex
                        :css.prop/flex-direction :css.val/column
                        :css.prop/gap "2rem"
                        :css.prop/justify-content :css.val/center
                        :css.prop/line-height "2rem"
                        }}
      [:div {:style {:css.prop/color (:theme/primary-color theme)}}
       (->> framework/state
            deref
            :weeknotes
            (map #(view-weeknote theme %))
            (interpose [:hr]))]])))

(require 'go.notes 'go.flexing 'go.core2 'go.refs 'go.refs2 'go.beautiful)
(defn notes [_] go.notes/view)
(defn flexing [_] go.flexing/view)

(def routes
  (->> (concat [[path/index #'principles-page]
                [path/add-weeknote {:get #'write-weeknote-fragment
                                    :post #'add-weeknote}]
                [path/add-weeknote-prompt #'add-weeknote-prompt]
                [path/view-weeknotes #'weeknotes]
                [path/bretroulette #'bretroulette-page]
                [path/notes #'notes]
                [path/flexing #'flexing]
                [path/core2 #'go.core2/view]
                [path/refs #'go.refs/view]
                [path/refs2 #'go.refs2/view]]
               go.beautiful/ring-routes)
       (filter first)
       (vec)))

(defn start! [opts]
  (framework/start! #'routes opts))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; COMMENTARY AND EXPLORATION

(comment
  @framework/last-request
  @framework/state
  ,)
