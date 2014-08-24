(ns hoplon-tops.core-test
  (:require-macros
    [purnam.test :refer [describe it is is-not runs waits-for]]
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

(describe
  {:doc "Generative testing word-item"}
  (it "Should add correct classes to server words"
    (tc/quick-check 100
      (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
        (let [el (c/word-item (cell (assoc w :origin :server)))]
          (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
          (runs
            (is (dommy/text el) (:word w))
            (is (dommy/has-class? el "list-group-item") true)
            (is (dommy/has-class? el "list-group-item-warning") false)
            (is (dommy/has-class? el "list-group-item-success") false)
            (is (dommy/has-class? el "list-group-item-danger") false)
            (is (dommy/has-class? el "invalid") false))
          true))))
  (it "Should add correct classes to local words"
    (tc/quick-check 100
      (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
        (let [el (c/word-item (cell (assoc w :origin :local)))]
          (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
          (runs
            (is (dommy/text el) (:word w))
            (is (dommy/has-class? el "list-group-item") true)
            (is (dommy/has-class? el "list-group-item-warning") true)
            (is (dommy/has-class? el "list-group-item-success") false)
            (is (dommy/has-class? el "list-group-item-danger") false)
            (is (dommy/has-class? el "invalid") false))
          true))))
  (it "Should add correct classes to local valid words"
    (tc/quick-check 100
      (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
        (let [el (c/word-item (cell (assoc w :origin :local :valid true)))]
          (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
          (runs
            (is (dommy/text el) (:word w))
            (is (dommy/has-class? el "list-group-item") true)
            (is (dommy/has-class? el "list-group-item-warning") false)
            (is (dommy/has-class? el "list-group-item-success") true)
            (is (dommy/has-class? el "list-group-item-danger") false)
            (is (dommy/has-class? el "invalid") false))
          true))))
  (it "Should add correct classes to local invalid words"
    (tc/quick-check 100
      (prop/for-all [w (gen/hash-map :word gen/string-ascii)]
        (let [el (c/word-item (cell (assoc w :origin :local :invalid true)))]
          (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
          (runs
            (is (dommy/text el) (:word w))
            (is (dommy/has-class? el "list-group-item") true)
            (is (dommy/has-class? el "list-group-item-warning") false)
            (is (dommy/has-class? el "list-group-item-success") false)
            (is (dommy/has-class? el "list-group-item-danger") true)
            (is (dommy/has-class? el "invalid") true))
          true)))))

(describe
  {:doc  "Testing word-item"
   :vars [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))]}
  (it "Should add correct classes to server words"
    (let [el (c/word-item (cell (assoc w :origin :server)))]
      (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
      (runs
        (is (dommy/text el) (:word w))
        (is (dommy/has-class? el "list-group-item") true)
        (is (dommy/has-class? el "list-group-item-warning") false)
        (is (dommy/has-class? el "list-group-item-success") false)
        (is (dommy/has-class? el "list-group-item-danger") false)
        (is (dommy/has-class? el "invalid") false))))
  (it "Should add correct classes to local words"
    (let [el (c/word-item (cell (assoc w :origin :local)))]
      (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
      (runs
        (is (dommy/text el) (:word w))
        (is (dommy/has-class? el "list-group-item") true)
        (is (dommy/has-class? el "list-group-item-warning") true)
        (is (dommy/has-class? el "list-group-item-success") false)
        (is (dommy/has-class? el "list-group-item-danger") false)
        (is (dommy/has-class? el "invalid") false))))
  (it "Should add correct classes to local valid words"
    (let [el (c/word-item (cell (assoc w :origin :local :valid true)))]
      (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
      (runs
        (is (dommy/text el) (:word w))
        (is (dommy/has-class? el "list-group-item") true)
        (is (dommy/has-class? el "list-group-item-warning") false)
        (is (dommy/has-class? el "list-group-item-success") true)
        (is (dommy/has-class? el "list-group-item-danger") false)
        (is (dommy/has-class? el "invalid") false))))
  (it "Should add correct classes to local invalid words"
    (let [el (c/word-item (cell (assoc w :origin :local :invalid true)))]
      (waits-for "Class must be set" 0 (dommy/has-class? el "list-group-item"))
      (runs
        (is (dommy/text el) (:word w))
        (is (dommy/has-class? el "list-group-item") true)
        (is (dommy/has-class? el "list-group-item-warning") false)
        (is (dommy/has-class? el "list-group-item-success") false)
        (is (dommy/has-class? el "list-group-item-danger") true)
        (is (dommy/has-class? el "invalid") true)))))

