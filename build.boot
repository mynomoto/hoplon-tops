#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.5.1"

(set-env!
  :project      'hoplon-tops
  :version      "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.task   "2.2.4"]
                  [tailrecursion/hoplon      "5.10.22"]]
  :out-path     "resources/public"
  :src-paths    #{"src/hl" "src/cljs" "src/clj"})

;; Static resources (css, images, etc.):
(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all]
         '[tailrecursion.castra.task :as c])

(deftask dev
  "Build hoplon-tops for development."
  []
  (comp (watch) (hoplon {:prerender false}) (c/castra-dev-server 'hoplon-tops.api)))

(deftask prod
  "Build hoplon-tops for production."
  []
  (hoplon {:optimizations :advanced}))
