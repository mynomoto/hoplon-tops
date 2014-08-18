(ns hoplon-tops.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc= cell=]])
  (:require
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(defc last-10 [])
(defc= state (first (last last-10))
  (fn [x] (swap! last-10 #(conj (vec (take-last 9 %)) [x :server]))))
(defc error nil)
(defc loading [])

(def get-state
  (mkremote 'hoplon-tops.api/rand-word state error loading))

(defc input-state nil)
(defc input-error nil)
(defc input-loading [])

(defc cummulative-input-error {})

(cell=
  (do
    (when-not (nil? (:data input-error))
      (swap! ~(cell cummulative-input-error) merge (:data input-error)))
    (swap! ~(cell cummulative-input-error) select-keys (map first last-10))))

(def submit-word
  (mkremote 'hoplon-tops.api/submit-word input-state input-error input-loading))

(defn init []
  (get-state)
  (js/setInterval get-state 1000))

(init)
