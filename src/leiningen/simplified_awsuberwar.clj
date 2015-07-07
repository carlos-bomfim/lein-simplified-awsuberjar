(ns leiningen.simplified-awsuberwar
  (:require [leiningen.compile :as compile]
            [leiningen.core.classpath :as classpath]
            [leiningen.core.project :as project]
            [leiningen.ring.uberwar :as uberwar]
            [leiningen.ring.util :as util]
            [leiningen.ring.war :as war]
            [robert.hooke]))


(defn skip-file? [original_func project war-path file]
  (and (not (re-find #".ebextensions.*" war-path))
       (original_func project war-path file)))


(def hooked (atom 0))


(defn simplified-awsuberwar
  "Creates a valid Amazon web services WAR that
   is deployable to servlet containers."
  ([project]
     (simplified-awsuberwar project (uberwar/default-uberwar-name project)))
  ([project war-name]
     (when (= 0 @hooked)
       (do
         (robert.hooke/add-hook #'war/skip-file? #'skip-file?)
         (swap! hooked inc)))
     (uberwar/uberwar project war-name)))
