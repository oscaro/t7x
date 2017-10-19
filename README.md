# t7x

**t7x** is a [Transifex][] API client written in Clojure.

[Transifex]: https://www.transifex.com/

## Usage

```clojure
[com.oscaro/t7x "0.1.0"]
```


### Examples

#### Download an XLSX resource as a Docjure workbook

We'll use Docjure for this example:


```clojure
[dk.ative/docjure "1.11.0"]
```

```clojure
(ns your-ns
  (:require [t7x.client :as tx]
            [clojure.java.io :as io]
            [dk.ative.docjure.spreadsheet :as sp]))

(defn get-workbook
  []
  (-> (tx/mk-client "your-api-key")
      (tx/download-bin-resource "myproject" "myxlsx")
      io/input-stream
      sp/load-workbook))
```

## License

Copyright © 2017 Oscaro
