(ns t7x.client
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]))

(defn- make-opts
  [client opts]
  (merge-with merge opts (:opts client)))

(defn- make-url
  [client path]
  (str (:baseurl client) path))

(defn- call
  [client m path opts]
  (let [url (make-url client path)
        opts (make-opts client opts)]
    (http/request
      (merge {:method m
              :url url}
             opts))))

(defn- project-path [project-slug] (format "/project/%s" project-slug))
(defn- resource-path [project-slug resource-slug] (format "%s/resource/%s" (project-path project-slug) resource-slug))
(defn- resource-translations-path [project-slug resource-slug lang-code]
  (format "%s/translation/%s" (resource-path project-slug resource-slug) lang-code))
(defn- create-resource-path [project-slug] (format "%s/resources" (project-path project-slug)))

(defn- download-resource*
  [client project-slug resource-slug opts]
  (let [path (str (resource-path project-slug resource-slug) "/content")
        opts (update opts :query-params #(assoc % "file" "1"))]
    (call client :get path opts)))

(defn- download-resource-translations*
  [client project-slug resource-slug lang-code opts]
  (let [path (resource-translations-path project-slug resource-slug lang-code)
        opts (update opts :query-params #(merge % {:file 1} (:query-params opts)))]
    (:body (call client :get path opts))))

(defn- info-resource*
  [client project-slug resource-slug opts]
  (let [path (resource-path project-slug resource-slug)]
    (:body (call client :get path opts))))

(defn- args->map [args]
  {:pre [(even? (count args))]}
  (->> (partition 2 args)
       (map vec)
       (filter (fn [[param value]] value))                  ;; because Transifex interprets ?details=false as "I want details"
       (into {})))

(defn- mk-multipart [args]
  (map (fn [[k v]] {:name (name k) :content v}) args))

(defn- upload-resource-translation*
  [client project-slug resource-slug lang-code content args]
  (let [path (resource-translations-path project-slug resource-slug lang-code)
        args (assoc (args->map args) :file content)]
    (:body (call client :put path {:multipart (mk-multipart args)}))))

(defn- create-resource*
  [client project-slug args]
  (let [path (create-resource-path project-slug)]
    (:body (call client :post path {:multipart (mk-multipart args)}))))

(defn mk-client
  "Create an API client"
  ([api-token]
   (mk-client "api" api-token))
  ([user password]
   {:baseurl "https://www.transifex.com/api/2"
    :opts    {:basic-auth [user password]}}))

(defn env->client
  "Create an API client from the environment. It assumes you set
   TRANSIFEX_API_KEY to a valid Transifex API token."
  []
  (when-let [api-key (System/getenv "TRANSIFEX_API_KEY")]
    (mk-client api-key)))

(defn download-resource
  "Download a resource and return it as a string"
  [client project-slug resource-slug]
  (:body (download-resource* client project-slug resource-slug nil)))

(defn download-bin-resource
  "Download a binary resource and return it as a byte array"
  [client project-slug resource-slug]
  (:body
    (download-resource* client project-slug resource-slug {:as :byte-array})))

(defn info-resource
  "Retrieve detailed informations about resource as a json string.
  extra possible args are :
  - :details (bool): extra fields are returned"
  [client project-slug resource-slug & args]
  (info-resource* client project-slug resource-slug {:query-params (args->map args)}))

(defn download-resource-translations
  "Download resource translations in given language as a string.
  extra possible args are :
  - :mode (string): default or reviewed or translator or onlytranslated or onlyreviewed or sourceastranslation
  - :file (string): file format. Don't specify to get the original file format."
  [client project-slug resource-slug lang-code & args]
  (download-resource-translations* client project-slug resource-slug lang-code {:query-params (args->map args)}))

(defn download-bin-resource-translations
  "Download resource translations in given language as a byte array.
  extra possible args are :
  - :mode (string): default or reviewed or translator or onlytranslated or onlyreviewed or sourceastranslation
  - :file (string): file format. Don't specify to get the original file format."
  [client project-slug resource-slug lang-code & args]
  (download-resource-translations* client project-slug resource-slug lang-code {:query-params (args->map args)
                                                                                :as           :byte-array}))

(defn create-resource
  "Create a new resource. Returns a json summary.
  - project-slug: the slug of the project where to create the new resource
  - resource-slug: the slug for the new resource
  - name: the display name for the new resource
  - i18n-type: XLIFF, PO, etc. see https://docs.transifex.com/formats/introduction
  - source: the source translations. Can be a java.io.File or a java.lang.Byte array
  extra possible args are:
  - :accept_translations (boolean)
  - :category or :categories
  - :priority (int): 0 for Normal, 1 for High, 2 for Urgent
  - :mp4_url or :ogg_url or :webm_url or :youtube_url"
  [client project-slug resource-slug name i18n-type source & args]
  (let [args (assoc (args->map args) :slug resource-slug
                                     :name name
                                     :i18n_type i18n-type
                                     :content source)]
    (create-resource* client project-slug args)))

(defn upload-resource-translation
  "Upload file as new translations for resource. Returns a json summary.
  translations can be a java.io.File or a java.lang.Byte array.
  extra possible args are:
  - :file_type (string): xliff is the only possible value"
  [client project-slug resource-slug lang-code translations & args]
  (upload-resource-translation* client project-slug resource-slug lang-code translations args))
