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

(deftest ^:async server-word-item
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-item (cell (assoc w :origin :server)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (not (dommy/has-class? el "list-group-item-warning")))
        (is (not (dommy/has-class? el "list-group-item-success")))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-item
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-item (cell (assoc w :origin :local)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (dommy/has-class? el "list-group-item-warning"))
        (is (not (dommy/has-class? el "list-group-item-success")))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-item-valid
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-item (cell (assoc w :origin :local :valid true)))]
    (js/setTimeout
      (fn []
        (is (= (:word w) (dommy/text el)))
        (is (dommy/has-class? el "list-group-item"))
        (is (not (dommy/has-class? el "list-group-item-warning")))
        (is (dommy/has-class? el "list-group-item-success"))
        (is (not (dommy/has-class? el "list-group-item-danger")))
        (is (not (dommy/has-class? el "invalid")))
        (done)) 0)))

(deftest ^:async local-word-item-invalid
  (let [w (last (gen/sample (gen/hash-map :word gen/string-ascii)))
        el (c/word-item (cell (assoc w :origin :local :invalid true)))]
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
        el (c/word-list (cell ws))
        _ (dommy/append! js/document.body el)]
    (js/setTimeout
      (fn []
        (is (= (count w) (.-childElementCount el)))
        (is (dommy/has-class? el "list-group"))
        (is (= (str/join (map :word (reverse w))) (dommy/text el)))
        (done)) 0)))

(deftest ^:async word-input
  (let [w (last (gen/sample (gen/vector (gen/hash-map :word gen/string-ascii) 0 10)))
        ws (cell (mapv #(assoc % :origin :server) w))
        el (c/word-input ws)
        in (.-firstChild el)
        sp (aget (.-childNodes el) 1)
        bt (.-firstChild sp)]
    (js/setTimeout
      (fn []
        (is (dommy/has-class? el "input-group"))
        (is (= 2 (.-childElementCount el)))
        (is (dommy/has-class? in "form-control"))
        (is (= "" (.-value in)))
        (is (= 1 (.-childElementCount sp)))
        (is (dommy/has-class? sp "input-group-btn"))
        (is (dommy/has-class? bt "btn"))
        (is (dommy/has-class? bt "btn-primary"))
        (is (= (dommy/text bt) "Submit"))
        (done)) 0)))

(deftest ^:async tops-component-test
  (let [w (last (gen/sample (gen/vector (gen/hash-map :word gen/string-ascii) 0 10)))
        ws (cell (mapv #(assoc % :origin :server) w))
        elp (c/tops-component ws)
        _ (dommy/append! js/document.body elp)
        elc (.-firstChild elp)
        h (aget (.-childNodes elc) 0)
        wl (aget (.-childNodes elc) 2)]
    (js/setTimeout
      (fn []
        (is (= 1 (.-childElementCount elp)))
        (is (= 3 (.-childElementCount elc)))
        (is (= 3 (-> elc .-childNodes .-length)))
        (is (dommy/has-class? elp "row"))
        (is (dommy/has-class? elc "col-lg-4"))
        (is (dommy/has-class? elc "col-md-5"))
        (is (dommy/has-class? elc "col-sm-6"))
        (is (= "Hoplon Tops" (dommy/text h)))
        (is (= (count w) (.-childElementCount wl)))
        (is (dommy/has-class? wl "list-group"))
        (is (= (str/join (map :word (reverse w))) (dommy/text wl)))
        (done)) 0)))
