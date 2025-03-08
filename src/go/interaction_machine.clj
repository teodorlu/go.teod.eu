(ns go.interaction-machine)

;; thread safety is not a goal for now due to lack of effort and skill
;; limitations.

(deftype Machine [initial-state step-fn done? state state-kw done-promise])

(def !machines (atom []))

(defn create [initial-state step-fn done?]
  (let [machine (Machine. initial-state step-fn done? (atom nil) (atom :ready) (promise))]
    (swap! !machines conj machine)
    machine))

(defn start [^Machine machine]
  (if (compare-and-set! (.state-kw machine) :ready :running)
    (do
      (future
        (reset! (.state machine) (.initial-state machine))
        (loop []
          (prn [:stepping {:state (deref (.state machine))}])
          (swap! (.state machine) (.step-fn machine))
          (cond
            (= :stopping (deref (.state-kw machine)))
            (when (compare-and-set! (.state-kw machine) :stopping :stopped)
              (prn :stopped)
              (deliver (.done-promise machine) :stopped))

            ((.done? (deref (.state machine)))
             (deref (.state machine)))
            (do
              (prn :completed)
              (reset! (.state-kw machine) :completed)
              (deliver (.done-promise machine) :completed))

            :else
            (do
              (prn :recurring)
              (recur)))))
      true)
    false))

(defn stop [^Machine machine]
  (compare-and-set! (.state-kw machine)
                    :running :stopping)
  (deref (.done-promise machine)))

(defn wait [^Machine machine]
  (deref (.done-promise machine))
  (deref (.state machine)))

(comment

  (let [counter (create 0 inc #(< (rand) 0.95))]
    (prn ::starting)
    (start counter)
    (prn ::waiting))

  @(.state (last @!machines))
  @(.state-kw (last @!machines))
  (.done-promise (last @!machines))
  )
