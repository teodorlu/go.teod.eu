(ns user)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start! []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7658]
    (clerk-serve {:browse? true :port clerk-port})))

#_(clerk-start!)
