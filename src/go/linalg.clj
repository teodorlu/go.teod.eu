(ns go.linalg
  "Linear algebra for computational mechanics

   In this file, we will explore whether we can \"get anywhere\" with Neanderthal
   for implementing finite element analysis primitives in Clojure. We will take
   nomenclature from this text book:

       An engineering approach to Finite Element Analysis of linear structural
       mechanics problems

       Kolbein Bell, 2013
       https://fagbokforlaget.no/produkt/9788232102686-an-engineering-approach-to-finite-element-analysis-of-linear-structural-mec

   Kolbein Bell (https://bell.folk.ntnu.no/) played an important role in
   establishing computational mechanics at NTNU from the 60s up untill now, and
   is a good place to start.")

(require '[uncomplicate.commons.core :refer [with-release]]
         '[uncomplicate.neanderthal
           [native :refer [dv dge]]
           [core :refer [mv mm trans]]])

(def k (dge 3 3 [1 1 1
                 2 2 2
                 3 3 3]
            {:layout :row}))

(def ones-row3 (dge 1 3 [1 1 1]))
(def ones-col3 (dge 3 1 [1 1 1]))
(def scalar->matrix #(dge 1 1 [%]))

(= (scalar->matrix 18.0)
   (mm
    ones-row3
    (mm k ones-col3)))
