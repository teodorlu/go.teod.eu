(ns go.stepper)

;; Turn imperative code into interactive state machines

;; This program runs to completion without stopping:

(defn imperative []
  (println "Starting ...")
  (Thread/sleep 10)
  (println "Done."))

#_(imperative)

(def return-immediately
  {:step (fn [state])
   :done? (fn [state] true)})

(defn counter [maximum]
  {:step inc
   :done? (fn [state]
            (<= maximum state))})

(defn run-to-completion
  [machine initial-state]
  (let [{:keys [step done?]} machine]
    (loop [state initial-state]
      (if (done? state)
        state
        (recur (step state))))))



(run-to-completion return-immediately ::initial)
;; => :go.stepper/initial

(run-to-completion (counter 10) 0)
;; => 10

;; if we wanted, we could run counter with a max step count.

(defn run-with-max-steps [machine initial-state max-steps]
  (let [{:keys [step done?]} machine]
    (loop [state initial-state
           step-number 0]
      (if (or (done? state)
              (<= max-steps step-number))
        state
        (recur (step state)
               (inc step-number))))))

(run-with-max-steps (counter 999)
                    33
                    100)
;; => 133
