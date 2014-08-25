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
    [hoplon-tops.rpc :as rpc]
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

(def tops-component (c/tops-component rpc/state))
(reset! rpc/state [{:word "abcdef" :origin :server}])

(describe
  {:doc "Test whole component"
   :globals [elp tops-component]}
  (it "Should add new word to the list on button click"
    (runs (dommy/append! js/document.body elp))
    (waits-for "Rendering" 100 (dommy/has-class? (sel1 :li) "list-group-item"))
    (runs
      (let [elc (.-firstChild elp)
            h (aget (.-childNodes elc) 0)
            i (js/jQuery (sel1 :input))
            b (js/jQuery (sel1 :button))
            wl (sel1 :ul)]
        (is (.-childElementCount elp) 1)
        (is (.-childElementCount wl) 1)
        (is (dommy/text (.-firstChild wl)) "abcdef")
        (is (.val i) "")
        (.focus i)
        (.val i "blabla")
        (is (.val i) "blabla")
        (.change i)
        (is (.text b) "Submit")
        (.click b)
        (is (.-childElementCount wl) 2)
        (is (dommy/text (.-firstChild wl)) "blabla")
        (is (dommy/has-class? (.-firstChild wl) "list-group-item") true)))))
