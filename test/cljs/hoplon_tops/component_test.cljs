(ns hoplon-tops.component-test
  (:require-macros
    [cemerick.cljs.test
     :refer [is deftest with-test run-tests testing test-var]]
    [clojure.test.check.clojure-test :refer [defspec]]
    [dommy.macros :refer [sel sel1 node]])
  (:require
    [cemerick.cljs.test :as t]
    [clojure.test.check :as tc]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop :include-macros true]
    [clojure.string :as str]
    [dommy.utils :as utils]
    [dommy.core :as dommy]))

(def runs 100)

(deftest basic
  (is false))