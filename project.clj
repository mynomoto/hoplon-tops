(defproject hoplon-tops "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript  "0.0-2227"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [fsrun "0.1.2"]]

  :source-paths ["src/clj"]

  :cljsbuild
  {:builds []
   :test-commands {"unit" ["phantomjs" :runner
                           "test/vendor/es5-shim.js"
                           "test/vendor/es5-sham.js"
                           "test/vendor/console-polyfill.js"
                           "window.literal_js_was_evaluated=true"
                           "resources/public/c6f4dce0-0384-11e4-9191-0800200c9a66.js"]}})
