(ns clj-valex.test.client
  (:use [clojure.test]
        [clj-valex.bz2]))


(def photograph (partial uncompress "resources/photograph.lex.bz2"))

; $ bzcat resources/photograph.lex.bz2 | wc 
;   3123   20697  190344
;
; ... off-by-one?  :-s

(deftest uncompress-to-list
  (is (= 3124 (count (photograph to-list)))))

(deftest uncompress-to-string
  (is (= 190345 (count (photograph to-string)))))

