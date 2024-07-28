(ns go.explore.datalevin-from-scratch
  (:require
   [datalevin.core :as d]
   [babashka.fs :as fs]))

(assert (System/getenv "GARDEN_STORAGE")
        "A bound GARDEN_STORAGE is required: we need somewhere to put the data.")

(def datalevin-db-path (str (fs/file (System/getenv "GARDEN_STORAGE") "datalevin-from-scratch-db")))
(def schema {:weeknote-seed/text {:db/valueType :db.type/string}})
(def conn (d/create-conn datalevin-db-path
                         schema
                         {:auto-entity-time? true}))

(comment
  (d/transact! conn [{:weeknote-seed/text "I'd like to explore color themes."}])
  :rfc)

(d/q '[:find ?text
       :where [_ :weeknote-seed/text ?text]
       :timeout 5000]
     (d/db conn))
