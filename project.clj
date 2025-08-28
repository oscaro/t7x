(defproject com.oscaro/t7x "0.1.2-SNAPSHOT"
  :description "Transifex API client"
  :url "https://github.com/oscaro/t7x"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-marginalia "0.9.1"]]
  :deploy-repositories [["snapshots" {:url "https://repo.clojars.org"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]
                        ["releases"  {:url "https://repo.clojars.org"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]]
  :dependencies [[org.clojure/clojure            "1.12.2"]
                 [clj-http                       "3.13.1"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}
                   :dependencies [[org.clojure/tools.namespace "1.5.0"]
                                  [dk.ative/docjure            "1.21.0"]]

                   :source-paths ["dev"]}})
