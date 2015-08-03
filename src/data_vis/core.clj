(ns data-vis.core)

(use 'incanter.core
     'incanter.io)

(def data-out  (read-dataset "data/AERS-ASCII-2011q3/ascii/OUTC11Q3.TXT" :header true :delim \$))
(def data-drug (read-dataset "data/AERS-ASCII-2011q3/ascii/DRUG11Q3.TXT" :header true :delim \$))
(def data-reactions (read-dataset "data/AERS-ASCII-2011q3/ascii/REAC11Q3.TXT" :header true :delim \$))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(defn lazy-read-csv
  [csv-file]
  (let [in-file (io/reader csv-file)
        csv-seq (csv/read-csv in-file :separator \$)
        lazy (fn lazy [wrapped]
               (lazy-seq
                (if-let [s (seq wrapped)]
                  (cons (first s) (lazy (rest s)))
                  (.close in-file))))]
    (lazy csv-seq)))


(def data
  (read-dataset
    "http://github.com/liebke/incanter/raw/master/data/cars.csv"
    :header true))

(def data (incanter.datasets/get-dataset :cars))

(dim data)

(with-data data
  (def lm (linear-model ($ :dist) ($ :speed)))
  (doto (scatter-plot ($ :speed) ($ :dist))
    (add-lines ($ :speed) (:fitted lm))
    view))

(nrow data-out)
(nrow data-drug)

;;;; will not work as matrix operation on works with the data being double (not string or anything - its a pure mathematical matrix and not a data matrix??????????)
;;
;;;(def va-matrix-out
;;  (to-matrix ($ [:ISR :OUTC_COD :col2] data-out))

(def data-out-group ($group-by :ISR data-out))

(def cars (get-dataset :cars))

(with-data cars
    (view ($where {:speed {:$gt -10 :$lt 10}}))
    (view ($where {:dist {:$in #{10 12 16}}}))
    (view ($where {:dist {:$nin #{10 12 16}}})))

(view (scatter-plot :Sepal.Length :Sepal.Width :data (get-dataset :iris)))

(def data-out-group-count ($rollup :count :ISR :OUTC_COD data-out))

(def data-out-group-count-isr ($rollup :count :ISR :ISR data-out))

(use '(incanter core datasets))

(def iris (get-dataset :iris))

($rollup :count :Sepal.Length :Species iris)

(def hair-eye-color (get-dataset :hair-eye-color))
($rollup :mean :count [:hair :eye] hair-eye-color)
($rollup :count :count [:hair :eye] hair-eye-color)

(def do-rollup  ($rollup :count :col2 [:ISR :OUTC_COD] data-out))
(def dor2  ($rollup :count :count [:ISR :OUTC_COD] data-out))


(def du1 ($rollup :count :count [:ISR :DRUGNAME] data-drug))

(def reactroll ($rollup :count :count [:ISR :PT] data-reactions))


;; Join DRUG and REACTIONS
(view ($join [:ISR :ISR] data-drug data-reactions))

(def dr ($rollup :count :reccount [:DRUGNAME :PT] ($join [:ISR :ISR] data-drug data-reactions)))

(def dr-order-desc ( $order :reccount :desc dr))
(def dr-order-asc ($order :reccount :asc dr))

(view dr-order-asc)
(view dr-order-desc)

(view ($order :reccount :desc dr))


($where {:PT "MYOCARDIAL INFARCTION"} ($where {:DRUGNAME "AVANDIA"} dr))
($where {:PT "BLADDER CANCER"} ($where {:DRUGNAME "ACTOS"} dr))

;; Build some UI to plot the scatter chart for the highest 10 count

(use '(incanter core stats charts io))

(with-data (take 10 dr-order-desc)
  (doto (scatter-plot ($ :DRUGNAME) ($ :PT)))
    view)
