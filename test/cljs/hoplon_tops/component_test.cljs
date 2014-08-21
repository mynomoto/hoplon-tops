(ns hoplon-tops.component-test
  (:require-macros
    [cemerick.cljs.test
     :refer [is deftest with-test run-tests testing test-var done]]
    [clojure.test.check.clojure-test :refer [defspec]]
    [dommy.macros :refer [sel sel1 node]]
    [tailrecursion.hoplon :refer [with-init!]])
  (:require
    [cemerick.cljs.test :as t]
    [tailrecursion.javelin :refer [cell]]
    [clojure.test.check :as tc]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop :include-macros true]
    [clojure.string :as str]
    [hoplon-tops.component :as c]
    [dommy.utils :as utils]
    [dommy.core :as dommy]))

(def runs 100)

(deftest ^:async server-word-view
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-view (cell (assoc w :origin :server)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (not (dommy/has-class? el "list-group-item-warning")))
        (is (not (dommy/has-class? el "list-group-item-success")))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-view
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-view (cell (assoc w :origin :local)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (dommy/has-class? el "list-group-item-warning"))
        (is (not (dommy/has-class? el "list-group-item-success")))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-view-valid
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-view (cell (assoc w :origin :local :valid true)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (not (dommy/has-class? el "list-group-item-warning")))
        (is (dommy/has-class? el "list-group-item-success"))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-view-invalid
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-view (cell (assoc w :origin :local :invalid true)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (not (dommy/has-class? el "list-group-item-warning")))
        (is (not (dommy/has-class? el "list-group-item-success")))
        (is (dommy/has-class? el "list-group-item-danger"))
        (is (dommy/has-class? el "invalid"))
        (done)) 0)))

(deftest ^:async word-list-test
  (let [w (last (gen/sample (gen/vector (gen/hash-map :word gen/string-ascii) 0 10)))
        ws (mapv #(assoc % :origin :server) w)
        body js/document.body
        el (c/word-list (cell ws))
        _ (dommy/append! body el)]
    (js/setTimeout
      (fn []
        (is (dommy/has-class? el "list-group"))
        (is (= (count w) (.-childElementCount el)))
        (is (= (str/join (map :word (reverse w))) (dommy/text el)))
        (done)) 0)))
