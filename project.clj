(defproject com.oscaro/t7x "0.1.1-SNAPSHOT"
  :description "Transifex API client"
  :url "https://github.com/oscaro/t7x"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["snapshots" {:url "https://repo.clojars.org"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]
                        ["releases"  {:url "https://repo.clojars.org"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]]
  :dependencies [[org.clojure/clojure            "1.8.0"]
                 [clj-http                       "3.7.0"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [dk.ative/docjure            "1.11.0"]]

                   :source-paths ["dev"]}})
