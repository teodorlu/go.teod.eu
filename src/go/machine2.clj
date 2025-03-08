(ns go.machine2)

(defn create [state step-fn done?]
  {::state state
   ::step-fn step-fn
   ::done? done?
   ::halt! (promise)
   ::report-fns (atom [])})

(defn step [machine]
  (update machine ::state (::step-fn machine)))

(defn state [machine]
  (::state machine))

(defn done? [{::keys [state done?]}]
  (boolean (done? state)))

(defn halt! [machine]
  (deliver (::halt! machine) ::halt))

(defn report-all! [machine]
  (doseq [report-fn @(::report-fns machine)]
    (report-fn (state machine))))

(defn add-reporter [machine reporter]
  (swap! (::report-fns machine) conj reporter))

(defn run [machine]
  (loop [current machine]
    (report-all! current)
    (if (realized? (::halt! machine))
      current
      (let [updated (step current)]
        (if (done? updated)
          (doto updated report-all!)
          (recur updated))))))
