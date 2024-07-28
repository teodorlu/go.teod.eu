(ns go.explore.database-connect
  (:require
   [next.jdbc :as jdbc]
   [babashka.fs :as fs]))

(assert (System/getenv "GARDEN_STORAGE"))

(def db {:dbtype "sqlite"
         :dbname (str (fs/file (System/getenv "GARDEN_STORAGE") "db.sqlite"))})
(def ds (next.jdbc/get-datasource db))

123

(next.jdbc/execute!
 ds
 ["SELECT 44 * 455 as result"])
