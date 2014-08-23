(ns hoplon-tops.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc= cell=]])
  (:require
    [tailrecursion.javelin :refer [cell]]
    [tailrecursion.castra :refer [mkremote]]))

(defn add-word [ws o w]
  (swap! ws #(conj (vec (take-last 9 %)) {:word w :origin o})))

(defc state [])
(defc error nil)
(defc loading [])

(defc= last-word (first (last state)) (partial add-word state :server))

(def get-state
  (mkremote 'hoplon-tops.api/rand-word last-word error loading))

(defc input-state nil)
(defc input-error nil)
(defc input-loading [])

(cell=
  (when-not (nil? (:data input-error))
    (swap! ~(cell state)
      #(mapv (fn [x]
               (if (= (:invalid (:data input-error)) (:word x))
                 (assoc x :invalid true)
                 x)) %))
    (reset! ~(cell input-error) nil)))

(cell=
  (when-not (nil? input-state)
    (swap! ~(cell state)
      #(mapv (fn [x]
               (if (= (:valid input-state) (:word x))
                 (assoc x :valid true)
                 x)) %))
    (reset! ~(cell input-state) nil)))

(def submit-word
  (mkremote 'hoplon-tops.api/submit-word input-state input-error input-loading))

(defn init [interval]
  (get-state)
  (js/setInterval get-state interval))

(init 1000)
