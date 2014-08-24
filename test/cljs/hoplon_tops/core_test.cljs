(ns hoplon-tops.core-test
  (:require-macros
    [purnam.test :refer [describe it is is-not runs waits-for]]
    [tailrecursion.javelin :refer [defc]]
    [dommy.macros :refer [sel sel1 node]])
  (:require
    [purnam.test]
    [clojure.test.check :as tc]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop :include-macros true]
    [tailrecursion.javelin :refer [cell]]
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [hoplon-tops.component :as c]))

(defc c (cell nil))
(def word-item (c/word-item c))

(describe
  {:doc "Generative testing word-item"
   :globals [el word-item]}
  (it "Should add correct classes to server words"
    (waits-for "Just a cycle" 0 true)
    (runs
      (is
        (tc/quick-check 100
          (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
            (let [_ (reset! c (assoc w :origin :server))]
              (and
                (= (dommy/text el) (:word w))
                (dommy/has-class? el "list-group-item")
                (not (dommy/has-class? el "list-group-item-warning"))
                (not (dommy/has-class? el "list-group-item-success"))
                (not (dommy/has-class? el "list-group-item-danger"))
                (not (dommy/has-class? el "invalid"))))))
        #(:result %))))
  (it "Should add correct classes to local words"
    (waits-for "Just a cycle" 0 true)
    (runs
      (is
        (tc/quick-check 100
          (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
            (let [_ (reset! c (assoc w :origin :local))]
              (and
                (= (dommy/text el) (:word w))
                (dommy/has-class? el "list-group-item")
                (dommy/has-class? el "list-group-item-warning")
                (not (dommy/has-class? el "list-group-item-success"))
                (not (dommy/has-class? el "list-group-item-danger"))
                (not (dommy/has-class? el "invalid"))))))
        #(:result %))))
  (it "Should add correct classes to local valid words"
    (waits-for "Just a cycle" 0 true)
    (runs
      (is
        (tc/quick-check 100
          (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
            (let [_ (reset! c (assoc w :origin :local :valid true))]
              (and
                (= (dommy/text el) (:word w))
                (dommy/has-class? el "list-group-item")
                (not (dommy/has-class? el "list-group-item-warning"))
                (dommy/has-class? el "list-group-item-success")
                (not (dommy/has-class? el "list-group-item-danger"))
                (not (dommy/has-class? el "invalid"))))))
        #(:result %))))
  (it "Should add correct classes to local invalid words"
    (waits-for "Just a cycle" 0 true)
    (runs
      (is
        (tc/quick-check 100
          (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
            (let [_ (reset! c (assoc w :origin :local :invalid true))]
              (and
                (= (dommy/text el) (:word w))
                (dommy/has-class? el "list-group-item")
                (not (dommy/has-class? el "list-group-item-warning"))
                (not (dommy/has-class? el "list-group-item-success"))
                (dommy/has-class? el "list-group-item-danger")
                (dommy/has-class? el "invalid")))))
        #(:result %)))))

