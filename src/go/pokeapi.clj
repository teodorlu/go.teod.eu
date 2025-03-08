(ns go.pokeapi)

#_(clojure.repl.deps/sync-deps)
#_(set! *print-namespace-maps* false)

(require '[babashka.http-client :as http])
(require '[charred.api :as charred])

(http/head "https://pokeapi.co/api/v2/pokemon")
(http/get "https://pokeapi.co/api/v2/pokemon")

(def call
  (memoize
   (fn [uri & [opts]]
     (-> uri
         (http/get opts)
         :body
         (charred/read-json :key-fn keyword)))))

(charred/read-json "{\"x\": 1, \"y\": 2}"
                   :key-fn keyword)

;; list first 150 pokemon
(-> "https://pokeapi.co/api/v2/pokemon"
    (call {:query-params {"offset" 0
                          "limit" 151}})
    :results)

(defn base-stat-total [pokemon]
  (->> pokemon
       :stats
       (map :base_stat)
       (reduce +)))

(def gen1-pokemon
  (vec (pmap (comp call :url)
             (-> "https://pokeapi.co/api/v2/pokemon"
                 (call {:query-params {"offset" 0
                                       "limit" 151}})
                 :results))))

(defn pokemon->qualified-name
  [pokemon]
  (when-let [n (:name pokemon)]
    (keyword "pokemon" n)))

(def gen1-pokemon-map
  (->> gen1-pokemon
       (map (juxt pokemon->qualified-name identity))
       (into (sorted-map))))
(comment
  (keys gen1-pokemon-map)
  (take 5 (keys gen1-pokemon-map))
  )

(defn entity [k]
  (case (namespace k)
    "pokemon" (call (str "https://pokeapi.co/api/v2/pokemon/" (name k)))
    nil))

(entity :pokemon/arcanine)

;; strongest by base stat total
(->> (sort-by base-stat-total > gen1-pokemon)
     (map (juxt :name base-stat-total)))

(get (group-by :name gen1-pokemon)
     "arcanine")

(def mew (call "https://pokeapi.co/api/v2/pokemon/151/"))

(:stats mew)

;; task: compute the base stat total for all pokemon.

(dissoc mew :moves)
(keys mew)
(base-stat-total mew)
