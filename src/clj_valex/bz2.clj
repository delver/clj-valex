;; Utility function to slurp data from compressed bz2 files

(ns clj-valex.bz2
  (:import [java.io FileInputStream BufferedInputStream 
                    InputStreamReader BufferedReader]
           [org.apache.commons.compress.compressors CompressorStreamFactory]))

(def buffered-reader 
  (let [factory (CompressorStreamFactory.)]
    (fn [f]
      (->>
        (clojure.java.io/file f)
        (FileInputStream.) 
        (BufferedInputStream.)
        (.createCompressorInputStream factory)
        (InputStreamReader.)
        (BufferedReader.)))))

(defn to-list [^BufferedReader rdr]
  (loop [lines []]
    (if-let [line (.readLine rdr)]
      (recur (conj lines line))
      lines)))

(defn to-string [^BufferedReader rdr]
  (loop [sb (StringBuilder.)]
      (if-let [line (.readLine rdr)]
        (recur (-> sb (.append line) (.append "\n")))
        (str sb))))

(defn uncompress
  "Reads the contents of the compressed file according to the reader-fn,
   which must be able to take an argument of java.io.BufferedReader. 
   Note: the function should not be lazy, as the stream will be closed 
   before returning"
  [bz2-file reader-fn]
  (with-open [#^BufferedReader rdr (buffered-reader bz2-file)]
    (reader-fn rdr)))
