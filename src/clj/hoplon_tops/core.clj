(ns hoplon-tops.core
  (:require
    [ring.util.response               :refer [file-response]]
    [ring.adapter.jetty               :refer [run-jetty]]
    [compojure.core                   :refer [defroutes GET POST]]
    [compojure.route                  :refer [files]]
    [ring.middleware.session          :refer [wrap-session]]
    [ring.middleware.session.cookie   :refer [cookie-store]]
    [tailrecursion.castra.handler     :refer [castra]]))

(defn index []
  (file-response "public/index.html" {:root "resources"}))

(defroutes routes
  (GET "/" [] (index))
  (POST "/" [] (castra 'hoplon-tops.api))
  (files "/" {:root "resources/public"}))

(def app
  (-> routes
      (wrap-session {:store (cookie-store {:key "a 16-byte secret"})})))

(defonce server
  (run-jetty #'app {:port 8000 :join? false}))
