(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-http.client :as http]
            [clojure.string :as cs]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]

            [dk.ative.docjure.spreadsheet :as sp]

            [t7x.client :as tx]))
