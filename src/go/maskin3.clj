(ns go.maskin3)

;; maskin2 var gøy, men jeg tror jeg bommet på designet.
;;
;; - maskin/kjør kjørte igang hele maskinen og holdt på tilstanden mellom hver iterasjon
;; - samtidig ville jeg ha innsikt i hvordan det gikk.

;; ny ide: skill "jobb" fra "maskin".
;; en maskin kan gjøre jobber.
;;
;; maskin: immutable
;; jobb: mutable

;; jobben holder både på tilstand og på hvem som lytter.
;; jobben kan også holde på maskinen?

(defn ny
  ([gå ferdig?]
   (ny gå ferdig? (constantly nil)))
  ([gå ferdig? lytter]
   {::gå gå
    ::ferdig? ferdig?
    ::lytter lytter}))

(defn gå [maskin tilstand]
  ((::gå maskin) tilstand))

(defn kjør
  [maskin start]
  (let [stoppsignal (promise)]
    {::stoppsignal stoppsignal
     ::prosess
     (future
       (loop [tilstand start]
         ((::lytter maskin)
          tilstand)
         (cond
           ((::ferdig? maskin)
            tilstand)
           tilstand

           (and (realized? stoppsignal)
                (= ::stopp! @stoppsignal))
           tilstand

           :else
           (recur ((::gå maskin)
                   tilstand)))))}))

(defn vent [jobb]
  @(::prosess jobb))

(defn stopp! [jobb]
  (deliver (::stoppsignal jobb) ::stopp!))
