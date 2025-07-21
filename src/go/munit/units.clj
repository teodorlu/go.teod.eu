(ns go.munit.units
  "Make your own unit system

  Units give you arithmethic on quanity. A quanitity has a size and a unit.

  Make units with plain numbers or by referring to base units:
    4 (unitless)
    si/m
    (u/* 4 si/m)

  Return values are numbers, vectors or maps.
    4 ; four
    [4 si/m] ; four meters
    [4 {si/m 2}] ; four square meters

  You can contruct any derived unit yourself:
    (def km (u/* 1000 si/m))

  In summary,
  - pure numbers are unitless numbers
  - vectors imply multiplication
  - maps are base unit exponents"
  (:refer-clojure :exclude [* / + -])
  (:require [go.munit.impl]))

(defn define-system [{:keys [bases]}]
  {:bases (into (sorted-set) bases)})

(defrecord BaseUnit [system sym])

(defmethod print-method BaseUnit [base-unit ^java.io.Writer w]
  (.write w (pr-str (.sym base-unit))))

(defn base [system-var unit-sym]
  (when-not (and (var? system-var)
                 (map? (deref system-var)))
    (throw (ex-info "system-var must be a var poting to a unit system"
                    {:system-var system-var})))
  (when-not (symbol? unit-sym)
    (throw (ex-info "unit-sym must be a symbol"
                    {:unit-sym unit-sym})))
  (when-not (contains? (:bases (deref system-var))
                       unit-sym)
    (throw (ex-info "The bases of the unit system must contain the unit"
                    {:system-var system-var
                     :unit-sym unit-sym})))
  (BaseUnit. system-var unit-sym))

(defn unit [q] #_todo)
(defn magnitude [q] #_todo)

;; Loading from symbolic serializing to symbolic can solve for storage
;; (defn load-symbolic [system-var symbolic])
;; but let's not start there.

(defn * [& args])
(defn / [& args])
(defn + [& args])
(defn - [& args])

(defn simplify [q]
  (go.munit.impl/simplify q))
