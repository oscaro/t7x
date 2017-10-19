(defproject com.oscaro/t7x "0.1.1-SNAPSHOT"
  :description "Transifex API client"
  :url "https://github.com/oscaro/t7x"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure            "1.8.0"]
                 [clj-http                       "3.7.0"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [dk.ative/docjure            "1.11.0"]]

                   :source-paths ["dev"]}})
