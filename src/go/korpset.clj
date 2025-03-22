(ns go.korpset
  (:require [clojure.string :as str]))

(def vår
  "
7 januar	Første øvelse						Hele korpset
18 januar		Dansegalla Haug musikkorps			Senior/storband
18 februar		Ikke øvelse, vinterferie
20 februar		Ikke øvelse, vinterferie
27 februar		Frelsesarmeens kulturkafe				Senior/storband
19 mars			Årsmøte Holstsgt.					Foresatte m.fl.
29-30 mars		Helgeseminar Sagene Skole			Hele korpset
6 april			Vårkonserten, Voldsløkka scene			Hele korpset
15 april		Ikke øvelse, påskeferie
3-4 mai		Holstsgt. til disposisjon, ikke avklart
17 mai		Spilling morgen, barnetog mm			Hele korpset
23-25 mai		Korpsfestival i Elverum		Hele korpset
12 juni			Klækken hotell					Senior
17 juni		Sommeravslutning, Holsts gate/Sagene skole	Hele korpset
")

(def høst
  "
5 august		Tirsdag, første øvelse (ikke enetimer)		Hele korpset
24 august		Søndagstur, sosialt arr, båttur på fjorden (?)	Hele korpset
7 september		Spilling i hagen i Torshov kirke (?)		Senior
18 september		Elvelangs, kveldsspilling langs Akerselva		Senior
20-21 september		Helgeseminar Holstsgt. /Sagene skole (?)	Hele korpset
30 september		Ikke øvelse, høstferie
2 oktober		Ikke øvelser, høstferie
7 oktober		Konsert med Lambertseter UK, Kolben		Hele korpset
18-19 oktober		Holstsgt. til disposisjon, seminar huskonsert(?)	Hele korpset
21 oktober		Huskonsert						Hele korpset
28 oktober		Musikantfest						Hele korpset
11 november		Ikke øvelse, Sagene Cabareten i Holstsgt.
13 november		Frelsesarmeens kulturkafe, ikke avklart,	Senior/storband
22-23 november,		Holstsgt. til disposisjon
November		Flere julegrantenninger, ikke avklart,	Hele korpset
")

(defn parse [text]
  (->> text str/trim str/split-lines
       (map #(str/split % #"\s*\t+\s*"))))
#_(-> vår parse first)

(defn cells->csv [cells]
  (->> cells
       (map #(str/join "," %))
       (str/join "\n")))

(require 'replicant.string)

(comment
  (parse høst)
  (parse vår)

  (spit "/Users/teodorlu/Downloads/vår.csv" (cells->csv (parse vår)))
  (spit "/Users/teodorlu/Downloads/høst.csv" (cells->csv (parse høst)))

  )

(defn parse2 [text]
  (->> text str/trim str/split-lines
       (map #(str/split % #"\s*\t+\s*"))
       (map (partial zipmap [:når :hva :hvem]))))

(parse2 vår)

;; table-layout: fixed;
;; width: 120px;

(defn ->hiccup [maps]
  [:table #_ {:style {:table-layout :fixed
                   :width "120px"}}
   [:thead [:th "Dato"] [:th "Arrangement"] [:th "For hvem"]]
   [:tbody
    (map (fn [{:keys [når hva hvem]}]
           [:tr
            [:td når]
            [:td hva]
            [:td hvem]])
         maps)]])

(spit "terminliste-2025.html"
      (-> [:div
           [:p [:strong "Terminliste vår 2025"]]
           (-> vår parse2 ->hiccup)

           [:p [:strong "Terminliste høst 2025"]]
           (-> høst parse2 ->hiccup)]
          (replicant.string/render {:indent 2})))

(replicant.string/render
 [:div [:p "hei"]]
 {:indent 2})

(replicant.string/render [:table [:thead]]
                         {:indent 2})
